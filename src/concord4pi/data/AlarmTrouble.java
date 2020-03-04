package concord4pi.data;

import org.tinylog.Level;

import concord4pi.logging.LogEngine;
import concord4pi.messaging.NotificationPacket;

public class AlarmTrouble implements IAlarmObject {
	private int generalType;
	private int specificType;
	private int sourceType;
	private String sourceNumber;
	private String eventData;
	
	private AlarmAreaPartition parent;
	private LogEngine logger;
	
	public AlarmTrouble(AlarmAreaPartition parent, LogEngine logger, int generalType, int specificType, int sourceType, String sourceNumber, String eventData) {
		this.parent = parent;
		this.logger = logger;
		setGeneralType(generalType);
		setSpecificType(specificType);
		setSourceType(sourceType);
		setSourceNumber(sourceNumber);
		setEventData(eventData);
		updateTrigger(new NotificationPacket(myAddress(), "", NotificationPacket.NPACTION_NEW));
	}
	
	public String getTypeString() {
		return generalType + "." + specificType;
	}
	
	private void setGeneralType(int generalType) {
		this.generalType = generalType;
		updateTrigger(new NotificationPacket(myAddress() + "/general", "" + generalType, NotificationPacket.NPACTION_UPDATE));
		updateTrigger(new NotificationPacket(myAddress() + "/general/text", getGeneralTypeText(), NotificationPacket.NPACTION_UPDATE));
		logger.log("Set General Type [" + getGeneralTypeText() + "] on AlarmTrouble Event in PartitionArea [" + parent.getID() + "]", Level.TRACE);
	}
	
	public int getGeneralType() {
		return generalType;
	}
	
	public String getGeneralTypeText() {
		switch(generalType) {
		case 1: return "Alarm";
		case 2 : return "Alarm Cancel";
		case 3 : return "Alarm Restoral";
		case 4 : return "Fire Trouble";
		case 5 : return "Fire Trouble Restoral";
		case 6 : return "Non-Fire Trouble";
		case 7 : return "Non-Fire Trouble Restoral";
		case 8 : return "Bypass";
		case 9 : return "Unbypass";
		case 10: return "Opening";
		case 11: return "Closing";
		case 12: return "Partition Configuration Change";
		case 13: return "Partition Event";
		case 14: return "Partition Test";
		case 15: return "System Trouble";
		case 16: return "System Trouble Restoral";
		case 17: return "System Configuration Change";
		case 18: return "System Event";  
		default: return "UNKNOWN";
		}
	}
	
	private void setSpecificType(int specificType) {
		this.specificType = specificType;
		updateTrigger(new NotificationPacket(myAddress() + "/specific", "" + specificType, NotificationPacket.NPACTION_UPDATE));
		updateTrigger(new NotificationPacket(myAddress() + "/specific/text", getSpecificTypeText(), NotificationPacket.NPACTION_UPDATE));
		logger.log("Set Specific Type [" + getSpecificTypeText() + "] on AlarmTrouble Event in PartitionArea [" + parent.getID() + "]", Level.TRACE);
	}
	
	public int getSpecificType() {
		return specificType;
	}
	
	public String getSpecificTypeText() {
		switch(generalType) {
		case 1 :
		case 2 :
		case 3 :
			switch(specificType) {
			case 0: return "Unspecified";
			case 1: return "Fire";
			case 2: return "Fire Panic";
			case 3: return "Police";
			case 4: return "Police Panic";
			case 5: return "Medical";
			case 6: return "Medical Panic";
			case 7: return "Auxiliary";
			case 8: return "Auxiliary Panic";
			case 9: return "Tamper";
			case 10: return "No Activity";
			case 11: return "Suspicion";
			case 12: return "Not used";
			case 13: return "Low Temperature";
			case 14: return "High Temperature";
			case 15: return "Keystroke Violation (Touchpad Tamper)";
			case 16: return "Duress";
			case 17: return "Exit Fault";
			case 18: return "Explosive Gas";
			case 19: return "Carbon Monoxide";
			case 20: return "Environmental";
			case 21: return "Latchkey";
			case 22: return "Equipment Tamper";
			case 23: return "Holdup";
			case 24: return "Sprinkler";
			case 25: return "Heat";
			case 26: return "Siren Tamper";
			case 27: return "Smoke";
			case 28: return "Repeater Tamper";
			case 29: return "Fire Pump Activated";
			case 30: return "Fire Pump Failure";
			case 31: return "Fire Gate Valve";
			case 32: return "Low CO2 Pressure";
			case 33: return "Low Liquid Pressure";
			case 34: return "Low Liquid Level";
			case 35: return "Entry/Exit";
			case 36: return "Perimeter";
			case 37: return "Interior";
			case 38: return "Near (Two Trip: return Concord only)";
			case 39: return "Water Alarm";
			default: return "UNKNOWN";
			}
		case 4 :
		case 5 :
		case 6 :
		case 7 :
			switch(specificType) {
			case 0: return "Unspecified";
			case 1: return "Hardwire";
			case 2: return "Ground Fault";
			case 3: return "Device";
			case 4: return "Supervisory";
			case 5: return "Low Battery";
			case 6: return "Tamper";
			case 7: return "SAM";
			case 8: return "Partial Obscurity";
			case 9: return "Jam";
			case 10: return "Zone AC Fail";
			case 11: return "n/u";
			case 12: return "NAC Trouble";
			case 13: return "Analog Zone Trouble";
			case 14: return "Fire Supervisory";
			case 15: return "Pump Fail";
			case 16: return "Fire Gate Valve Closed";
			case 17: return "CO2 Pressure Trouble";
			case 18: return "Liquid Pressure Trouble";
			case 19: return "Liquid Level Trouble";
			default: return "UNKNOWN";
			}
		case 8 :
			switch(specificType) {
			case 0: return "Direct Bypass";
			case 1: return "Indirect Bypass";
			case 2: return "Swinger Bypass";
			case 3: return "Inhibit";
			default: return "UNKNOWN";
			}
		case 9 :
			switch(specificType) {
			case 0: return "Normal Open";
			case 1: return "Early Open";
			case 2: return "Late Open";
			case 3: return "Fail To Open";
			case 4: return "Open Exception";
			case 5: return "Open Extension";
			case 6: return "Open Using Keyfob/Keyswitch";
			case 7: return "Scheduled Open";
			case 8: return "Remote Open";
			default: return "UNKNOWN";
			}
		case 10 :
			switch(specificType) {
			case 0: return "Normal Close";
			case 1: return "Early Close";
			case 2: return "Late Close";
			case 3: return "Fail To Close";
			case 4: return "Close Exception";
			case 5: return "Close Extension";
			case 6: return "Close Using Keyfob/Keyswitch";
			case 7: return "Scheduled Close";
			case 8: return "Remote Close";
			case 9: return "Recent Close (Concord only)";
			default: return "UNKNOWN";
			}
		case 11 :
			switch(specificType) {
			case 0: return "Normal Close";
			case 1: return "Early Close";
			case 2: return "Late Close";
			case 3: return "Fail To Close";
			case 4: return "Close Exception";
			case 5: return "Close Extension";
			case 6: return "Close Using Keyfob/Keyswitch";
			case 7: return "Scheduled Close";
			case 8: return "Remote Close";
			case 9: return "Recent Close (Concord only)";
			default: return "UNKNOWN";
			}
		case 12:
			switch(specificType) {
			case 0: return "User Access Code Added";
			case 1: return "User Access Code Deleted";
			case 2: return "User Access Code Changed";
			case 3: return "User Access Code Expired";
			case 4: return "User Code Authority Changed";
			case 5: return "Authority Levels Changed";
			case 6: return "Schedule Changed";
			case 7: return "Arming or O/C Schedule Changed";
			case 8: return "Zone Added";
			case 9: return "Zone Deleted";
			default: return "UNKNOWN";
			}
		case 13:
			switch(specificType) {
			case 0: return "Schedule On";
			case 1: return "Schedule Off";
			case 2: return "Latchkey On";
			case 3: return "Latchkey Off";
			case 4: return "Smoke Detectors Reset";
			case 5: return "Valid User Access Code Entered";
			case 6: return "Arming Level Changed";
			case 7: return "Alarm Reported";
			case 8: return "Agent Release";
			case 9: return "Agent Release Restoral";
			case 10: return "Partition Remote Access";
			case 11: return "Keystroke Violation in Partition";
			case 12: return "Manual Force Arm";
			case 13: return "Auto Force Arm";
			case 14: return "Auto Force Arm Failed";
			case 15: return "Arming Protest Begun";
			case 16: return "Arming Protest Ended";
			default: return "UNKNOWN";
			}
		case 14:
			switch(specificType) {
			case 0: return "Manual Phone Test";
			case 1: return "Auto Phone Test";
			case 2: return "Auto Phone Test with existing trouble";
			case 3: return "Phone Test OK";
			case 4: return "Phone Test Failed";
			case 5: return "User Sensor Test Started";
			case 6: return "User Sensor Test Ended";
			case 7: return "User Sensor Test Completed";
			case 8: return "User Sensor Test Incomplete";
			case 9: return "user Sensor Test Trip";
			case 10: return "Installer Sensor Test Started";
			case 11: return "Installer Sensor Test Ended";
			case 12: return "Installer Sensor Test Completed";
			case 13: return "Installer Sensor Test Incomplete";
			case 14: return "Installer Sensor Test Trip";
			case 15: return "Fire Drill Started";
			default: return "UNKNOWN";
			}
		case 15 :
		case 16 :
			switch(specificType) {
			case 0: return "Bus Receiver Failure";
			case 1: return "Bus Antenna Tamper";
			case 2: return "Main Low Battery";
			case 3: return "SnapCard Low Battery";
			case 4: return "Module Low Battery";
			case 5: return "Main AC Failure";
			case 6: return "SnapCard AC Failure";
			case 7: return "Module AC Failure";
			case 8: return "Aux. Power Failure";
			case 9: return "Bus Shutdown";
			case 10: return "Bus Low Power Mode";
			case 11: return "Phone Line 1 Failure";
			case 12: return "Phone Line 2 Failure";
			case 13: return "Remote Phone Tamper";
			case 14: return "Watchdog Reset";
			case 15: return "RAM Failure";
			case 16: return "Flash Failure";
			case 17: return "Printer Error";
			case 18: return "History Buffer (almost) Full";
			case 19: return "History Buffer Overflow";
			case 20: return "Report Buffer Overflor";
			case 21: return "Bus Device Failure";
			case 22: return "Failure To Communicate";
			case 23: return "Long Range Radio Trouble";
			case 24: return "Module Tamper Trouble";
			case 25: return "Un-enrolled Modulte Trouble";
			case 26: return "Audio Output Trouble";
			case 27: return "Analog Module Trouble";
			case 28: return "Cell Module Trouble";
			case 29: return "Buddy 1 Failure";
			case 30: return "Buddy 2 Failure";
			case 31: return "Buddy 3 Failure";
			case 32: return "Buddy 4 Failure";
			case 33: return "SnapCard Trouble";
			case 34: return "Analog Loop Short";
			case 35: return "Analog Loop Break";
			case 36: return "Analog Address 0";
			case 37: return "Un-enrolled Analog Head";
			case 39: return "Duplicate Analog Head";
			case 40: return "Microphone Switch Trouble";
			case 41: return "Microphone Trouble";
			case 42: return "Microhone Wiring Trouble";
			case 43: return "JTECH Premise Paging Trouble";
			case 44: return "Voice Siren Tamper Trouble";
			case 45: return "Microburst Transmit Failure";
			case 46: return "Microbust Transmit Disable";
			case 47: return "Micorburst Module Failure";
			case 48: return "Microburst Not In Service";
			case 49: return "Automation Supervisory Trouble";
			case 50: return "Microburst Module Initializing";
			case 51: return "Printer Paper Out Trouble";
			default: return "UNKNOWN";
			}
		case 17 :
			switch(specificType) {
			case 0: return "Program Mode Entry";
			case 1: return "Program Mode Exit Without Change";
			case 2: return "Program Mode Exit With Change";
			case 3: return "Downloader Session Start";
			case 4: return "Downloader Session End Without Change";
			case 5: return "Downloader Session End With Change";
			case 6: return "Downloader Error";
			case 7: return "Downloader Connection Denied";
			case 8: return "Date/Time Changed";
			case 9: return "Module Added";
			case 10: return "Module Deleted";
			case 11: return "Speech Tokens Changed";
			case 12: return "Code Changed";
			case 13: return "Panel First Service (cold reset)";
			case 14: return "Panel Back In Service (warm reset)";
			case 15: return "Installer Code Changed";
			default: return "UNKNOWN";
			}
		case 18 :
			switch(specificType) {
			case 0: return "Callback Requested";
			case 1: return "Output Activity (not used: return see 18.5 & 18.6)";
			case 2: return "Buddy Reception";
			case 3: return "Buddy Transmission Request";
			case 4: return "History Buffer Cleared";
			case 5: return "Output On";
			case 6: return "Output Off";
			default: return "UNKNOWN";
			}
		default: return "UNKNOWN";
		}
	}
	
	private void setEventData(String eventData) {
		this.eventData = eventData;
		updateTrigger(new NotificationPacket(myAddress() + "/eventData", eventData, NotificationPacket.NPACTION_UPDATE));
		logger.log("Set Event Data [" + eventData + "] on AlarmTrouble Event in PartitionArea [" + parent.getID() + "]", Level.TRACE);
	}
	
	public String getEventData() {
		return eventData;
	}
	
	private void setSourceType(int sourceType) {
		this.sourceType = sourceType;
		updateTrigger(new NotificationPacket(myAddress() + "/source", "" + sourceType, NotificationPacket.NPACTION_UPDATE));
		updateTrigger(new NotificationPacket(myAddress() + "/source/text", getSourceTypeText(), NotificationPacket.NPACTION_UPDATE));
		logger.log("Set Source Type [" + getSourceTypeText() + "] on AlarmTrouble Event in PartitionArea [" + parent.getID() + "]", Level.TRACE);
	}
	
	public String getSourceTypeText() {
		switch(sourceType) {
		case 0 :
			return "Bus Device";
		case 1 :
			return "Local Phone";
		case 2 :
			return "Zone";		
		case 3 :
			return "System";
		case 4 :
			return "Remote Phone";
		default :
			return "UNKNOWN";
		}
	}
	
	public int getSourceType() {
		return sourceType;
	}
	
	private void setSourceNumber(String sourceNumber) {
		this.sourceNumber = sourceNumber;
		updateTrigger(new NotificationPacket(myAddress() + "/sourceNumber", sourceNumber, NotificationPacket.NPACTION_UPDATE));
		logger.log("Set Source Number [" + sourceNumber + "] on AlarmTrouble Event in PartitionArea [" + parent.getID() + "]", Level.TRACE);
	}
	
	public String getSourceNumber() {
		return sourceNumber;
	}

	@Override
	public String toJSON() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String myAddress() {
		return parent.myAddress() + "/event";
	}

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateTrigger(NotificationPacket packet) {
		parent.updateTrigger(packet);
	}
}
