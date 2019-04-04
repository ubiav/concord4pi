package concord4pi.data;

import java.util.LinkedList;
import java.util.List;

import org.tinylog.Level;

import concord4pi.logging.LogEngine;
import concord4pi.messaging.NotificationPacket;

public class AlarmZone implements IAlarmObject {
	public final int ZONESTATUS_NONE = 0;
	public final int ZONESTATUS_TRIPPED = 1;
	public final int ZONESTATUS_FAULTED = 2;
	public final int ZONESTATUS_ALARM = 4;
	public final int ZONESTATUS_TROUBLE = 8;
	public final int ZONESTATUS_BYPASSED = 16;
	
	private final int UNSET = -1;
	
	private final int maxMessageHistory = 5;
	
	private AlarmAreaPartition parent;
	private LogEngine logger;
	private String ID;
	private int group = UNSET;
	private int type = UNSET;
	private int status = UNSET;
	private List<String> statusText = new LinkedList<String>();
	private List<Integer> keyFobCommands = new LinkedList<Integer>();
	
	public AlarmZone(String ID, AlarmAreaPartition parent, LogEngine logger) {
		this.ID = ID;
		this.parent = parent;
		this.logger = logger;
		updateTrigger(new NotificationPacket(myAddress(), "", NotificationPacket.NPACTION_NEW));
	}
	
	@Override
	public String getID() {
		return ID;
	}
	
	public void updateZoneData(int group, int type, int status, String text)  {
		setGroup(group);
		setType(type);
		setStatus(status);
		setText(text);
		
		logger.log("Received Updated Zone Data for PartitionArea [" + parent.getID() + "] in Group [" + getGroup() + "] " + 
				"for Zone [" + getID() + "] | " + "ZONE TYPE: [" + getType() + "] " + "ZONE STATUS: [" + getStatusText() + "] " + 
				"ZONE TEXT: [" + getText() + "]", Level.DEBUG);
	}
	
	public void setGroup(int group) {
		int updateType;
		if(this.group == UNSET) { updateType = NotificationPacket.NPACTION_NEW; } 
		else { updateType = NotificationPacket.NPACTION_UPDATE; }
		this.group = group;
		updateTrigger(new NotificationPacket(myAddress() + "/group/" + group, "", updateType));
		logger.log("Set Group to [" + group + "] for Zone [" + getID() + "]", Level.TRACE);
	}
	
	public int getGroup() {
		return group;
	}

	public void setType(int type) {
		this.type = type;
		updateTrigger(new NotificationPacket(myAddress() + "/type", "" + type, NotificationPacket.NPACTION_UPDATE));
		updateTrigger(new NotificationPacket(myAddress() + "/type/text", getTypeName(), NotificationPacket.NPACTION_UPDATE));
		logger.log("Set Type to [" + type + "] for Zone [" + getID() + "]", Level.TRACE);
	}
	
	public int getType() {
		return this.type;
	}
	
	public String getTypeName() {
		if(type == 0) { return "Hardwired"; }
		else if(type == 1) { return "RF"; }
		else if(type == 2) { return "RF Touchpad"; }
		else { return "UNKNOWN"; }
	}
	
	public void setStatus(int status) {
		this.status = status;
		updateTrigger(new NotificationPacket(myAddress() + "/status", "" + status, NotificationPacket.NPACTION_UPDATE));
		updateTrigger(new NotificationPacket(myAddress() + "/status/text", getStatusText(), NotificationPacket.NPACTION_UPDATE));
		logger.log("Set Status to [" + getStatusText() + "] for Zone [" + getID() + "]", Level.TRACE);
	}
	
	public int getStatus() {
		return status;
	}
	
	public String getStatusText() {
		List<String> zoneStatusFlags = new LinkedList<String>();
		
		if(status == ZONESTATUS_NONE) {
			zoneStatusFlags.add("NONE");
		}
		else {
			if((status & ZONESTATUS_TRIPPED) == ZONESTATUS_TRIPPED) {
				zoneStatusFlags.add("TRIPPED");
			}
			if((status & ZONESTATUS_FAULTED) == ZONESTATUS_FAULTED) {
				zoneStatusFlags.add("FAULTED");
			}
			if((status & ZONESTATUS_ALARM) == ZONESTATUS_ALARM) {
				zoneStatusFlags.add("ALARM");
			}
			if((status & ZONESTATUS_TROUBLE) == ZONESTATUS_TROUBLE) {
				zoneStatusFlags.add("TROUBLE");
			}
			if((status & ZONESTATUS_BYPASSED) == ZONESTATUS_BYPASSED) {
				zoneStatusFlags.add("BYPASSED");
			}
		}
		
		return String.join("|", zoneStatusFlags);
	}
	
	public void setText(String newStatusText) {
		this.statusText.add(newStatusText);
		
		updateTrigger(new NotificationPacket(myAddress() + "/text", newStatusText, NotificationPacket.NPACTION_UPDATE));
		
		while(statusText.size() > maxMessageHistory) {
			statusText.remove(statusText.get(statusText.size()-1));
		}
		logger.log("Set Text to [" + newStatusText + "] for Zone [" + getID() + "]", Level.TRACE);
	}
	
	
	public String getText() {
		
		return getPastText(0);
	}
	
	public String getPastText(int messagePastIndex) {
		return statusText.get(messagePastIndex);
	}
	
	public int getKeyFobCommand() {
		
		return getPastKeyFobCommand(0);
	}

	public String getKeyFobCommandText() {
		return getKeyFobCommandString(getPastKeyFobCommand(0)); 
	}

	public String getPastKeyFobCommandText(int index) {
		return getKeyFobCommandString(getPastKeyFobCommand(index)); 
	}

	private String getKeyFobCommandString(int keyFobCommand) {
		switch(keyFobCommand) {
		case 0:
			return "DISARM";
		case 1:
			return "ARM";
		case 2:
			return "LIGHTS";
		case 3:
			return "STAR";
		case 4:
			return "ARM & DISARM";
		case 5:
			return "LIGHTS & STAR";
		case 6:
			return "LONG LIGHTS";
		case 9:
			return "ARM & STAR";
		case 10:
			return "DISARM & LIGHTS";
		default:
			return "NONE";
		}
	}
	
	public int getPastKeyFobCommand(int index) {
		return keyFobCommands.get(index);
	}
	
	public void setKeyFobCommand(int newKeyFobCommand) {
		keyFobCommands.add(newKeyFobCommand);
		
		updateTrigger(new NotificationPacket(myAddress() + "/keyFob/command", "" + newKeyFobCommand, NotificationPacket.NPACTION_UPDATE));
		updateTrigger(new NotificationPacket(myAddress() + "/keyFob/command/text", getKeyFobCommandText(), NotificationPacket.NPACTION_UPDATE));
		
		while(keyFobCommands.size() > maxMessageHistory) {
			keyFobCommands.remove(keyFobCommands.get(keyFobCommands.size()-1));
		}
		logger.log("Set new KeyFob Command to [" + newKeyFobCommand + "] for Zone [" + getID() + "]", Level.TRACE);
	}


	@Override
	public String toJSON() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String myAddress() {
		return parent.myAddress() + "/zones/" + ID;
	}
	
	@Override
	public void updateTrigger(NotificationPacket packet) {
		parent.updateTrigger(packet);
	}


}
