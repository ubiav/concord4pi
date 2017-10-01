package concord4pi.SB2000;

import java.util.Arrays;
import java.util.logging.Level;

import concord4pi.logs.LogEngine;

public class Message {
	private final int MULTIBYTECOMMAND_PREFIX[] = new int[] { 0x22, 0x23 };	
	private final int MODULUS = 256;

	private int length = 0;
	private int command[];
	private int data[];
	private int control;
	private int checksum;
	
	private boolean isControlMessage = false;	

	//Constructors
	public Message(int controlChar) {
		isControlMessage = true;
		control = controlChar;
	}
	
	public Message(String asciiMessage)  {
		//the asciiMessage must be the full message from the Automation Module
		//including the length and checksum
		LogEngine.Log(Level.FINER, "Created Message from [" + asciiMessage + "]", this.getClass().getName());
		initMessage(convertASCIIStringToArray(asciiMessage));
	}
	
	public Message(String asciiCommand, String asciiData)  {
		createMessage(convertASCIIStringToArray(asciiCommand),convertASCIIStringToArray(asciiData));
	}
	
	public Message(int binaryMessage[])  {		
		initMessage(binaryMessage);
	}
	
	public Message(int binaryCommand[], int binaryData[])  {
		createMessage(binaryCommand, binaryData);
	}

	private void initMessage(int[] binaryMessage) {
		int currentByte = 0;
		length = binaryMessage[currentByte];
		currentByte++;
		
		if(contains(MULTIBYTECOMMAND_PREFIX, binaryMessage[currentByte])) {
			//this is a 2-byte command
			this.command = Arrays.copyOfRange(binaryMessage, currentByte, currentByte + 2);
			currentByte += 2;
		}
		else {
			//this is a 1-byte command
			this.command = Arrays.copyOfRange(binaryMessage, currentByte, currentByte + 1);
			currentByte++;
			
		}
		
		this.data = new int[length - currentByte];
		if(currentByte < length) {
			this.data = Arrays.copyOfRange(binaryMessage, currentByte, length);
		}
		
		this.checksum = binaryMessage[length];
	}
	
	private void createMessage(int commands[], int data[]) {
		this.length = commands.length + data.length + 1;
		this.command = commands;
		this.data = data;
		this.checksum = calculateChecksum();
	}
	
	private String convertBinaryByteToHex(int byteToConvert) {
		return String.format("%02X", byteToConvert);	
	}
	
	private int convertHexToBinary(String asciiValue) {
		return Integer.parseInt(asciiValue, 16);
	}
	
	private String convertBinaryArrayToHex(int bytesToConvert[]) {
		StringBuilder result = new StringBuilder();
		for(int i = 0; i < bytesToConvert.length; i++) {
			result.append(convertBinaryByteToHex(bytesToConvert[i]));
		}
		return result.toString();
	}
	
	private int[] convertASCIIStringToArray(String asciiString) {
		String[] stringArray = asciiString.split("(?<=\\G.{2})");
		int[] binaryArray = new int[stringArray.length];

		for(int i = 0; i < stringArray.length; i++) {
			binaryArray[i] = convertHexToBinary(stringArray[i]);
		}

		return binaryArray;
	}
	
	private int calculateChecksum() {
		int checksum = 0;
		int messageLength = command.length + data.length + 1;
		int message[] = new int[messageLength];
		int index = 0;
		
		message[index] = messageLength;
		index++;
		
		//get all the command bytes (max 2)
		for(int nextint : command) {
			message[index] = nextint;
			index++;
		}

		//get all data bytes;
		for(int nextint : data) {
			message[index] = nextint;
			index++;
		}
		
		//loop through the entire message and calculate checksum
		for(int nextint : message) {
			checksum += nextint;
		}
		
		return checksum % MODULUS;
		
	}
	
	public String toString() {
		StringBuilder results = new StringBuilder();
		
		results.append(convertBinaryByteToHex(length));
		
		if(!isControlMessage) {
		
			if(command != null) {
				for(int i = 0; i < command.length; i++) {
					results.append(convertBinaryByteToHex(command[i]));
				}
			}
			
			if(data != null) {
				for(int i = 0; i < data.length; i++) {
					results.append(convertBinaryByteToHex(data[i]));
				}
			}
			
			results.append(convertBinaryByteToHex(checksum));
		}
		
		return results.toString();
	}
	
	public boolean validCheckSum() {
		if(!isControlMessage) return this.checksum == this.calculateChecksum();
		else return true;
	}
	
	
	private boolean contains(final int[] array, final int key) {
	    for (final int i : array) {
	        if (i == key) {
	            return true;
	        }
	    }
	    return false;	
	}
	
	public int getLength() {
		return length;
	}
	
	public String getLengthString() {
		return convertBinaryByteToHex(length);
	}
	
	public int[] getCommand() {
		return command;
	}
	
	public String getCommandString() {
		return convertBinaryArrayToHex(command);
	}

	public int[] getData() {
		return data;
	}
	
	public String getDataString() {
		return convertBinaryArrayToHex(data);
	}

	public int getChecksum() {
		return checksum;
	}

	public String getChecksumString() {
		return convertBinaryByteToHex(checksum);
	}
	
	public boolean isControlCommand() {
		return isControlMessage;
	}
	
	public int getControlChar() {
		return control;
	}

}

