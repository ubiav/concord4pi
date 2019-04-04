package concord4pi.SB2000;

public class SB2000Constants {

	public static final byte MESSAGE_START = 0x0a;
	public static final byte ACK = 0x06;
	public static final byte NAK = 0x15;
	
	public static final String COMMANDCODE_PANELTYPE = "01";
	public static final String COMMANDCODE_AUTOMATIONEVENTLOST = "02";
	public static final String COMMANDCODE_SELZONE = "03";
	public static final String COMMANDCODE_SELPARTITION = "04";
	public static final String COMMANDCODE_SELSUPERBUSDEVICE = "05";
	public static final String COMMANDCODE_CADI = "20";
	public static final String COMMANDCODE_ZONESTATUS = "21";
	public static final String COMMANDCODE_ARMINGLEVEL = "2201";
	public static final String COMMANDCODE_ALARMTROUBLE = "2202";
	public static final String COMMANDCODE_ENTRYEXITDELAY = "2203";
	public static final String COMMANDCODE_SIRENSETUP = "2204";
	public static final String COMMANDCODE_SIRENSYNC = "2205";
	public static final String COMMANDCODE_SIRENGO = "2206";
	public static final String COMMANDCODE_TOUCHPADDISPLAY = "2209";
	public static final String COMMANDCODE_SIRENSTOP = "220B";
	public static final String COMMANDCODE_FEATURESTATE = "220C";
	public static final String COMMANDCODE_TEMPERATURE = "220D";
	public static final String COMMANDCODE_TIMEDATE = "220E";
	public static final String COMMANDCODE_LIGHTSSTATE = "2301";
	public static final String COMMANDCODE_USERLIGHTS = "2302";
	public static final String COMMANDCODE_KEYFOB = "2303";
	public static final String COMMANDCODE_KEYPRESS = "40";
	
	public static final int SOURCETYPE_ZONE = 2;

	public static final byte[] FULLCOMMAND_FullEquipmentList = "020204".getBytes();
	public static final byte[] FULLCOMMAND_DynamicDataRefresh = "022022".getBytes();
	
	public static String convertTextToken(String token) {
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
		return tokenName;
	}

}
