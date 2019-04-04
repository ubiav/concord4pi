package concord4pi.SB2000;

import java.util.Arrays;

public class IOMessage implements IBaseMessage {
	
	private final byte[] MULTIBYTECOMMAND_PREFIX = new byte[] { 0x22, 0x23 };	
	private final int MODULUS = 256;
	private final int maxReTransmit = 5;
	
	protected byte[] commandCode;
	protected byte[] data;
	protected byte checksum;
	protected byte messageLength;
	private int transmissionCount = 0;
	
	public IOMessage(byte[] rawMessage) {
		initMessage(rawMessage);
	}
	
	public boolean isControlMessage() {
		return false;
	}
	
	public void incrementTransmissionCount() {
		transmissionCount++;
	}
	
	public boolean isReTransmittable() {
		return transmissionCount <= maxReTransmit;
	}
	
	private void initMessage(byte[] rawMessage) {
		int commandStart = 1;
		int dataStart;
		
		byte[] encodedMessage = encodeRawMessage(rawMessage);
		
		if(encodedMessage.length > 0) {
			messageLength = encodedMessage[0];
	
			if(messageLength == (encodedMessage.length - 1)) {
				//length of the message is valid.
				checksum = encodedMessage[messageLength];
		
				if(contains(MULTIBYTECOMMAND_PREFIX, encodedMessage[1])) {
					this.commandCode = Arrays.copyOfRange(encodedMessage, commandStart, commandStart + 2);
					dataStart = commandStart + 2;
				}
				else {
					this.commandCode = Arrays.copyOfRange(encodedMessage, commandStart, commandStart + 1);
					dataStart = commandStart + 1;
				}
				
				if(dataStart < this.messageLength) {
					this.data = Arrays.copyOfRange(encodedMessage, dataStart, this.messageLength);
				}
				else {
					//there might not be any data payload
					this.data = new byte[0];
				}
			}
			else {
				invalidateMessage();
			}
		}
		else {
			invalidateMessage();
		}
	}

	public String toString() {
		return convertEncodedByteArrayToHexString(getEncodedMessage());
	}
	
	public byte[] getEncodedMessage() {
		byte[] encodedMessage = new byte[commandCode.length + data.length + 2];
		int arrayIndex = 0;
		
		encodedMessage[arrayIndex++] = messageLength;
		
		for(int i=0; i < commandCode.length; i++) {
			encodedMessage[arrayIndex++] = commandCode[i];
		}
		
		for(int i=0; i < data.length; i++) {
			encodedMessage[arrayIndex++] = data[i];
		}
		
		encodedMessage[arrayIndex++] = checksum;
		
		return encodedMessage;
	}
	
	private byte[] getEncodedMessageWithoutCheckSum() {
		byte[] encodedMessage = new byte[commandCode.length + data.length + 1];
		int arrayIndex = 0;
		
		encodedMessage[arrayIndex++] = messageLength;
		
		for(int i=0; i < commandCode.length; i++) {
			encodedMessage[arrayIndex++] = commandCode[i];
		}
		
		for(int i=0; i < data.length; i++) {
			encodedMessage[arrayIndex++] = data[i];
		}
		
		return encodedMessage;
	}
	
	public byte[] getMessageBytes() {
		return null;
	}
	
	public boolean isValid() {
		return ((getCalcMessageLength() == getMessageLength()) && (getCalcCheckSum() == getMessageCheckSum()));
	}
	
	public byte getMessageLength() {
		return messageLength;
	}
	
	public String getMessageLengthString() {
		return convertByteToStringHex(messageLength);
	}
	
	public int getCalcMessageLength() {
		return getEncodedMessageWithoutCheckSum().length;
	}
	
	public byte[] getCommandCode() {
		return commandCode;
	}
	
	public String getCommandCodeString() {
		return convertEncodedByteArrayToHexString(commandCode);
	}
	
	public byte[] getData() {
		return data;
	}

	public String getDataString() {
		return convertEncodedByteArrayToHexString(data);
	}
	
	public byte getMessageCheckSum() {
		return checksum;
	}
	
	public byte getCalcCheckSum() {
		return calculateCheckSum();
	}
	
	protected byte calculateCheckSum() {
		int checksum = 0;
		byte[] message = getEncodedMessageWithoutCheckSum();

		for(byte eachByte: message) {
			checksum += eachByte;
		}
		
		return (byte)(checksum % MODULUS);
	}
	
	private byte[] encodeRawMessage(byte[] rawMessage) {
		return convertHexStringToEncodedByteArray(convertRawByteArrayToHexString(rawMessage));
	}
	
	private String convertRawByteArrayToHexString(byte[] byteArray) {
		
		if((byteArray.length % 2) != 0) {
			return null;
		}
		StringBuilder tempMessage =  new StringBuilder();
		for(int i = 0; i < byteArray.length; i+=2) {
			tempMessage.append(new String(new char[] { (char)byteArray[i], (char)byteArray[i+1]}));
		}
		
		return tempMessage.toString();
	}
	
	private String convertEncodedByteArrayToHexString(byte[] byteArray) {
		StringBuilder tempMessage =  new StringBuilder();
		for(byte eachByte : byteArray) {
			tempMessage.append(convertByteToStringHex(eachByte));
		}
		
		return tempMessage.toString();		
	}

	
	private String convertByteToStringHex(byte byteToConvert) {
		return String.format("%02X", byteToConvert);	
	}
	
	private byte convertHexToEncodedBinary(String asciiValue) {
		return (byte)Integer.parseInt(asciiValue, 16);
	}
	
	private byte[] convertHexStringToEncodedByteArray(String message) {
		try {
			String[] stringArray = message.split("(?<=\\G.{2})");
			byte[] binaryArray = new byte[stringArray.length];
	
			for(int i = 0; i < stringArray.length; i++) {
				binaryArray[i] = convertHexToEncodedBinary(stringArray[i]);
			}
	
			return binaryArray;
		}
		catch (Exception e) {
			return new byte[0];
		}
	}
	
	private boolean contains(final byte[] array, final byte key) {
	    for (final byte i : array) {
	        if (i == key) {
	            return true;
	        }
	    }
	    return false;	
	}
	
	private void invalidateMessage() {
		//messageLength is not valid so just invalidate the message;
		this.commandCode = new byte[0];
		this.data = new byte[0];
		this.checksum = -1;		
	}
}
