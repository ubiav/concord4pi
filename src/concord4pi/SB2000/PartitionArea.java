package concord4pi.SB2000;

import java.util.logging.Level;

import concord4pi.logs.LogEngine;

public class PartitionArea extends AlarmObject {
	private Zone zones;
	private User users;
	private TouchPad touchpads;
	private ArmingLevel armLevel;
	private AlarmTrouble alarmTrouble;
	private Siren siren;
	private int featureState;
	private Temperature temperature;
	
	public PartitionArea(State newParent) {
		super(newParent);
		touchpads = new TouchPad(this);
		LogEngine.Log(Level.FINEST, "Created new Partition/Area", this.getClass().getName());

	}
	
	public PartitionArea(String id, State newParent) {
		super(id, newParent);
		touchpads = new TouchPad(this);
		LogEngine.Log(Level.FINEST, "Created new Partition/Area with ID [" + id + "]", this.getClass().getName());
	}
	

	public void clearAll() {
		zones.clean();
		zones = null;
		
		users.clean();
		users = null;
		
		touchpads.clean();
		touchpads = null;
	}

	/*
	 * 
	 * FEATURESTATE Code
	 * 
	 */
	
	public int getFeatureState() {
		return featureState;
	}
	
	public void setFeatureState(int newFState) {
		featureState = newFState;
		sendMQTTMessage(featureState, "/featureState");
	}
	
	/*
	 * 
	 * END FEATURESTATE Code
	 * 
	 */

	
	
	/*
	 * 
	 * TEMPERATURE Code
	 * 
	 */
	
	public Temperature getTemperature() {
		if(temperature == null) {
			return addTemperature(Constants.TEMPERATURE_UNSET);
		}
		else {
			return temperature;
		}
	}
	
	public Temperature addTemperature(int newTemp) {
		return addTemperature(newTemp, Constants.TEMPERATURE_UNSET, Constants.TEMPERATURE_UNSET);
	}
	
	public Temperature addTemperature(int newTemp, int ESLSP, int ESHSP) {
		temperature = new Temperature(newTemp, ESLSP, ESHSP, this);
		return temperature;
	}

	/*
	 * 
	 * END TEMPERATURE Code
	 * 
	 */
	
	
	
	
	/*
	 * 
	 * SIREN Code
	 * 
	 */
	
	public Siren getSiren() {
		if(siren == null) {
			return addSiren();
		}
		else {
			return siren;
		}
	}
	
	public Siren addSiren() {
		return addSiren(0, "");
	}
	
	public Siren addSiren(int newRepetitionCount, String newCadence) {
		Siren newSiren;
		if(newCadence.equals("")) {
			newSiren = new Siren(this);
		}
		else {
			newSiren = new Siren(newRepetitionCount, newCadence, this);
		}
		
		siren = newSiren;
		return siren;
	}
	
	/*
	 * 
	 * END SIREN Code
	 * 
	 */
	
	/**
	 * 
	 * ARMINGLEVEL Code
	 * 
	 */
	
	public ArmingLevel getArmingLevel() {
		if(armLevel == null) {
			return addArmLevel(ArmingLevel.armingLevel_ZoneTest, null);
		}
		else {
			return armLevel;
		}
	}
	
	public ArmingLevel addArmLevel(int armingLevel, User newUser) {
		ArmingLevel newArmLevel = new ArmingLevel(armingLevel, newUser, this);
		armLevel = newArmLevel;
		return armLevel;
	}
	
	/**
	 * 
	 * END ARMINGLEVEL Code
	 * 
	 */

	/**
	 * 
	 * ALARMTROUBLE Code
	 * 
	 */
	
	public AlarmTrouble addAlarmTrouble(int newSourceType,
			String newSourceNumber,
			int newGeneralType,
			int newSpecificType,
			String newEventSpecificData
		) 
	{
		AlarmTrouble newAlarmTrouble = new AlarmTrouble(newSourceType, 
														newSourceNumber, 
														newGeneralType, 
														newSpecificType, 
														newEventSpecificData, 
														this);
		alarmTrouble = newAlarmTrouble;
		return alarmTrouble;
	}
	
	public AlarmTrouble getAlarmTrouble() {
		return alarmTrouble;
	}
	
	/**
	 * 
	 * END ALARMTROUBLE Code
	 * 
	 */
	
	/**
	 * 
	 * ZONE Code
	 * 
	 */
	
	public Zone getZones() {
		return zones;
	}
	
	public void addZone(Zone newZone) {
		if(zones == null) {
			zones = newZone;
		}
		else {
			zones.add(newZone);
		}
	}

	private Zone addZone(String zoneID) {
		Zone newZone = new Zone(zoneID, this);
		
		if(zones == null) {
			zones = newZone;
		}
		else {
			zones.add(newZone);
		}
		return newZone;
	}
	
	public Zone getZone(String zoneID) {
		if(zones == null) {
			return addZone(zoneID);
		}
		else {
			if(zoneExists(zoneID)) {
				return (Zone)zones.find(zoneID);
			}
			else {
				return addZone(zoneID);
			}
		}
	}
	
	public boolean zoneExists(String zoneID) {
		Zone theZone = (Zone)zones.find(zoneID);
		if(theZone == null) {
			return false;
		}
		else {
			return true;
		}
	}
	
	public void clearZone(String zoneNumber) {
		if(zones.getID() == zoneNumber) {
			//fix up the head node for the linked list
			Zone newFirst = (Zone)zones.getNext();
			Zone currentFirst = zones;
			
			currentFirst.setNext(null);
			currentFirst.setPrevious(null);
			
			zones = newFirst;
			zones.setPrevious(null);
		}
		else {
			zones.clear(zoneNumber);
		}
	}
	
	/**
	 * 
	 * END ZONE Code
	 * 
	 */
	
	/**
	 * 
	 * USER Code
	 * 
	 */

	public User getUsers() {
		return users;
	}
	
	private User addUser(String userNumber) {
		User newUser = new User(userNumber, this);
		if(users == null) {
			users = newUser;
		}
		else {
			users.add(newUser);
		}
		return newUser;
	}
	
	public User getUser(String userID) {
		if(users == null) {
			return addUser(userID);
		}
		else {
			User theUser = (User)users.find(userID);
			if(theUser == null) {
				return addUser(userID);
			}
			else {
				return theUser;
			}			
		}
	}
	
	public void clearUser(String userNumber) {
		if(users.getID() == userNumber) {
			//fix up the head node for the linked list
			User newFirst = (User)users.getNext();
			User currentFirst = users;
			
			currentFirst.setNext(null);
			currentFirst.setPrevious(null);
			
			users = newFirst;
			users.setPrevious(null);
		}
		else {
			users.clear(userNumber);
		}
	}
	
	/**
	 * 
	 * END USER Code
	 * 
	 */
	
	/**
	 * 
	 * TOUCHPAD Code
	 * 
	 */
	
	private TouchPad addTouchPad() {
		TouchPad newTouchPad = new TouchPad(this);
		if(touchpads == null) {
			touchpads = newTouchPad;
		}
		else {
			touchpads.add(newTouchPad);
		}
		return newTouchPad;
	}
	
	public TouchPad getTouchPad() {
		if(touchpads == null) {
			return addTouchPad();
		}
		return touchpads;
	}


	public void clearTouchPad(String userNumber) {
		if(touchpads.getID() == userNumber) {
			//fix up the head node for the linked list
			TouchPad newFirst = (TouchPad)touchpads.getNext();
			TouchPad currentFirst = touchpads;
			
			currentFirst.setNext(null);
			currentFirst.setPrevious(null);
			
			touchpads = newFirst;
			touchpads.setPrevious(null);
		}
		else {
			touchpads.clear(userNumber);
		}
	}
	/**
	 * 
	 * END TOUCHPAD Code
	 * 
	 */
	
	@Override
	public String toString() {
		StringBuilder newString = new StringBuilder();
		newString.append("\"partitionarea" + id + "\":{");
		newString.append("\"featureState\":" + featureState + ",");
		newString.append(super.toString() + ",");
		
		newString.append("\"armingLevel\": {");
		if(armLevel != null) { 
			newString.append(armLevel.toString());
		}
		newString.append("},");

		
		newString.append("\"alarmTrouble\": {");
		if(alarmTrouble != null) { 
			newString.append(alarmTrouble.toString());
		}
		newString.append("},");
		
		newString.append("\"siren\": {");
		if(siren != null) { 
			newString.append(siren.toString());
		}
		newString.append("},");

		newString.append("\"temperature\": {");
		if(temperature != null) { 
			newString.append(temperature.toString());
		}
		newString.append("},");
		

		
		newString.append("\"zones\": {");
			Zone currentZone = zones;
			while(currentZone != null) {
				newString.append(currentZone.toString());
				currentZone = (Zone)currentZone.getNext();
				if(currentZone != null) {
					newString.append(",");
				}
			}
		newString.append("},");

		newString.append("\"users\": {");
			User currentUser = users;
			while(currentUser != null) {
				newString.append(currentUser.toString());
				currentUser = (User)currentUser.getNext();
				if(currentUser != null) {
					newString.append(",");
				}
			}
		newString.append("},");
		
		newString.append("\"touchpads\": {");
			TouchPad currentTP = touchpads;
			while(currentTP != null) {
				newString.append(currentTP.toString());
				currentTP = (TouchPad)currentTP.getNext();
				if(currentTP != null) {
					newString.append(",");
				}
			}
		newString.append("}");
		
		newString.append("}");
		
		return newString.toString();
	}
	
	
	/*
	 * 
	 * Private Classes for the PartitionArea 
	 *
	 */

	public class TouchPad extends AlarmObject {

		public TouchPad(PartitionArea newParent) {
			super(newParent);
			LogEngine.Log(Level.FINEST, "Created new TouchPad", this.getClass().getName());
		}
		
		@Override
		public String toString() {
			StringBuilder newString = new StringBuilder();
			newString.append("\"touchpad\":{");
			newString.append(super.toString());
			newString.append("}");
			return newString.toString();
		}
	}
	
	public class ArmingLevel extends AlarmObject {
		public static final int armingLevel_ZoneTest = 0;
		public static final int armingLevel_Off = 1;	
		public static final int armingLevel_HomePerimeter = 2;	
		public static final int armingLevel_AwayFull = 3;	
		public static final int armingLevel_Night = 4;	
		public static final int armingLevel_Silent = 5;	
		
		private int delayTime;
		private int delayFlags;

		public ArmingLevel(int newarmingLevel, User newUser, PartitionArea newParent) {
			super(newParent);
			setState(Constants.armingLevelName(newarmingLevel), newarmingLevel, "Arming Level Set", newUser);	
		}
		
		public void setArmingLevel(int newarmingLevel, User newUser) {
			setState(Constants.armingLevelName(newarmingLevel), newarmingLevel, "Arming Level Set", newUser);
		}
		
		public int getDelayFlags() {
			return delayFlags;
		}
		
		public void setDelayFlags(int newDelayFlags) {
			delayFlags = newDelayFlags;
			sendMQTTMessage(delayFlags, "/status/arming/delay/flags");
		}
		
		public int getDelayTime() {
			return delayTime;
		}
		
		public void setDelayTime(int newDelayTime) {
			delayTime = newDelayTime;
			sendMQTTMessage(delayTime, "/status/arming/delay/time");
		}

		@Override
		public String toString() {
			StringBuilder newString = new StringBuilder();
			newString.append("\"delayTime\":" + delayTime + ",");
			newString.append("\"delayFlags\":\"" + delayFlags + "\",");
			newString.append(super.toString());
			return newString.toString();
		}
	}
	
	public class AlarmTrouble extends AlarmObject {
		private int sourceType;
		private String sourceNumber;
		private int generalType;
		private int specificType;
		private String eventSpecificData;
		
		public AlarmTrouble(
				int newSourceType,
				String newSourceNumber,
				int newGeneralType,
				int newSpecificType,
				String newEventSpecificData,
				PartitionArea newParent
			) 
		{
			super(newParent);
			setState(sourceNumber, sourceType);
			setSourceType(newSourceType);
			setSourceNumber(newSourceNumber);
			setGeneralType(newGeneralType);
			setSpecificType(newSpecificType);
			setEventSpecificData(newEventSpecificData);
		}
		
		public int getSourceType() {
			return sourceType;
		}
		
		public void setSourceType(int newSourceType) {
			sourceType = newSourceType;
			sendMQTTMessage(sourceType, "/source/type");
		}
		
		public String getSourceNumber() {
			return sourceNumber;
		}
		
		public void setSourceNumber(String newSourceNumber) {
			sourceNumber = newSourceNumber;
			sendMQTTMessage(sourceNumber, "/source/number");
		}
		
		public int getGeneralType() {
			return generalType;
		}
		
		public void setGeneralType(int newGeneralType) {
			generalType = newGeneralType;
			sendMQTTMessage(generalType, "/type/general");
		}

		public int getSpecificType() {
			return specificType;
		}
		
		public void setSpecificType(int newSpecificType) {
			specificType = newSpecificType;
			sendMQTTMessage(specificType, "/type/specific");
		}

		public String getEventSpecificData() {
			return eventSpecificData;
		}
		
		public void setEventSpecificData(String newEventSpecificData) {
			eventSpecificData = newEventSpecificData;
			sendMQTTMessage(eventSpecificData, "/type/eventSpecificData");
		}		
		@Override
		public String toString() {
			StringBuilder newString = new StringBuilder();
			newString.append("\"sourceType\":" + sourceType + ",");
			newString.append("\"sourceNumber\":\"" + sourceNumber + "\",");
			newString.append("\"generalType\":" + generalType + ",");
			newString.append("\"specificType\":" + specificType + ",");
			newString.append("\"eventSpecificData\":\"" + eventSpecificData + "\",");
			newString.append(super.toString());
			return newString.toString();
		}
	}
	
	public class Siren extends AlarmObject {
		
		private int repetitionCount;
		private String cadence;
		private Boolean on;

		public Siren(PartitionArea newParent) {
			super(newParent);
			on = new Boolean(false);
			sendUpdateOnlyIfChanged = false;
		}
		
		public Siren(int newRepetitionCount, String newCadence, PartitionArea newParent) {
			super(newParent);
			on = new Boolean(false);
			setRepetitionCount(newRepetitionCount);
			setCadence(newCadence);
			sendUpdateOnlyIfChanged = false;
		}
		
		public int getRepetitionCount() {
			return repetitionCount;
		}
		
		public void setRepetitionCount(int newRepetitionCount) {
			repetitionCount = newRepetitionCount;
			sendMQTTMessage(repetitionCount, "/repetitionCount");
		}
		
		public String getCadence() {
			return cadence;
		}
		
		public void setCadence(String newCadence) {
			cadence = newCadence;
			sendMQTTMessage(cadence, "/cadence");
		}
		
		public boolean isOn() {
			return on.booleanValue();
		}
		
		public void turnOn() {
			on = true;
			sendMQTTMessage(on.compareTo(true), "/status/on");
		}
		
		public void turnOff() {
			on = false;
			sendMQTTMessage(on.compareTo(true), "/status/on");
		}
		
		@Override
		public String toString() {
			StringBuilder newString = new StringBuilder();
			newString.append("\"on\":" + on.compareTo(true) + ",");
			newString.append("\"repetitionCount\":" + repetitionCount + ",");
			newString.append("\"cadence\":\"" + cadence + "\",");
			newString.append(super.toString());
			return newString.toString();
		}
	}
	
	public class Temperature extends AlarmObject {

		private int energySaverLowSetPoint;
		private int energySaverHighSetPoint;

		public Temperature(PartitionArea newParent) {
			super(newParent);
			setState("Temperature Undefined", -1);
			sendUpdateOnlyIfChanged = false;
		}

		public Temperature(int temperature, int newEnergySaverLowSetPoint, int newEnergySaverHighSetPoint, PartitionArea newParent) {
			super(newParent);
			setState("Temperature State", temperature);
			setEnergySaverLowSetPoint(newEnergySaverLowSetPoint);
			setEnergySaverHighSetPoint(newEnergySaverHighSetPoint);
			sendUpdateOnlyIfChanged = false;
		}
		
		public int getEnergySaverLowSetPoint() {
			return energySaverLowSetPoint;
		}
		
		public void setEnergySaverLowSetPoint(int newEnergySaverLowSetPoint) {
			energySaverLowSetPoint = newEnergySaverLowSetPoint;
			sendMQTTMessage(energySaverLowSetPoint, "/ESLSP");
		}
		public int getEnergySaverHighSetPoint() {
			return energySaverHighSetPoint;
		}
		
		public void setEnergySaverHighSetPoint(int newEnergySaverHighSetPoint) {
			energySaverHighSetPoint = newEnergySaverHighSetPoint;
			sendMQTTMessage(energySaverHighSetPoint, "/ESHSP");
		}
		
		@Override
		public String toString() {
			StringBuilder newString = new StringBuilder();
			newString.append("\"ESLSP\":" + energySaverLowSetPoint + ",");
			newString.append("\"ESHSP\":" + energySaverHighSetPoint + ",");
			newString.append(super.toString());
			return newString.toString();
		}
	}
}