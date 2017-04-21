package concord4pi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;

import concord4pi.SB2000.Constants;
import concord4pi.SB2000.Message;
import concord4pi.SB2000.State;

public class QueueProcessor implements Runnable {
	
	private Queue<Message> rxQueue;
	private Queue<Message> txQueue;
	private Queue<Message> ACKQueue;
	private Queue<Message> controlQueue;
	
	private SerialInterface comPort;
	
	private CommandInterpreter commandInterpreter;
	
	private boolean quitNow = false;
	
	private State alarmSystemState;
	
	public QueueProcessor(Queue<Message> rx, Queue<Message> tx, Queue<Message> control, 
			SerialInterface serialObject, 
			State newState
	) {
		alarmSystemState = newState;
		
		rxQueue = rx;
		txQueue = tx;
		controlQueue = control;
		
		comPort = serialObject;
		
		//this queue is to make sure that we get ACKs back from the
		//automation unit... if we get a NAK, we'll resend.
		ACKQueue = new  ConcurrentLinkedQueue<Message>();
		
		commandInterpreter = new CommandInterpreter(alarmSystemState, txQueue);
		
		LogEngine.Log(Level.INFO, "CommandProcessor Loaded", this.getClass().getName());
	}
	
	@Override
	public void run() {
		Message currentMessage;
		
		LogEngine.Log(Level.INFO, "CommandProcessor Started", this.getClass().getName());
		while(!quitNow) {
			LogEngine.Log(Level.FINEST, "Searching Queues...", this.getClass().getName());
			LogEngine.Log(Level.FINEST, "Searching RX Queue...", this.getClass().getName());
			while(!rxQueue.isEmpty()) {
				currentMessage = rxQueue.poll();
				if(currentMessage != null) {
					LogEngine.Log(Level.FINE, "Received Message [" + currentMessage + "] from RXQueue", this.getClass().getName());
					handleRxMessage(currentMessage);
				}
			}
			
			LogEngine.Log(Level.FINEST, "Searching TX Queue...", this.getClass().getName());
			while(!txQueue.isEmpty()) {
				currentMessage = txQueue.poll();
				if(currentMessage != null) {
					if(currentMessage.validCheckSum()) {
						LogEngine.Log(Level.FINE, "Received Message [" + currentMessage + "] from TXQueue", this.getClass().getName());
						handleTxMessage(currentMessage);
					}
					else {
						LogEngine.Log(Level.FINE, "Received INVALID Message [" + currentMessage + "] from TXQueue; Discarding...", this.getClass().getName());
					}
				}
			}

			LogEngine.Log(Level.FINEST, "Searching Control Queue...", this.getClass().getName());
			while(!controlQueue.isEmpty()) {
				currentMessage = controlQueue.poll();
				if(currentMessage != null) {
					LogEngine.Log(Level.FINE, "Received Message [" + currentMessage + "] from ControlQueue", this.getClass().getName());
					handleControlMessage(currentMessage);
				}
			}
			
			//let's rest the loop just momentarily...
			//maybe this can be removed later
			try {
				Thread.sleep(Constants.LOOP_MILLISECOND_DELAY);
			} catch (InterruptedException e) {
				LogEngine.Log(Level.WARNING, "Could not sleep", this.getClass().getName());
			}
		}
	}
	
	private void handleControlMessage(Message currentMessage) {
		if(ACKQueue.size() > 0) {
			Message retransmitMessage = ACKQueue.poll();
			if(currentMessage.getLength() == Constants.ACK) {
				//must be an ACK
				retransmitMessage = ACKQueue.poll();
				LogEngine.Log(Level.FINE, "Received an ACK for " + retransmitMessage.toString(), this.getClass().getName());
			}
			else {
				//must be a NAK
				//add back in to re-transmit if checksums still look good.
				//otherwise drop it
				LogEngine.Log(Level.FINE, "Getting Message From ACK Queue", this.getClass().getName());
				LogEngine.Log(Level.FINE, "Got a NAK... Adding Message [" + retransmitMessage + "] to txQueue", this.getClass().getName());
				if(retransmitMessage.validCheckSum()) {
					txQueue.add(retransmitMessage);
				}
				else {
					LogEngine.Log(Level.FINE, "INVALID Message [" + retransmitMessage + "] from ACKQueue; Discarding...", this.getClass().getName());
				}
			}
		}
		else {
			LogEngine.Log(Level.FINE, "ACKQueue is empty; can't process all control Messages received; Discarding...", this.getClass().getName());
		}
	}

	private void handleTxMessage(Message currentMessage) {
		if(currentMessage.isControlCommand()) {
			comPort.writeControlChar((byte)currentMessage.getLength());
		}
		else {
			comPort.writeMessage(currentMessage.toString());
			ACKQueue.add(currentMessage);
		}
	}

	private void handleRxMessage(Message currentMessage) {
		String logMessage;
		String[] commandInfo = commandInterpreter.lookupRxCommand(currentMessage.getCommandString());
		LogEngine.Log(Level.FINE, 
				"Received " + commandInfo[0] + " on RXQueue", 
				this.getClass().getName()
		);
		
		LogEngine.Log(Level.FINE, "Trying to call command handler [" + commandInfo[1] + "]", this.getClass().getName());
		Method commandHandler;
		try {
			//get the command Handler and run the command Handler
			commandHandler = commandInterpreter.getClass().getMethod(commandInfo[1], Message.class);
			logMessage = (String)commandHandler.invoke(commandInterpreter, currentMessage);
			LogEngine.Log(Level.INFO, "[" + currentMessage + "]" + commandInfo[1] + " " + commandInfo[0] + " " + logMessage, this.getClass().getName());
		} catch (IllegalAccessException | 
				IllegalArgumentException | 
				InvocationTargetException | 
				NoSuchMethodException | 
				SecurityException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void stopThread() {
		LogEngine.Log(Level.INFO, "Stopping CommandProcessor...", this.getClass().getName());
		quitNow = true;
	}
	
	
}
