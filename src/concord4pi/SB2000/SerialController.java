package concord4pi.SB2000;

import org.tinylog.Level;

import com.fazecast.jSerialComm.*;

import concord4pi.logging.LogEngine;

public class SerialController {

	public SerialPort serialPort;
	private String deviceName;
	private final int baudRate = 9600;
	private final int dataBits = 8;
	private final int stopBits = SerialPort.ONE_STOP_BIT;
	private final int parity = SerialPort.ODD_PARITY;
	
	private LogEngine logger;
	
	public SerialController(String serialPort, LogEngine logger) {
		this.deviceName = serialPort;
		this.logger = logger;
		setupSerialPort();
	}
	
	public void setupSerialPort() {
		serialPort = SerialPort.getCommPort(deviceName);
		serialPort.setComPortParameters(baudRate, dataBits, stopBits, parity);
		//serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, msDelay, 0);
		
		if(serialPort.openPort()) {
			//if we get here, then we have the port opened!
			logger.log("Serial Port (" + deviceName + ") OPEN!", Level.INFO);
		}
		else {
			logger.log("Serial Port (" + deviceName + ") could not be opened!", Level.ERROR);
			System.out.println("Could not connect to the serial port (" + deviceName + ")");
			System.out.println("Please correct and restart");
			System.exit(0);
		}
	}
	
	public void closePort() {
		if(serialPort.closePort()) {
			while(serialPort.isOpen());
			//if we get here, then we have the port closed!
			logger.log("Serial Port (" + deviceName + ") CLOSED!", Level.INFO);
		}
		else {
			logger.log("Serial Port (" + deviceName + ") could not be closed!", Level.ERROR);
			System.out.println("The serial port (" + deviceName + ") may hang on restart until cleared");
			System.exit(0);
		}	
		
	}
	
	public boolean isBytesWaiting() {
		if(serialPort != null) {
			int bytesWaiting = serialPort.bytesAvailable();
			logger.log(bytesWaiting + " bytes waiting for read from serial port", Level.TRACE);
			return bytesWaiting > 0;
		}
		else {
			return false;
		}
	}
	
	public int numBytesWaiting() {
		if(serialPort != null) {
			return serialPort.bytesAvailable();
		}
		else {
			return 0;
		}
	}

	
	public int readByte() {
		byte buffer[] = new byte[1];
		int byteInt;

		if(serialPort.readBytes(buffer, 1) != -1) {
			byteInt = buffer[0] & 0xFF;
			return byteInt;
		}
		else {
			logger.log("Byte could not be read from Serial Port", Level.TRACE);
			return 0;
		}
	}
	
	public byte[] readBytes(int numberOfBytesToRead) {
		byte[] messageData = new byte[numberOfBytesToRead];
		for(int i = 0; i < numberOfBytesToRead; i++) {
			messageData[i] = (byte) readByte();
		}
		logger.log("Read " + numberOfBytesToRead + " Bytes: " + (new String(messageData)), Level.TRACE);
		return messageData;
	}

	public boolean writeByte(byte byteToSend) {
		byte[] message = new byte[1];
		message[0] = byteToSend;
		
		logger.log("Message ready for writing to Serial Port [" + message[0] + "]", Level.TRACE);
		
		if(serialPort.writeBytes(message, message.length) != -1) {
			logger.log("Wrote Message to Serial Port: " + message[0], Level.TRACE);
			return true;
		}
		else {
			logger.log("Could not write message to Serial Port: " + message[0], Level.TRACE);
			return false;
		}
	}
	
	public boolean writeBytes(String stringMessage) {
		byte[] message = convertStringToByteArray(stringMessage);
		return writeBytes(message);
	}
	
	public boolean writeBytes(byte[] message) {
		logger.log("Message ready for writing to Serial Port [" + (new String(message)) + "]", Level.TRACE);
		
		//first write the message start character
		if(serialPort.writeBytes(new byte[] {SB2000Constants.MESSAGE_START}, 1) != -1) {
			//then send the message
			if(serialPort.writeBytes(message, message.length) != -1) {
				logger.log("Wrote Message to Serial Port: " + new String(message), Level.TRACE);
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	private byte[] convertStringToByteArray(String message) {
		int messageLength = message.length();
		byte[] byteArray = new byte[messageLength];
		
		for(int i = 0; i < messageLength; i++) {
			byteArray[i] = (byte)message.charAt(i);
		}
		
		return byteArray;
	}
	
	public void addDataListener(SerialPortDataListener newListener) {
		serialPort.addDataListener(newListener);
		logger.log("Added listener to serial port", Level.DEBUG);
	}
	
	public void removeListener() {
		serialPort.removeDataListener();
	}

}
