package concord4pi;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;

import concord4pi.API.RESTServer;
import concord4pi.MQTT.MQTTService;
import concord4pi.SB2000.*;
import concord4pi.logs.LogEngine;
import concord4pi.serial.SerialInterface;

public class AlarmSystem {
	private QueueProcessor commandProcessor;
	private Thread cpThread;
	
	private State alarmSystemState = new State();
	
	private RESTServer API;
	
	private MQTTService mqtt;
	
	private Queue<Message> txQueue = new ConcurrentLinkedQueue<Message>();
	private Queue<Message> rxQueue = new ConcurrentLinkedQueue<Message>();
	private Queue<Message> controlQueue = new ConcurrentLinkedQueue<Message>();
	
	private SerialInterface comPort;
	
	public AlarmSystem() {
		setupSerialPort();
		setupRESTAPI();
		setupMQTT();
		setupCommandProcessor();
	}
	
	public void start() {
		boolean quitProgram = false;
		
		LogEngine.Log(Level.INFO, "Alarm System Automation Started.", this.getClass().getName());
		txQueue.add(new Message(Constants.FullEquipmentListCommand));
		txQueue.add(new Message(Constants.DynamicDataRefreshCommand));
		
		while(!quitProgram) {
			
			//handle all the incoming data
			//otherwise move on to transmission 
			//and control characters
			
			LogEngine.Log(Level.FINEST, "Looking for data on Serial Port...", this.getClass().getName());

			
			if(comPort.bytesWaiting()) {
				//we have some bytes to read in
				LogEngine.Log(Level.FINEST, "Bytes waiting on Serial Port", this.getClass().getName());
				scanForMessageStart();
				
				Message nextMessage = getNextMessage();
				
				if(nextMessage.validCheckSum()) {
					//got a good message
					//send an ACK
					LogEngine.Log(Level.FINER, "Adding Message [" + nextMessage + "] to RxQueue", this.getClass().getName());
					rxQueue.add(nextMessage);
					LogEngine.Log(Level.FINER, "Adding ACK to TxQueue", this.getClass().getName());
					txQueue.add(new Message(Constants.ACK));
				}
				else {
					//we got a bad message
					//send a NAK and discard the MESSAGE
					LogEngine.Log(Level.FINE, "Received a BAD message [" + nextMessage + "]", this.getClass().getName());
					txQueue.add(new Message(Constants.NAK));
				}
			}
			
			//a quick sleep to keep the threads from locking the CPU.
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				LogEngine.Log(Level.WARNING, e.getMessage(), this.getClass().getName());
			}
		}
		LogEngine.Log(Level.INFO, "Alarm System Automation exiting...", this.getClass().getName());
		shutdownRESTAPI();
		shutdownMQTT();
	}
	
	public void shutdown()  {
		closeSerialPort();
		commandProcessor.stopThread();
		try {
			cpThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		LogEngine.Log(Level.INFO, "Alarm System Automation Stopped.", this.getClass().getName());
	}
	
	private void setupSerialPort() {
		comPort = new SerialInterface();
		comPort.openPort();
	}
	
	private void closeSerialPort() {
		comPort.closePort();
	}
	
	private void setupCommandProcessor() {
		commandProcessor = new QueueProcessor(rxQueue, txQueue, controlQueue, comPort, alarmSystemState);
		cpThread = new Thread(commandProcessor);
		cpThread.start();
	}
	
	private void setupRESTAPI() {
		API = new RESTServer(alarmSystemState, getTxQueue());
		API.start();
	}
	
	private void shutdownRESTAPI() {
		API.stop();
	}
	
	private void setupMQTT() {
		mqtt = new MQTTService(txQueue);
		mqtt.connect();
		alarmSystemState.addMQTTService(mqtt);
	}
	
	private void shutdownMQTT() {
		mqtt.disconnect();
	}
	
	private int scanForMessageStart() {
		int readByte;
		
		readByte = comPort.readByte();
		while(readByte != Constants.MESSAGE_START) {
			LogEngine.Log(Level.FINEST, "Looking for start message... Received [" + readByte + "]", this.getClass().getName());
			if((readByte == Constants.ACK) || (readByte == Constants.NAK)) {
				controlQueue.add(new Message(readByte));
			}
			readByte = comPort.readByte();
		}
		return readByte;
	}

	private Message getNextMessage() {
		StringBuilder rawMessage = new StringBuilder();
		String nextByte;
			   
		//get the length first
		nextByte = findNextASCIICode();
		int length = Integer.parseInt(nextByte, 16);
		rawMessage.append(nextByte);
	
		//get the next "length" bytes...
		//after this, we should have a complete message
		for(int i = 0; i < length; i++) {
			//get a message of the transmitted length
			nextByte = findNextASCIICode();
			if(nextByte.equals((byte)Constants.MESSAGE_START)) {
				//we got a new message character
				//send a NAK to say something broke about this.
				LogEngine.Log(Level.FINER, "Got a MESSAGE START In the middle of a message [" + rawMessage + "]", this.getClass().getName());
				txQueue.add(new Message(Constants.NAK));
				return getNextMessage();
			}
			else {
				rawMessage.append(nextByte);
			}
		}
		
		if(rawMessage.length() != ((length + 1) * 2)) {
			LogEngine.Log(Level.FINER, 
					"Got an actual length (" + rawMessage.length() + ") different than expected (" + (length + 1) * 2 + ")", 
					this.getClass().getName()
			);
		}
		
		return new Message(rawMessage.toString());   
	}
	
	private String findNextASCIICode() {
		StringBuilder rawByte = new StringBuilder();	
		
		//I need two bytes for the ASCII representation of
		//the hex code.
		while(rawByte.length() < 2) {
			int messageByte = comPort.readByte();
			
			//look for the next digit in the hex value... 
			//queue up the control characters, if found
			while((messageByte == Constants.ACK) || (messageByte == Constants.NAK)) {
		   		//queue up the control characters in case I get them.
		   		controlQueue.add(new Message(messageByte));
		   		messageByte = comPort.readByte();
		   	}
		   	
		   	if(messageByte == Constants.MESSAGE_START) {
		   		//something happened start over
				LogEngine.Log(Level.FINER, 
						"GOT MESSAGE_START [" + String.valueOf(Constants.MESSAGE_START) + "]", 
						this.getClass().getName()
				);
		   		return String.valueOf((char)(Constants.MESSAGE_START));
		   	}
		   	else {
			   	rawByte.append((char)messageByte);
		   	}
		}
	  	return rawByte.toString();
	}
	
	public Queue<Message> getTxQueue() {
		return txQueue;
	}
	
	public State getStateObject() {
		return alarmSystemState;
	}
}
