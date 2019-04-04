package concord4pi.SB2000;

public class ControlMessage implements IBaseMessage {

	private byte messageCode;
	
	public ControlMessage(byte messageCode) {
		this.messageCode = messageCode;
	}

	public String toString() {
		if(messageCode == SB2000Constants.ACK) {
			return "[ACK]";
		}
		else {
			return "[NAK]";
		}
	}
	
	public byte message() {
		return messageCode;
	}
	
	public boolean isControlMessage() {
		return true;
	}
}
