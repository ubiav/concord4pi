package concord4pi.data;

import java.util.LinkedList;
import java.util.List;

import org.tinylog.Level;

import concord4pi.logging.LogEngine;
import concord4pi.messaging.NotificationPacket;

public class AlarmArmingLevel implements IAlarmObject {

	private AlarmUser armingUser;
	private AlarmAreaPartition parent;
	private LogEngine logger;
	
	private int armingLevel;
	private int delayFlags;
	private int delayTime;
	
	private final int ALARMEDMASK = 1;
	
	private final int BIT45MASK = 0b11000;
	private final int BIT6MASK = 0b100000;
	private final int BIT7MASK = 0b1000000;
	
	private final int DELAYFLAG_STANDARD = 0b00000;
	private final int DELAYFLAG_EXTENDED = 0b01000;
	private final int DELAYFLAG_TWICEEXTENDED = 0b10000;
	
	private final int DELAYFLAG_EXIT = 0b100000;
	private final int DELAYFLAG_END = 0b100000;

	
	public AlarmArmingLevel(AlarmAreaPartition parent, LogEngine logger) {
		this.parent = parent;
		this.logger = logger;
		updateTrigger(new NotificationPacket(myAddress(), "", NotificationPacket.NPACTION_NEW));

	}
	
	public void setArmingLevel(int armingLevel) {
		this.armingLevel = armingLevel;
		updateTrigger(new NotificationPacket(myAddress(), "" + armingLevel, NotificationPacket.NPACTION_UPDATE));
		updateTrigger(new NotificationPacket(myAddress() + "/text", getArmingLevelText(), NotificationPacket.NPACTION_UPDATE));
		logger.log("Set ArmingLevel to [" + armingLevel + "]", Level.TRACE);
	}
	
	public int getArmingLevel() {
		return armingLevel;
	}
	
	public String getArmingLevelText() {
		return toString();
	}
	
	public AlarmUser getArmingUser() {
		return armingUser;
	}
	
	public void setArmingUserByID(String ID) {
		setArmingUser(parent.getUser(ID));

	}
	
	public void setArmingUser(AlarmUser theUser) {
		this.armingUser = theUser;
		updateTrigger(new NotificationPacket(myAddress() + "/user", this.armingUser.getID(), NotificationPacket.NPACTION_UPDATE));
	}
	
	public void setDelayFlags(int delayFlags) {
		this.delayFlags = delayFlags;
		updateTrigger(new NotificationPacket(myAddress() + "/delayFlags", "" + delayFlags, NotificationPacket.NPACTION_UPDATE));
		updateTrigger(new NotificationPacket(myAddress() + "/delayFlags", getDelayFlagsText(), NotificationPacket.NPACTION_UPDATE));
		logger.log("Set ArmingLevel Delay Flags to [" + delayFlags + "]", Level.TRACE);
	}
	
	public int getDelayFlags() {
		return delayFlags;
	}
	
	public String getDelayFlagsText() {
		List<String> delayFlags = new LinkedList<String>();
		int bit54 = this.delayFlags & BIT45MASK;
		int bit6 = this.delayFlags & BIT6MASK;
		int bit7 = this.delayFlags & BIT7MASK;
		
		
		if((bit54 & DELAYFLAG_STANDARD) == DELAYFLAG_STANDARD) {
			delayFlags.add("STANDARD");
		}
		else if((bit54 & DELAYFLAG_EXTENDED) == DELAYFLAG_EXTENDED) {
			delayFlags.add("EXTENDED");
		}
		else if((bit54 & DELAYFLAG_TWICEEXTENDED) == DELAYFLAG_TWICEEXTENDED) {
			delayFlags.add("TWICE EXTENDED");
		}
		
		if((bit6 & DELAYFLAG_EXIT) == DELAYFLAG_EXIT) {
			delayFlags.add("EXIT DELAY");
		}
		else {
			delayFlags.add("ENTRY DELAY");
		}
		
		if((bit7 & DELAYFLAG_END) == DELAYFLAG_END) {
			delayFlags.add("END DELAY");
		}
		else {
			delayFlags.add("START DELAY");
		}
		return String.join("|", delayFlags);
	}
	
	public void setDelayTime(int delayTime) {
		this.delayTime = delayTime;
		updateTrigger(new NotificationPacket(myAddress() + "/delayTime", getDelayTimeText(), NotificationPacket.NPACTION_UPDATE));
		logger.log("Set ArmingLevel Delay Time to [" + delayFlags + "]", Level.TRACE);
	}
	
	public int getDelayTime() {
		return this.delayTime;
	}
	
	public String getDelayTimeText() {
		return String.valueOf(this.delayTime);
	}
	
	public boolean isArmed() {
		return (this.armingLevel <= ALARMEDMASK);
	}

	@Override
	public String toJSON() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String myAddress() {
		return parent.myAddress() + "/armingLevel";
	}

	@Override
	public String getID() {
		return null;
	}
	
	@Override
	public void updateTrigger(NotificationPacket packet) {
		parent.updateTrigger(packet);
	}
	
	public String toString() {
		if(parent.isArea()) {
			switch(armingLevel) {
			case 0 :
				return "Zone Test";
			case 1 :
				return "Off";
			case 2 :
				return "Home/Perimeter";
			case 3 :
				return "Away/Full";
			case 4 :
				return "Night";
			case 5 :
				return "Silent";
			default :
				return "UNKNOWN";
			}
		}
		else {
			switch(armingLevel) {
			case 1 :
				return "Off";
			case 2 :
				return "Stay";
			case 3 :
				return "Away";
			case 8 :
				return "Phone Test";
			case 9 :
				return "Sensor Test";
			default :
				return "UNKNOWN"; 
			}
		}
	}
}
