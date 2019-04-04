package concord4pi.data;

import org.tinylog.Level;

import concord4pi.SB2000.SB2000Constants;
import concord4pi.logging.LogEngine;
import concord4pi.messaging.NotificationPacket;

public class AlarmTouchpad implements IAlarmObject {
	private AlarmAreaPartition parent;
	private LogEngine logger;
	
	private String text;
	private boolean textWasBroadcast = false;
	
	public AlarmTouchpad(AlarmAreaPartition parent, LogEngine logger) {
		this.parent = parent;
		this.logger = logger;
		updateTrigger(new NotificationPacket(myAddress(), "", NotificationPacket.NPACTION_NEW));
	}

	public void setText(String text) {
		this.text = text;
		updateTrigger(new NotificationPacket(myAddress() + "/text", text, NotificationPacket.NPACTION_UPDATE));
		logger.log("Set Touchpad Display to [" + text + "]", Level.TRACE);
	}
	
	public void setTextFromTokens(String textTokens) {
		setText(convertTokensToString(textTokens));
	}
	
	public String getText() {
		return text;
	}
	
	private String convertTokensToString(String textTokens) {
		String tokens[] = textTokens.split("(?<=\\G.{2})");
		StringBuilder tokenString = new StringBuilder();

		for (int i = 0; i < tokens.length; i++) {
			tokenString.append(SB2000Constants.convertTextToken(tokens[i]));
		}
		
		return tokenString.toString();
	}
	
	public void setBroadcastFlag(boolean isBroadcast) {
		this.textWasBroadcast = isBroadcast;
		updateTrigger(new NotificationPacket(myAddress() + "/broadcastFlag", getBroadcastFlagText(), NotificationPacket.NPACTION_UPDATE));
		
	}
	
	public boolean getBroadcastFlag() {
		return this.textWasBroadcast;
	}
	
	public String getBroadcastFlagText() {
		if(this.textWasBroadcast) {
			return "BROADCAST";
		}
		else {
			return "NORMAL";
		}
	}
	
	
	@Override
	public String toJSON() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String myAddress() {
		return parent.myAddress() + "/touchpad";
	}

	@Override
	public String getID() {
		return null;
	}
	
	@Override
	public void updateTrigger(NotificationPacket packet) {
		parent.updateTrigger(packet);
	}
}
