package concord4pi.messaging;

public class NotificationPacket {

	public String key;
	public String value;
	public int action;
	
	public final static int NPACTION_NEW = 1;
	public final static int NPACTION_UPDATE = 2;
	public final static int NPACTION_DELETE = 3;
	
	public NotificationPacket() {
	}

	public NotificationPacket(String key, String value, int action) {
		this.key = key;
		this.value = value;
		this.action = action;
	}
	
	public String toString() {
		return key + "|" + value + "|" + action;
	}
}
