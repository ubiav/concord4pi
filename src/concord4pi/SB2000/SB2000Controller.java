package concord4pi.SB2000;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

import com.fazecast.jSerialComm.*;

import concord4pi.logging.LogEngine;

import org.tinylog.*;

public class SB2000Controller implements Runnable {
	private Queue<IBaseMessage> txQueue = new ConcurrentLinkedQueue<IBaseMessage>();
	private Queue<IBaseMessage> rxQueue = new ConcurrentLinkedQueue<IBaseMessage>();
	private Queue<IBaseMessage> txVerifyQueue = new ConcurrentLinkedQueue<IBaseMessage>();
	
	private final int maxSimultaneousThreads = 1;
	Semaphore lock = new Semaphore(maxSimultaneousThreads);
	private String dataToProcess = "";
	private boolean newDataWaiting = false;
	
	private SerialController serialPort;
	private LogEngine logger;
	private boolean isRunning = false;
	
	public SB2000Controller(String serialDeviceName, LogEngine logger) {
		this.logger = logger;
		isRunning = true;
		serialPort = new SerialController(serialDeviceName, logger);
		setupCallbacks();
	}
	
	public void setupCallbacks() {
		serialPort.addDataListener(new serialDataListener());
	}
	
	public void run() {
		while(isRunning) {
			//process all the queues
			//first check to see if there is anything on the transmit queue
			processOutgoingMessages();
			
			//then process anything that is incoming
			processIncomingMessages();
		}
	}
	
	public boolean messageWaiting() {
		return !rxQueue.isEmpty();
	}
	
	public IOMessage getNextMessage() {
		return (IOMessage)rxQueue.poll();
	}
	
	public void sendMessage(IOMessage newMessage) {
		txQueue.offer(newMessage);
	}
	
	public void sendMessages(IOMessage[] newMessages) {
		for(IOMessage newMessage: newMessages) {
			sendMessage(newMessage);
		}
	}
	
	public void shutdown() {
		isRunning = false;
		serialPort.removeListener();
		serialPort.closePort();
	}
	
	private void processOutgoingMessages() {
		IBaseMessage message;
		while(!txQueue.isEmpty()) {
			//send the messages in the transmit queue
			message = txQueue.poll();
			if(message.isControlMessage()) {
				serialPort.writeByte(((ControlMessage)message).message());
			}
			else {
				serialPort.writeBytes(((IOMessage)message).toString());
				txVerifyQueue.offer(message);
			}			
		}
	}
	
	private void processIncomingMessages() {
		String dataProcessorString;
		
		//exit for zero length arrays
		if((dataToProcess.length() == 0) || !newDataWaiting) { return; }
		
		try {
			//use semaphores so that we don't write and read at the same time
			lock.acquire();
			logger.log("Acquired lock for data processing", Level.TRACE);
			dataProcessorString = dataToProcess;
			dataToProcess = "";
			newDataWaiting = false;
			
			while(dataProcessorString.length() > 0) {
				//search through the characters to find messages and control characters;
				byte testChar = (byte)dataProcessorString.charAt(0);
				if(testChar == SB2000Constants.ACK) {
					//manage the ACK
					//remove this character from the message string
					dataProcessorString = dataProcessorString.substring(1);
	
					//since I got an ACK... just throw the message away and dispose of the item on the verify queue, we're good.
					logger.log("Received ACK for message: [" + (IOMessage)txVerifyQueue.peek() + "]", Level.TRACE);
					txVerifyQueue.poll();
	
				}
				else if(testChar == SB2000Constants.NAK) {
					//manage the NAK
					//remove this character from the message string
					dataProcessorString = dataProcessorString.substring(1);
	
					//since it is a NAK, I need to re-transmit
					IOMessage theMessage = (IOMessage)txVerifyQueue.poll();
					if(theMessage != null) {
						if(theMessage.isValid()) {
							logger.log("Re-transmitting Message due to NAK: [" + theMessage + "]", Level.DEBUG);
							theMessage.incrementTransmissionCount();
							if(theMessage.isReTransmittable()) {
								txQueue.offer(theMessage);
							}
							else {
								logger.log("Maximum number of retransmissions reached: [" + theMessage + "]", Level.WARN);
							}
						}
						else {
							logger.log("Discarding Invalid Message queued for re-transmit: [" + theMessage + "]", Level.DEBUG);
						}
					}
					else {
						logger.log("Received a NAK, but no messages to retransmit!", Level.DEBUG);
					}				
				}
				else if(testChar == SB2000Constants.MESSAGE_START) {
					//remove this character from the message string
					dataProcessorString = dataProcessorString.substring(1);
	
					if(dataProcessorString.length() > 2) {
						//there is enough room to check for a message length
						//add one to include the checksum byte
						int length = 0;
						try {
							//Try to parse the message.  Ignore and discard it if exception occurs
							length = Integer.parseInt(dataProcessorString.substring(0,2), 16) + 1;
						}
						catch (Exception e) {
							logger.log("Could not determine message length from message.  This shouldn't be the case.  Ignoring message... ", Level.WARN);
							length = -1;
						}
						
						//check if there is a full message according to the expected length
						//multiply by 2 since each number is represented by two ASCII characters
						if((length > 0) && (dataProcessorString.length() >= (length * 2))) {
							String newMessageText = dataProcessorString.substring(0, (length * 2)).trim();
							logger.log("Discovered new message [" + newMessageText + "]", Level.DEBUG);
							
							IOMessage newMessage = new IOMessage(newMessageText.getBytes());
							
							if(newMessage.isValid()) {
								rxQueue.offer(newMessage);
								txQueue.offer(new ControlMessage(SB2000Constants.ACK));
								logger.log("Received valid message [" + newMessageText + "]", Level.DEBUG);
							}
							else {
								txQueue.offer(new ControlMessage(SB2000Constants.NAK));
								logger.log("Received invalid message [" + newMessageText + "]", Level.DEBUG	);
							}
							
							//erase the message from the one I just pulled out
							dataProcessorString = dataProcessorString.substring((length * 2));
						}
						else {
							//let's see if there is another MESSAGE START in the message
							int nextStart;
							if((nextStart = dataProcessorString.indexOf(SB2000Constants.MESSAGE_START)) >= 0) {
								//there is another message in here... the first one is probably corrupted
								//let's just go with it and discard the first message by invalidating it
								String newMessageText = dataProcessorString.substring(0, nextStart).trim();
								logger.log("Discovered new message [" + newMessageText + "]", Level.DEBUG);
								
								IOMessage newMessage = new IOMessage(newMessageText.getBytes());
								
								if(newMessage.isValid()) {
									rxQueue.offer(newMessage);
									txQueue.offer(new ControlMessage(SB2000Constants.ACK));
									logger.log("Received valid message [" + newMessageText + "]", Level.DEBUG);
								}
								else {
									txQueue.offer(new ControlMessage(SB2000Constants.NAK));
									logger.log("Received invalid message [" + newMessageText + "]", Level.DEBUG	);
								}								
								dataProcessorString = dataProcessorString.substring(nextStart);
							}
							else {
								dataToProcess = new String(new byte[] {SB2000Constants.MESSAGE_START}) + dataProcessorString;
								logger.log("Not enough data to process message [" + dataProcessorString + "]... requeuing...", Level.TRACE);
								dataProcessorString = "";
								return;
							}
						}

					}
					else {
						dataToProcess = new String(new byte[] {SB2000Constants.MESSAGE_START}) + dataProcessorString;
						logger.log("Not enough data to determine message length [" + dataProcessorString + "]... requeuing...", Level.TRACE);
						dataProcessorString = "";
						return;
					}
				}
				else {
					//couldn't find any control characters in the string.  I'll discard this text and wait for the next message to come in
					logger.log("MESSAGE START code not found [" + dataProcessorString + "]", Level.TRACE);
					dataToProcess = new String(new byte[] {SB2000Constants.MESSAGE_START}) + dataProcessorString;
					dataProcessorString = "";
					return;
				}
			}
		}
		catch(InterruptedException lockException) {
			logger.log("Could not acquire lock for processing data", Level.WARN);
		}
		finally {
			lock.release();
			logger.log("Release lock for data processing", Level.TRACE);
		}
	}
	
	class serialDataListener implements SerialPortDataListener {
		@Override
		public int getListeningEvents() {
			return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
		}

		@Override
		public void serialEvent(SerialPortEvent event) {
			//if this isn't the correct event, just leave the method
			if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_RECEIVED) {
				return;
			}
			
			try {
				//use semaphores, so we aren't reading and writing to the string
				//at the same time.
				lock.acquire();
				logger.log("Acquired lock for data processing", Level.TRACE);
				dataToProcess += new String(event.getReceivedData());
				newDataWaiting = true;
			}
			catch(InterruptedException lockException) {
				logger.log("Could not acquire lock for processing data", Level.WARN);
			}
			finally {
				lock.release();
				logger.log("Release lock for data processing", Level.TRACE);
			}
		}
	}
}
