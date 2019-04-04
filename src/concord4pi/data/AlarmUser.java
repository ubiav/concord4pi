package concord4pi.data;

import org.tinylog.Level;

import concord4pi.logging.LogEngine;
import concord4pi.messaging.NotificationPacket;

public class AlarmUser implements IAlarmObject {

	private String ID;
	private AlarmAreaPartition parent;
	private LogEngine logger;
	
	public AlarmUser(String ID, AlarmAreaPartition parent, LogEngine logger) {
		this.ID = ID;
		this.parent = parent;
		this.logger = logger;
		updateTrigger(new NotificationPacket(myAddress(), "", NotificationPacket.NPACTION_NEW));
		this.logger.log("Created new Alarm User [ " + getID() + "]", Level.TRACE);
	}

	@Override
	public String getID() {
		return ID;
	}
	
	@Override
	public void updateTrigger(NotificationPacket packet) {
		parent.updateTrigger(packet);
	}

	public boolean isFob() {
		int FobCode = Integer.parseInt(ID.substring(0, 2), 16);
		return FobCode == 1;
	}
	
	public String getType() {
		int userNum = Integer.parseInt(ID.substring(2,4), 16);
		
		if(userNum >= 0 && userNum <= 229) {
			return "Regular User";
		}
		else if(userNum >= 230 && userNum <= 237) {
			return "Master Code";
		}
		else if(userNum >= 238 && userNum <= 245) {
			return "Duress Code";
		}
		else {
			switch(userNum) {
				case 246 :
					return "System Master Code";
				case 247 :
					return "Installer Code";
				case 248 :
					return "Dealer Code";
				case 249 :
					return "AVM Code";
				case 250 :
					return "Quick Arm";
				case 251 :
					return "Key Switch Arm";
				case 252 :
					return "System";
				default :
					return "UNKNOWN";
			}
		}
	}
	
	@Override
	public String toJSON() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String myAddress() {
		return parent.myAddress() + "/users/" + ID;
	}


}
