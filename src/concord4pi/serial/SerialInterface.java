package concord4pi.serial;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;

import com.fazecast.jSerialComm.*;

import concord4pi.Config;
import concord4pi.SB2000.Constants;
import concord4pi.logs.LogEngine;

public class SerialInterface {
	SerialPort serialPort;
	
	private String deviceName = Config.getProperty("serialdevice_name");
	private int baudRate = 9600;
	private int dataBits = 8;
	private int stopBits = SerialPort.ONE_STOP_BIT;
	private int parity = SerialPort.ODD_PARITY;
	
	private boolean debugMode = false;
	
	private Queue<String> debugMessage = new ConcurrentLinkedQueue<String>();
	
	
	//constructors
	public SerialInterface()  {
		this(false);
	}

	public SerialInterface(boolean debug) {
		debugMode = debug;
		
		//set up a debug message to receive from a "fake" serial port
		if(debugMode) {
			setupDebugQueue();
		}
		
		setupSerialPort();		
	}
	
	private void setupSerialPort() {
		if(!debugMode) {
			serialPort = SerialPort.getCommPort(deviceName);
			serialPort.setComPortParameters(baudRate, dataBits, stopBits, parity);
			serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, Constants.LOOP_MILLISECOND_DELAY, 0);
		}
	}
	
	public boolean openPort() {
		if(!debugMode) {
			if(serialPort.openPort()) {
				LogEngine.Log(Level.INFO, "Serial Port " + deviceName + " is open", this.getClass().getName());
				return true;
			}
			else {
				LogEngine.Log(Level.SEVERE, "Serial Port " + deviceName + " could not be opened", this.getClass().getName());
				return false;
			}
		}
		else {
			return true;
		}
	}
	
	public boolean closePort() {
		if(!debugMode) {
			if(serialPort.closePort()) {
				LogEngine.Log(Level.INFO, "Serial Port " + deviceName + " is closed", this.getClass().getName());
				return true;
			}
			else {
				LogEngine.Log(Level.WARNING, "Serial Port " + deviceName + " could not be closed", this.getClass().getName());
				return false;
			}
		}
		else {
			return true;
		}
	}
	
	public boolean bytesWaiting() {
		if(!debugMode) {
			if(serialPort != null) {
				int bytesWaiting = serialPort.bytesAvailable();
				LogEngine.Log(Level.FINER,bytesWaiting + " bytes waiting for read", this.getClass().getName() );
				return bytesWaiting > 0;
			}
			else {
				return false;
			}
		}
		else {
			return true;
		}
	}
	
	public int readByte() {
		byte buffer[] = new byte[1];
		int byteInt;
		
		if(!debugMode) {
			if(serialPort.readBytes(buffer, 1) != -1) {
				byteInt = buffer[0] & 0xFF;
				return byteInt;
			}
			else {
				LogEngine.Log(Level.INFO, "Byte could not be read from Serial Port", this.getClass().getName() );
				return 0;
			}
		}
		else {
			if(!debugMessage.isEmpty()) {
				int testByte = (int)debugMessage.poll().charAt(0);
				return testByte;
			}
			else return 0;
		}
	}
	
	public String readBytes(int numberOfBytesToRead) {
		StringBuilder message = new StringBuilder();
		for(int i = 0; i < numberOfBytesToRead; i++) {
			message.append(readByte());
		}
		
		LogEngine.Log(Level.FINE, "Read Bytes: " + message.toString(), this.getClass().getName());

		return message.toString();
	}
	
	public boolean writeMessage(String messageToSend) {
		if(!debugMode) {
			messageToSend = (char)Constants.MESSAGE_START + messageToSend;
			byte[] message = convertStringToByteArray(messageToSend);
			LogEngine.Log(Level.FINER, "Message received for writing to Serial Port [" + messageToSend + ":" + message + "]", this.getClass().getName());
			if(serialPort.writeBytes(message, message.length) != -1) {
				LogEngine.Log(Level.FINE, "Wrote Message to Serial Port: " + new String(message), this.getClass().getName());
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return true;
		}
	}
	
	public boolean writeControlChar(byte commandChar) {
		byte[] commandMessage = new byte[1];
		commandMessage[0] = commandChar;
		if(serialPort.writeBytes(commandMessage, 1) != -1) {
			LogEngine.Log(Level.FINE, "Wrote Command Character to Serial Port: " + commandMessage[0], this.getClass().getName());
			return true;
		}
		else {
			return false;
		}
	}

	private byte[] convertStringToByteArray(String messageToSend) {
		int messageLength = messageToSend.length();
		byte[] byteArray = new byte[messageLength];
		
		for(int i = 0; i < messageLength; i++) {
			byteArray[i] = (byte)messageToSend.charAt(i);
		}
		
		return byteArray;
	}
	
	private void setupDebugQueue() {
		debugMessage.add(String.valueOf((char)Constants.MESSAGE_START));
		debugMessage.add("0");
		debugMessage.add("2");
		debugMessage.add("0");
		debugMessage.add("2");
		debugMessage.add(String.valueOf((char)Constants.MESSAGE_START));
		debugMessage.add("0");
		debugMessage.add("2");
		debugMessage.add("0");
		debugMessage.add("2");
		debugMessage.add("0");
		debugMessage.add("4");
	}
}
