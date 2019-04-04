package concord4pi.data;

import concord4pi.messaging.NotificationPacket;

public interface IAlarmObject {
	public String toString();
	public String toJSON();
	public String myAddress();
	public String getID();
	public void updateTrigger(NotificationPacket packet);
}
