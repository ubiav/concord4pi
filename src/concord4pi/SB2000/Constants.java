package concord4pi.SB2000;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import concord4pi.logs.LogEngine;

public class Constants {
	public final static int MESSAGE_START = 0x0a;
	public final static int ACK = 0x06;
	public final static int NAK = 0x15;
	
	public final static int MAX_MESSAGE_SIZE = 60;
	
	public final static int LOOP_MILLISECOND_DELAY = 10;
	
	public final static int ZONESTATUS_NONE = 0;
	public final static int ZONESTATUS_TRIPPED = 1;
	public final static int ZONESTATUS_FAULTED = 2;
	public final static int ZONESTATUS_ALARM = 4;
	public final static int ZONESTATUS_TROUBLE = 8;
	public final static int ZONESTATUS_BYPASSED = 16;
	
	public final static String ZONETYPE_NONE = "00";
	public final static int SOURCETYPE_ZONE = 2;
	
	public static final String FullEquipmentListCommand = "020204";
	public static final String DynamicDataRefreshCommand = "022022";

	public Constants() {
		
	}
	
	public static String textTokenName(String token) {
		String tokenName;
		switch(token) {
			case "00":
				tokenName="0";
				break;
			case "01":
				tokenName="1";
				break;
			case "02":
				tokenName="2";
				break;
			case "03":
				tokenName="3";
				break;
			case "04":
				tokenName="4";
				break;
			case "05":
				tokenName="5";
				break;
			case "06":
				tokenName="6";
				break;
			case "07":
				tokenName="7";
				break;
			case "08":
				tokenName="8";
				break;
			case "09":
				tokenName="9";
				break;
			// No case "a
			// No case "b
			case "0C":
				tokenName="#";
				break;
			case "0D":
				tokenName=":";
				break;
			case "0E":
				tokenName="/";
				break;
			case "0F":
				tokenName="?";
				break;
			case "10":
				tokenName=".";
				break;
			case "11":
				tokenName="A";
				break;
			case "12":
				tokenName="B";
				break;
			case "13":
				tokenName="C";
				break;
			case "14":
				tokenName="D";
				break;
			case "15":
				tokenName="E";
				break;
			case "16":
				tokenName="F";
				break;
			case "17":
				tokenName="G";
				break;
			case "18":
				tokenName="H";
				break;
			case "19":
				tokenName="I";
				break;
			case "1A":
				tokenName="J";
				break;
			case "1B":
				tokenName="K";
				break;
			case "1C":
				tokenName="L";
				break;
			case "1D":
				tokenName="M";
				break;
			case "1E":
				tokenName="N";
				break;
			case "1F":
				tokenName="O";
				break;
			case "20":
				tokenName="P";
				break;
			case "21":
				tokenName="Q";
				break;
			case "22":
				tokenName="R";
				break;
			case "23":
				tokenName="S";
				break;
			case "24":
				tokenName="T";
				break;
			case "25":
				tokenName="U";
				break;
			case "26":
				tokenName="V";
				break;
			case "27":
				tokenName="W";
				break;
			case "28":
				tokenName="X";
				break;
			case "29":
				tokenName="Y";
				break;
			case "2A":
				tokenName="Z";
				break;
			case "2B":
				tokenName=" ";
				break;
			case "2C":
				tokenName = "'";
				break;
			case "2D":
				tokenName="-";
				break;
			case "2E":
				tokenName="_";
				break;
			case "2F":
				tokenName="*";
				break;
			case "30":
				tokenName="AC POWER";
				break;
			case "31":
				tokenName="ACCESS";
				break;
			case "32":
				tokenName="ACCOUNT";
				break;
			case "33":
				tokenName="ALARM";
				break;
			case "34":
				tokenName="ALL";
				break;
			case "35":
				tokenName="ARM";
				break;
			case "36":
				tokenName="ARMING";
				break;
			case "37":
				tokenName="AREA";
				break;
			case "38":
				tokenName="ATTIC";
				break;
			case "39":
				tokenName="AUTO";
				break;
			case "3A":
				tokenName="AUXILIARY";
				break;
			case "3B":
				tokenName="AWAY";
				break;
			case "3C":
				tokenName="BACK";
				break;
			case "3D":
				tokenName="BATTERY";
				break;
			case "3E":
				tokenName="BEDROOM";
				break;
			case "3F":
				tokenName="BEEPS";
				break;
			case "40":
				tokenName="BOTTOM";
				break;
			case "41":
				tokenName="BREEZEWAY";
				break;
			case "42":
				tokenName="BASEMENT";
				break;
			case "43":
				tokenName="BATHROOM";
				break;
			case "44":
				tokenName="BUS";
				break;
			case "45":
				tokenName="BYPASS";
				break;
			case "46":
				tokenName="BYPASSED";
				break;
			case "47":
				tokenName="CABINET";
				break;
			case "48":
				tokenName="CANCELED";
				break;
			case "49":
				tokenName="CARPET";
				break;
			case "4A":
				tokenName="CHIME";
				break;
			case "4B":
				tokenName="CLOSET";
				break;
			case "4C":
				tokenName="CLOSING";
				break;
			case "4D":
				tokenName="CODE";
				break;
			case "4E":
				tokenName="CONTROL";
				break;
			case "4F":
				tokenName="CPU";
				break;
			case "50":
				tokenName="DEGREES";
				break;
			case "51":
				tokenName="DEN";
				break;
			case "52":
				tokenName="DESK";
				break;
			case "53":
				tokenName="DELAY";
				break;
			case "54":
				tokenName="DELETE";
				break;
			case "55":
				tokenName="DINING";
				break;
			case "56":
				tokenName="DIRECT";
				break;
			case "57":
				tokenName="DOOR";
				break;
			case "58":
				tokenName="DOWN";
				break;
			case "59":
				tokenName="DOWNLOAD";
				break;
			case "5A":
				tokenName="DOWNSTAIRS";
				break;
			case "5B":
				tokenName="DRAWER";
				break;
			case "5C":
				tokenName="DISPLAY";
				break;
			case "5D":
				tokenName="DURESS";
				break;
			case "5E":
				tokenName="EAST";
				break;
			case "5F":
				tokenName="ENERGY SAVER";
				break;
			case "60":
				tokenName="ENTER";
				break;
			case "61":
				tokenName="ENTRY";
				break;
			case "62":
				tokenName="ERROR";
				break;
			case "63":
				tokenName="EXIT";
				break;
			case "64":
				tokenName="FAIL";
				break;
			case "65":
				tokenName="FAILURE";
				break;
			case "66":
				tokenName="FAMILY";
				break;
			case "67":
				tokenName="FEATURES";
				break;
			case "68":
				tokenName="FIRE";
				break;
			case "69":
				tokenName="FIRST";
				break;
			case "6A":
				tokenName="FLOOR";
				break;
			case "6B":
				tokenName="FORCE";
				break;
			case "6C":
				tokenName="FORMAT";
				break;
			case "6D":
				tokenName="FREEZE";
				break;
			case "6E":
				tokenName="FRONT";
				break;
			case "6F":
				tokenName="FURNACE";
				break;
			case "70":
				tokenName="GARAGE";
				break;
			case "71":
				tokenName="GALLERY";
				break;
			case "72":
				tokenName="GOODBYE";
				break;
			case "73":
				tokenName="GROUP";
				break;
			case "74":
				tokenName="HALL";
				break;
			case "75":
				tokenName="HEAT";
				break;
			case "76":
				tokenName="HELLO";
				break;
			case "77":
				tokenName="HELP";
				break;
			case "78":
				tokenName="HIGH";
				break;
			case "79":
				tokenName="HOURLY";
				break;
			case "7A":
				tokenName="HOUSE";
				break;
			case "7B":
				tokenName="IMMEDIATE";
				break;
			case "7C":
				tokenName="IN SERVICE";
				break;
			case "7D":
				tokenName="INTERIOR";
				break;
			case "7E":
				tokenName="INTRUSION";
				break;
			case "7F":
				tokenName="INVALID";
				break;
			case "80":
				tokenName="IS";
				break;
			case "81":
				tokenName="KEY";
				break;
			case "82":
				tokenName="KITCHEN";
				break;
			case "83":
				tokenName="LAUNDRY";
				break;
			case "84":
				tokenName="LEARN";
				break;
			case "85":
				tokenName="LEFT";
				break;
			case "86":
				tokenName="LIBRARY";
				break;
			case "87":
				tokenName="LEVEL";
				break;
			case "88":
				tokenName="LIGHT";
				break;
			case "89":
				tokenName="LIGHTS";
				break;
			case "8A":
				tokenName="LVING";
				break;
			case "8B":
				tokenName="LOW";
				break;
			case "8C":
				tokenName="MAIN";
				break;
			case "8D":
				tokenName="MASTER";
				break;
			case "8E":
				tokenName="MEDICAL";
				break;
			case "8F":
				tokenName="MEMORY";
				break;
			case "90":
				tokenName="MIN";
				break;
			case "91":
				tokenName="MODE";
				break;
			case "92":
				tokenName="MOTION";
				break;
			case "93":
				tokenName="NIGHT";
				break;
			case "94":
				tokenName="NORTH";
				break;
			case "95":
				tokenName="NOT";
				break;
			case "96":
				tokenName="NUMBER";
				break;
			case "97":
				tokenName="OFF";
				break;
			case "98":
				tokenName="OFFICE";
				break;
			case "99":
				tokenName="OK";
				break;
			case "9A":
				tokenName="ON";
				break;
			case "9B":
				tokenName="OPEN";
				break;
			case "9C":
				tokenName="OPENING";
				break;
			case "9D":
				tokenName="PANIC";
				break;
			case "9E":
				tokenName="PARTITION";
				break;
			case "9F":
				tokenName="PATIO";
				break;
			case "A0":
				tokenName="PHONE";
				break;
			case "A1":
				tokenName="POLICE";
				break;
			case "A2":
				tokenName="POOL";
				break;
			case "A3":
				tokenName="PORCH";
				break;
			case "A4":
				tokenName="PRESS";
				break;
			case "A5":
				tokenName="QUIET";
				break;
			case "A6":
				tokenName="QUICK";
				break;
			case "A7":
				tokenName="RECEIVER";
				break;
			case "A8":
				tokenName="REAR";
				break;
			case "A9":
				tokenName="REPORT";
				break;
			case "AA":
				tokenName="REMOTE";
				break;
			case "AB":
				tokenName="RESTORE";
				break;
			case "AC":
				tokenName="RIGHT";
				break;
			case "AD":
				tokenName="ROOM";
				break;
			case "AE":
				tokenName="SCHEDULE";
				break;
			case "AF":
				tokenName="SCRIPT";
				break;
			case "B0":
				tokenName="SEC";
				break;
			case "B1":
				tokenName="SECOND";
				break;
			case "B2":
				tokenName="SET";
				break;
			case "B3":
				tokenName="SENSOR";
				break;
			case "B4":
				tokenName="SHOCK";
				break;
			case "B5":
				tokenName="SIDE";
				break;
			case "B6":
				tokenName="SIREN";
				break;
			case "B7":
				tokenName="SLIDING";
				break;
			case "B8":
				tokenName="SMOKE";
				break;
			case "B9":
				tokenName="Sn";
				break;
			case "BA":
				tokenName="SOUND";
				break;
			case "BB":
				tokenName="SOUTH";
				break;
			case "BC":
				tokenName="SPECIAL";
				break;
			case "BD":
				tokenName="STAIRS";
				break;
			case "BE":
				tokenName="START";
				break;
			case "BF":
				tokenName="STATUS";
				break;
			case "C0":
				tokenName="STAY";
				break;
			case "C1":
				tokenName="STOP";
				break;
			case "C2":
				tokenName="SUPERVISORY";
				break;
			case "C3":
				tokenName="SYSTEM";
				break;
			case "C4":
				tokenName="TAMPER";
				break;
			case "C5":
				tokenName="TEMPERATURE";
				break;
			case "C6":
				tokenName="TEMPORARY";
				break;
			case "C7":
				tokenName="TEST";
				break;
			case "C8":
				tokenName="TIME";
				break;
			case "C9":
				tokenName="TIMEOUT";
				break;
			case "CA":
				tokenName="TOUCHPAD";
				break;
			case "CB":
				tokenName="TRIP";
				break;
			case "CC":
				tokenName="TROUBLE";
				break;
			case "CD":
				tokenName="UNBYPASS";
				break;
			case "CE":
				tokenName="UNIT";
				break;
			case "CF":
				tokenName="UP";
				break;
			case "D0":
				tokenName="VERIFY";
				break;
			case "D1":
				tokenName="VIOLATION";
				break;
			case "D2":
				tokenName="WARNING";
				break;
			case "D3":
				tokenName="WEST";
				break;
			case "D4":
				tokenName="WINDOW";
				break;
			case "D5":
				tokenName="MENU";
				break;
			case "D6":
				tokenName="RETURN";
				break;
			case "D7":
				tokenName="POUND";
				break;
			case "D8":
				tokenName="HOME";
				break;
			case "f9":
				tokenName="\n";
				break;
			case "fa":
				tokenName="<spc>";
				break; 
			case "fb":
				tokenName="\n";
				break;
			case "fd":
				tokenName="<bs>";
				break; 
			case "fe":
				tokenName="<blink>";
				break;
			default:
				tokenName="";
				break;
		}
		LogEngine.Log(Level.FINEST, "Converted token [" + token + "] to [" + tokenName + "]", Constants.class.getName());
		return tokenName;
	}
	
	public static String messageTypeName(String messageType) {
		String messageTypeName;
		switch(messageType) {
			case "00" :
				messageTypeName = "NORMAL";
				break;
			case "01" :
				messageTypeName = "BROADCAST";
				break;
			default:
				messageTypeName = "UNDEFINED";
		}
		return messageTypeName;
	}

	public static String zoneStatusFlags(int zoneStatus) {
		StringBuilder zoneStatusFlags = new StringBuilder();

		if(zoneStatus  == ZONESTATUS_NONE) {
			zoneStatusFlags.append("NONE");
		}
		else {
			if((zoneStatus & ZONESTATUS_TRIPPED) == ZONESTATUS_TRIPPED) {
				if(zoneStatusFlags.length() == 0) { zoneStatusFlags.append("TRIPPED"); }
				else {zoneStatusFlags.append("|TRIPPED");}
			}
			if((zoneStatus & ZONESTATUS_FAULTED) == ZONESTATUS_FAULTED) {
				if(zoneStatusFlags.length() == 0) { zoneStatusFlags.append("FAULTED"); }
				else {zoneStatusFlags.append("|FAULTED");}
			}
			if((zoneStatus & ZONESTATUS_ALARM) == ZONESTATUS_ALARM) {
				if(zoneStatusFlags.length() == 0) { zoneStatusFlags.append("ALARM"); }
				else {zoneStatusFlags.append("|ALARM");}
			}
			if((zoneStatus & ZONESTATUS_TROUBLE) == ZONESTATUS_TROUBLE) {
				if(zoneStatusFlags.length() == 0) { zoneStatusFlags.append("TROUBLE"); }
				else {zoneStatusFlags.append("|TROUBLE");}
			}
			if((zoneStatus & ZONESTATUS_BYPASSED) == ZONESTATUS_BYPASSED) {
				if(zoneStatusFlags.length() == 0) { zoneStatusFlags.append("BYPASSED"); }
				else {zoneStatusFlags.append("|BYPASSED");}
			}
		}
		
		return zoneStatusFlags.toString();
	}
	
	public static String panelTypeName(String panelType) {
		String panelTypeName;
		switch(panelType) {
			case "14" :
				panelTypeName = "Concord";
				break;
			case "0B" :
				panelTypeName = "Express";
				break;
			case "1E" :
				panelTypeName = "Express 4";
				break;
			case "0E" :
				panelTypeName = "Euro";
				break;
			default :
				panelTypeName = "UNKNOWN";
				break;
		}
		return panelTypeName;
	}

	public static String armingLevelName(int armingLevel) {
		String armingLevelName;
		switch(armingLevel) {
			case 0 :
				armingLevelName = "Zone Test";
				break;
			case 1 :
				armingLevelName = "Off";
				break;
			case 2 :
				armingLevelName = "Home/Perimeter";
				break;
			case 3 :
				armingLevelName = "Away/Full";
				break;
			case 4 :
				armingLevelName = "Night";
				break;
			case 5 :
				armingLevelName = "Silent";
				break;
			default :
				armingLevelName = "UNKNOWN";
				break;
		}
		return armingLevelName;
	}

	public static String userNumberName(String userNumber) {
		int userNum = Integer.parseInt(userNumber, 16);
		String userNumberName;
		
		if(userNum >= 0 && userNum <= 229) {
			userNumberName = "Regular User";
		}
		else if(userNum >= 230 && userNum <= 237) {
			userNumberName = "Partition in Master Code";
		}
		else if(userNum >= 238 && userNum <= 245) {
			userNumberName = "Partition in Duress Code";
		}
		else {
			switch(userNum) {
				case 246 :
					userNumberName = "System Master Code";
					break;
				case 247 :
					userNumberName = "Installer Code";
					break;
				case 248 :
					userNumberName = "Dealer Code";
					break;
				case 249 :
					userNumberName = "AVM Code";
					break;
				case 250 :
					userNumberName = "Quick Arm";
					break;
				case 251 :
					userNumberName = "Key Switch Arm";
					break;
				case 252 :
					userNumberName = "System";
					break;
				default :
					userNumberName = "UNKNOWN";
					break;

			}
		}
		return userNumberName;
	}

	public static String userTypeName(int userType) {
		if(userType == 1) {
			return "KEYFOB";
		}
		else {
			return "NOT KEYFOB"; 
					
		}
	}

	public static String exitEntryDelayFlags(int delayFlags) {
		int bit54 = delayFlags >>> 3;
		int bit6 = delayFlags >>> 5;
		int bit7 = delayFlags >>> 6;
		
		StringBuilder exitEntryDelayFlags = new StringBuilder();
		
		if((bit54 & 3) == 0) {
			exitEntryDelayFlags.append("STANDARD");
		}
		else if((bit54 & 3) == 1) {
			exitEntryDelayFlags.append("EXTENDED");
		}
		else if((bit54 & 3) == 2) {
			exitEntryDelayFlags.append("TWICE EXTENDED");
		}
		
		if(bit6 == 1) {
			if(exitEntryDelayFlags.length() <= 0) {
				exitEntryDelayFlags.append("EXIT DELAY");
			}
			else {
				exitEntryDelayFlags.append("|EXIT DELAY");
			}
		}
		else {
			if(exitEntryDelayFlags.length() <= 0) {
				exitEntryDelayFlags.append("ENTRY DELAY");
			}
			else {
				exitEntryDelayFlags.append("|ENTRY DELAY");
			}
		}
		
		if(bit7 == 1) {
			if(exitEntryDelayFlags.length() <= 0) {
				exitEntryDelayFlags.append("END DELAY");
			}
			else {
				exitEntryDelayFlags.append("|END DELAY");
			}
		}
		else {
			if(exitEntryDelayFlags.length() <= 0) {
				exitEntryDelayFlags.append("START DELAY");
			}
			else {
				exitEntryDelayFlags.append("|START DELAY");
			}
		}

		return exitEntryDelayFlags.toString();
	}
	
	public static String getSourceTypeName(int sourceType) {
		String getSourceTypeName;
		switch(sourceType) {
			case 0 :
				getSourceTypeName = "Bus Device";
				break;
			case 1 :
				getSourceTypeName = "Local Phone";
				break;
			case 2 :
				getSourceTypeName = "Zone";
				break;
			case 3 :
				getSourceTypeName = "System";
				break;
			case 4 :
				getSourceTypeName = "Remote Phone";
				break;
			default :
				getSourceTypeName = "UNKNOWN";
				break;
		}
		
		return getSourceTypeName;
	}
	
	public static Map<Integer, String> getGeneralType() {
		Map<Integer, String> newMap = new HashMap<Integer, String>();
		newMap.put(1 , "Alarm");
		newMap.put(2 , "Alarm Cancel");
		newMap.put(3 , "Alarm Restoral");
		newMap.put(4 , "Fire Trouble");
		newMap.put(5 , "Fire Trouble Restoral");
		newMap.put(6 , "Non-Fire Trouble");
		newMap.put(7 , "Non-Fire Trouble Restoral");
		newMap.put(8 , "Bypass");
		newMap.put(9 , "Unbypass");
		newMap.put(10, "Opening");
		newMap.put(11, "Closing");
		newMap.put(12, "Partition Configuration Change");
		newMap.put(13, "Partition Event");
		newMap.put(14, "Partition Test");
		newMap.put(15, "System Trouble");
		newMap.put(16, "System Trouble Restoral");
		newMap.put(17, "System Configuration Change");
		newMap.put(18, "System Event");        
		return Collections.unmodifiableMap(newMap);
	}
	
	public static Map<Integer, String> getSpecificType(int generalType) {
		Map<Integer, String> newMap = new HashMap<Integer, String>();
	
		switch(generalType) {
			case 1 :
			case 2 :
			case 3 :
				newMap.put(0, "Unspecified");
				newMap.put(1, "Fire");
				newMap.put(2, "Fire Panic");
				newMap.put(3, "Police");
				newMap.put(4, "Police Panic");
				newMap.put(5, "Medical");
				newMap.put(6, "Medical Panic");
				newMap.put(7, "Auxiliary");
				newMap.put(8, "Auxiliary Panic");
				newMap.put(9, "Tamper");
				newMap.put(10, "No Activity");
				newMap.put(11, "Suspicion");
				newMap.put(12, "Not used");
				newMap.put(13, "Low Temperature");
				newMap.put(14, "High Temperature");
				newMap.put(15, "Keystroke Violation (Touchpad Tamper)");
				newMap.put(16, "Duress");
				newMap.put(17, "Exit Fault");
				newMap.put(18, "Explosive Gas");
				newMap.put(19, "Carbon Monoxide");
				newMap.put(20, "Environmental");
				newMap.put(21, "Latchkey");
				newMap.put(22, "Equipment Tamper");
				newMap.put(23, "Holdup");
				newMap.put(24, "Sprinkler");
				newMap.put(25, "Heat");
				newMap.put(26, "Siren Tamper");
				newMap.put(27, "Smoke");
				newMap.put(28, "Repeater Tamper");
				newMap.put(29, "Fire Pump Activated");
				newMap.put(30, "Fire Pump Failure");
				newMap.put(31, "Fire Gate Valve");
				newMap.put(32, "Low CO2 Pressure");
				newMap.put(33, "Low Liquid Pressure");
				newMap.put(34, "Low Liquid Level");
				newMap.put(35, "Entry/Exit");
				newMap.put(36, "Perimeter");
				newMap.put(37, "Interior");
				newMap.put(38, "Near (Two Trip, Concord only)");
				newMap.put(39, "Water Alarm");
				break;
			case 4 :
			case 5 :
			case 6 :
			case 7 :
				newMap.put(0, "Unspecified");
				newMap.put(1, "Hardwire");
				newMap.put(2, "Ground Fault");
				newMap.put(3, "Device");
				newMap.put(4, "Supervisory");
				newMap.put(5, "Low Battery");
				newMap.put(6, "Tamper");
				newMap.put(7, "SAM");
				newMap.put(8, "Partial Obscurity");
				newMap.put(9, "Jam");
				newMap.put(10, "Zone AC Fail");
				newMap.put(11, "n/u");
				newMap.put(12, "NAC Trouble");
				newMap.put(13, "Analog Zone Trouble");
				newMap.put(14, "Fire Supervisory");
				newMap.put(15, "Pump Fail");
				newMap.put(16, "Fire Gate Valve Closed");
				newMap.put(17, "CO2 Pressure Trouble");
				newMap.put(18, "Liquid Pressure Trouble");
				newMap.put(19, "Liquid Level Trouble");
				break;
			case 8 :
				newMap.put(0, "Direct Bypass");
				newMap.put(1, "Indirect Bypass");
				newMap.put(2, "Swinger Bypass");
				newMap.put(3, "Inhibit");
				break;
			case 9 :
				newMap.put(0, "Normal Open");
				newMap.put(1, "Early Open");
				newMap.put(2, "Late Open");
				newMap.put(3, "Fail To Open");
				newMap.put(4, "Open Exception");
				newMap.put(5, "Open Extension");
				newMap.put(6, "Open Using Keyfob/Keyswitch");
				newMap.put(7, "Scheduled Open");
				newMap.put(8, "Remote Open");
				break;
			case 10 :
				newMap.put(0, "Normal Close");
				newMap.put(1, "Early Close");
				newMap.put(2, "Late Close");
				newMap.put(3, "Fail To Close");
				newMap.put(4, "Close Exception");
				newMap.put(5, "Close Extension");
				newMap.put(6, "Close Using Keyfob/Keyswitch");
				newMap.put(7, "Scheduled Close");
				newMap.put(8, "Remote Close");
				newMap.put(9, "Recent Close (Concord only)");
				break;
			case 11 :
				newMap.put(0, "Normal Close");
				newMap.put(1, "Early Close");
				newMap.put(2, "Late Close");
				newMap.put(3, "Fail To Close");
				newMap.put(4, "Close Exception");
				newMap.put(5, "Close Extension");
				newMap.put(6, "Close Using Keyfob/Keyswitch");
				newMap.put(7, "Scheduled Close");
				newMap.put(8, "Remote Close");
				newMap.put(9, "Recent Close (Concord only)");
			case 12:
				newMap.put(0, "User Access Code Added");
				newMap.put(1, "User Access Code Deleted");
				newMap.put(2, "User Access Code Changed");
				newMap.put(3, "User Access Code Expired");
				newMap.put(4, "User Code Authority Changed");
				newMap.put(5, "Authority Levels Changed");
				newMap.put(6, "Schedule Changed");
				newMap.put(7, "Arming or O/C Schedule Changed");
				newMap.put(8, "Zone Added");
				newMap.put(9, "Zone Deleted");
				break;
			case 13:
				newMap.put(0, "Schedule On");
				newMap.put(1, "Schedule Off");
				newMap.put(2, "Latchkey On");
				newMap.put(3, "Latchkey Off");
				newMap.put(4, "Smoke Detectors Reset");
				newMap.put(5, "Valid User Access Code Entered");
				newMap.put(6, "Arming Level Changed");
				newMap.put(7, "Alarm Reported");
				newMap.put(8, "Agent Release");
				newMap.put(9, "Agent Release Restoral");
				newMap.put(10, "Partition Remote Access");
				newMap.put(11, "Keystroke Violation in Partition");
				newMap.put(12, "Manual Force Arm");
				newMap.put(13, "Auto Force Arm");
				newMap.put(14, "Auto Force Arm Failed");
				newMap.put(15, "Arming Protest Begun");
				newMap.put(16, "Arming Protest Ended");
				break;
			case 14:
				newMap.put(0, "Manual Phone Test");
				newMap.put(1, "Auto Phone Test");
				newMap.put(2, "Auto Phone Test with existing trouble");
				newMap.put(3, "Phone Test OK");
				newMap.put(4, "Phone Test Failed");
				newMap.put(5, "User Sensor Test Started");
				newMap.put(6, "User Sensor Test Ended");
				newMap.put(7, "User Sensor Test Completed");
				newMap.put(8, "User Sensor Test Incomplete");
				newMap.put(9, "user Sensor Test Trip");
				newMap.put(10, "Installer Sensor Test Started");
				newMap.put(11, "Installer Sensor Test Ended");
				newMap.put(12, "Installer Sensor Test Completed");
				newMap.put(13, "Installer Sensor Test Incomplete");
				newMap.put(14, "Installer Sensor Test Trip");
				newMap.put(15, "Fire Drill Started");
				break;
			case 15 :
			case 16 :
				newMap.put(0, "Bus Receiver Failure");
				newMap.put(1, "Bus Antenna Tamper");
				newMap.put(2, "Main Low Battery");
				newMap.put(3, "SnapCard Low Battery");
				newMap.put(4, "Module Low Battery");
				newMap.put(5, "Main AC Failure");
				newMap.put(6, "SnapCard AC Failure");
				newMap.put(7, "Module AC Failure");
				newMap.put(8, "Aux. Power Failure");
				newMap.put(9, "Bus Shutdown");
				newMap.put(10, "Bus Low Power Mode");
				newMap.put(11, "Phone Line 1 Failure");
				newMap.put(12, "Phone Line 2 Failure");
				newMap.put(13, "Remote Phone Tamper");
				newMap.put(14, "Watchdog Reset");
				newMap.put(15, "RAM Failure");
				newMap.put(16, "Flash Failure");
				newMap.put(17, "Printer Error");
				newMap.put(18, "History Buffer (almost) Full");
				newMap.put(19, "History Buffer Overflow");
				newMap.put(20, "Report Buffer Overflor");
				newMap.put(21, "Bus Device Failure");
				newMap.put(22, "Failure To Communicate");
				newMap.put(23, "Long Range Radio Trouble");
				newMap.put(24, "Module Tamper Trouble");
				newMap.put(25, "Un-enrolled Modulte Trouble");
				newMap.put(26, "Audio Output Trouble");
				newMap.put(27, "Analog Module Trouble");
				newMap.put(28, "Cell Module Trouble");
				newMap.put(29, "Buddy 1 Failure");
				newMap.put(30, "Buddy 2 Failure");
				newMap.put(31, "Buddy 3 Failure");
				newMap.put(32, "Buddy 4 Failure");
				newMap.put(33, "SnapCard Trouble");
				newMap.put(34, "Analog Loop Short");
				newMap.put(35, "Analog Loop Break");
				newMap.put(36, "Analog Address 0");
				newMap.put(37, "Un-enrolled Analog Head");
				newMap.put(39, "Duplicate Analog Head");
				newMap.put(40, "Microphone Switch Trouble");
				newMap.put(41, "Microphone Trouble");
				newMap.put(42, "Microhone Wiring Trouble");
				newMap.put(43, "JTECH Premise Paging Trouble");
				newMap.put(44, "Voice Siren Tamper Trouble");
				newMap.put(45, "Microburst Transmit Failure");
				newMap.put(46, "Microbust Transmit Disable");
				newMap.put(47, "Micorburst Module Failure");
				newMap.put(48, "Microburst Not In Service");
				newMap.put(49, "Automation Supervisory Trouble");
				newMap.put(50, "Microburst Module Initializing");
				newMap.put(51, "Printer Paper Out Trouble");
				break;
			case 17 :
				newMap.put(0, "Program Mode Entry");
				newMap.put(1, "Program Mode Exit Without Change");
				newMap.put(2, "Program Mode Exit With Change");
				newMap.put(3, "Downloader Session Start");
				newMap.put(4, "Downloader Session End Without Change");
				newMap.put(5, "Downloader Session End With Change");
				newMap.put(6, "Downloader Error");
				newMap.put(7, "Downloader Connection Denied");
				newMap.put(8, "Date/Time Changed");
				newMap.put(9, "Module Added");
				newMap.put(10, "Module Deleted");
				newMap.put(11, "Speech Tokens Changed");
				newMap.put(12, "Code Changed");
				newMap.put(13, "Panel First Service (cold reset)");
				newMap.put(14, "Panel Back In Service (warm reset)");
				newMap.put(15, "Installer Code Changed");
				break;
			case 18 :
				newMap.put(0, "Callback Requested");
				newMap.put(1, "Output Activity (not used, see 18.5 & 18.6)");
				newMap.put(2, "Buddy Reception");
				newMap.put(3, "Buddy Transmission Request");
				newMap.put(4, "History Buffer Cleared");
				newMap.put(5, "Output On");
				newMap.put(6, "Output Off");
				break;
		}

		return Collections.unmodifiableMap(newMap);
	}
	
	public static String getEventSpecificName(int generalType, int specificType) {
		String getEventSpecificName;
		switch(generalType) {
			case 1 :
			case 2 :
			case 3 :
			case 8 :
			case 9 :
			case 10 :
			case 11 :
			case 12 :
			case 14 :
				getEventSpecificName = "User Number";
				break;
			case 13 :
				if(specificType == 0 || 
					specificType == 1 ) {getEventSpecificName = "Schedule Number";}
				
				else if(specificType == 5 || 
						specificType == 6 || 
						specificType == 12 ) {getEventSpecificName = "Schedule Number";}
				else if(specificType == 15 ) {getEventSpecificName = "Arming Level Attempted";}
				else if(specificType == 16 ) {getEventSpecificName = "Current Arming Level";}
				else {getEventSpecificName = "";}
				break;
			case 18 :
				getEventSpecificName = "Output Number";
				break;
			default :
				getEventSpecificName = "";
				
		}
		return getEventSpecificName;
	}
	
}
