package concord4pi.messaging;

import java.util.ArrayList;

import concord4pi.SB2000.IOMessage;

public interface IBroadcaster {
	
	public void sendNotification(NotificationPacket packet);
	public void shutdown();
	public boolean messageWaiting();
	public ArrayList<IOMessage> getMessageList();
}
