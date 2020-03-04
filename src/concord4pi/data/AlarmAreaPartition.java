package concord4pi.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.tinylog.Level;

import concord4pi.logging.LogEngine;
import concord4pi.messaging.NotificationPacket;

public class AlarmAreaPartition implements IAlarmObject {	
	private final int maxMessageHistory = 5;

	private AlarmSystemData parent;
	private LogEngine logger;
	private String ID;
	private byte featureSet;
	
	private AlarmTemperature temperature;
	private AlarmArmingLevel armingLevel;
	private AlarmSiren siren;
	private AlarmTouchpad touchpad;
	private ArrayList<AlarmZone> zones = new ArrayList<AlarmZone>();
	private ArrayList<AlarmUser> users = new ArrayList<AlarmUser>();
	List<String> statusText = new LinkedList<String>();
	List<AlarmTrouble> alarmTroubleEvents = new LinkedList<AlarmTrouble>();

	private final byte featureChime = 0b1;
	private final byte featureEnergySaver = 0b10;
	private final byte featureNoDelay = 0b100;
	private final byte featureLatchKey = 0b1000;
	private final byte featureSilentArming = 0b10000;
	private final byte featureQuickArm = 0b1000000;
	
	public AlarmAreaPartition(String ID, AlarmSystemData parent, LogEngine logger) {
		this.ID = ID;
		this.parent = parent;
		this.logger = logger;
		this.armingLevel = new AlarmArmingLevel(this, logger);
		this.siren = new AlarmSiren(this, logger);
		this.touchpad = new AlarmTouchpad(this, logger);
		this.temperature = new AlarmTemperature(this, logger);
		updateTrigger(new NotificationPacket(myAddress(), "", NotificationPacket.NPACTION_NEW));
		
	}
	
	/**
	 * START ATTRIBUTES GET/SET SECTION
	 * Functions to get/set attributes of the AreaPartition
	 */
	
	public String getID() {
		return ID;
	}
	
	public boolean isArea() {
		String area = ID.substring(2,4);
		return (Integer.parseInt(area) != 0);
	}
	
	public boolean isPartition() {
		String partition = ID.substring(0,2);
		return (Integer.parseInt(partition) != 0);
	}
	
	public void setText(String newStatusText) {
		this.statusText.add(newStatusText);
		
		updateTrigger(new NotificationPacket(myAddress() + "/text", newStatusText, NotificationPacket.NPACTION_UPDATE));
		
		while(statusText.size() > maxMessageHistory) {
			statusText.remove(statusText.get(statusText.size()-1));
		}
		logger.log("Added new StatusText [" + newStatusText + "] for PartitionArea [" + getID() + "]", Level.TRACE);
	}
	
	public String getText() {
		
		return getPastText(0);
	}
	
	public String getPastText(int messagePastIndex) {
		if(messagePastIndex < statusText.size()) {
			return statusText.get(messagePastIndex);
		}
		else {
			return "";
		}
	}
	
	/**
	 * END ATTRIBUTES GET/SET SECTION  
	 */
	
	/**
	 * START ARMINGLEVEL SECTION
	 */
	
	public void setArmingLevel(AlarmArmingLevel newArmingLevel) {
		this.armingLevel = newArmingLevel;
		logger.log("Set ArmingLevel to [" + armingLevel.toString() + "] for PartitionArea [" + getID() + "]", Level.TRACE);
	}
	
	public AlarmArmingLevel getArmingLevel() {
		if(armingLevel == null) {
			armingLevel = new AlarmArmingLevel(this, logger);
		}
		return armingLevel;
	}

	/**
	 * END ARMINGLEVEL SECTION
	 */
	
	/**
	 * START ALARMTROUBLE SECTION
	 */
	
	public AlarmTrouble addAlarmTroubleEvent(int generalType, int specificType, int sourceType, String sourceNumber, String eventData) {
		AlarmTrouble newEvent = new AlarmTrouble(this, logger, generalType, specificType, sourceType, sourceNumber, eventData); 
		alarmTroubleEvents.add(newEvent);
		
		//when the linked list hits its max,remove the oldest.
		//this provides some history but limits the length of the list
		while(alarmTroubleEvents.size() > maxMessageHistory) {
			alarmTroubleEvents.remove(alarmTroubleEvents.get(alarmTroubleEvents.size()-1));
		}
		
		logger.log("Added new Alarm Trouble Event [" + newEvent.getTypeString() + "] with Data [" + newEvent.getEventData() + "]", Level.TRACE);
		return newEvent;
	}
	
	public AlarmTrouble getMostRecentAlarmTroubleEvent() {
		return getPastAlarmTroubleEvent(0);
	}
	
	public AlarmTrouble getPastAlarmTroubleEvent(int eventPastIndex) {
		if(eventPastIndex < alarmTroubleEvents.size()) {
			return alarmTroubleEvents.get(eventPastIndex);
		}
		else {
			return null;
		}
	}
	
	/**
	 * END ARMINGTROUBLE SECTION
	 */
	
	/**
	 * START OVERRIDE FUNCTIONS SECTION
	 */
	
	@Override
	public String toJSON() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String myAddress() {
		return parent.myAddress() + "/areapartition/" + ID;
	}	
	
	@Override
	public void updateTrigger(NotificationPacket packet) {
		parent.updateTrigger(packet);
	}
	
	/**
	 * END OVERRIDE FUNCTIONS SECTION
	 */


	/**
	 * START ALARMZONE FUNCTIONS
	 */
	public AlarmZone getZoneByID(String ID) {
		ListIterator<AlarmZone> zoneList = zones.listIterator();
		
		logger.log("Looking for Zone by ID " + ID, Level.TRACE);
		
		while(zoneList.hasNext()) {
			AlarmZone theZone = zoneList.next();
			if(theZone.getID().equals(ID)) {
				logger.log("Found Zone " + ID, Level.TRACE);
				return theZone;
			}
		}
		
		logger.log("Zone " + ID + " not found!  Adding new zone of ID: " + ID, Level.TRACE);
		return addZoneByID(ID);
	}

	private AlarmZone addZoneByID(String ID) {
		AlarmZone newZone = new AlarmZone(ID, this, logger); 
		zones.add(newZone);
		logger.log("Added new zone [" + newZone.getID() + "]", Level.TRACE);
		return newZone;
	}

	/**
	 * END ALARMZONE FUNCTIONS
	 */

	
	/**
	 * START ALARMUSER FUNCTIONS
	 */

	public AlarmUser getUser(String ID) {
		ListIterator<AlarmUser> userList = users.listIterator();
		
		logger.log("Looking for User by ID " + ID, Level.TRACE);
		
		while(userList.hasNext()) {
			AlarmUser theUser = userList.next();
			if(theUser.getID().equals(ID)) {
				logger.log("Found User " + ID, Level.TRACE);
				return theUser;
			}
		}
		
		logger.log("User " + ID + " not found!  Adding new user of ID: " + ID, Level.TRACE);
		return addUser(ID);
		
	}
	
	private AlarmUser addUser(String ID) {
		AlarmUser newUser = new AlarmUser(ID, this, logger); 
		users.add(newUser);
		logger.log("Added new user [" + newUser.getID() + "]", Level.TRACE);
		return newUser;
		
	}
	
	/**
	 * END ALARMUSER FUNCTIONS
	 */
	
	
	/**
	 * START ALARMSIREN FUNCTIONS
	 */
	
	public AlarmSiren getSiren() {
		if(siren == null) {
			siren = new AlarmSiren(this, logger);
		}
		return siren;
	}
	
	/**
	 * END ALARMSIREN FUNCTIONS
	 */
	
	
	/**
	 * START TOUCHPAD FUNCTIONS
	 */
	
	public AlarmTouchpad getTouchpad() {
		if(touchpad == null) {
			touchpad = new AlarmTouchpad(this, logger);
		}
		return touchpad;
	}
	
	/**
	 * END TOUCHPAD FUNCTIONS
	 */
	
	
	/**
	 * START FEATURESET FUNCTIONS
	 */

	public void setFeatureSet(byte featureSet) {
		
		this.featureSet = featureSet;
		updateTrigger(new NotificationPacket(myAddress() + "/featureSet", "" + featureSet, NotificationPacket.NPACTION_UPDATE));
		updateTrigger(new NotificationPacket(myAddress() + "/featureSet/text", getFeatureSetText(), NotificationPacket.NPACTION_UPDATE));

	}
	
	public byte getFeatureSet() {
		return this.featureSet;
	}
	
	public String getFeatureSetText() {
		List<String> functionSetText = new LinkedList<String>();
		
		if((featureSet & featureChime) == featureChime) {
			functionSetText.add("CHIME");
		}
		else if((featureSet & featureEnergySaver) == featureEnergySaver) {
			functionSetText.add("ENERGY SAVER");
		}
		else if((featureSet & featureNoDelay) == featureNoDelay) {
			functionSetText.add("NO DELAY");
		}
		else if((featureSet & featureLatchKey) == featureLatchKey) {
			functionSetText.add("LATCHKEY");
		}
		else if((featureSet & featureSilentArming) == featureSilentArming) {
			functionSetText.add("SILENT ARMING");
		}
		else if((featureSet & featureQuickArm) == featureQuickArm) {
			functionSetText.add("QUICK ARM");
		}
		
		return String.join("|", functionSetText);
	}
	
	
	/**
	 * END FEATURESET FUNCTIONS
	 */
	
	
	/**
	 * START TEMPERATURE FUNCTIONS
	 */
	
	public AlarmTemperature getTemperature() {
		return this.temperature;
	}
	
	/**
	 * END TEMPERATURE FUNCTIONS
	 */
	
	
}
