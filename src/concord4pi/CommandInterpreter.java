package concord4pi;

import java.util.Queue;
import java.util.logging.Level;

import concord4pi.SB2000.Constants;
import concord4pi.SB2000.Message;
import concord4pi.SB2000.State;
import concord4pi.logs.LogEngine;

public class CommandInterpreter {

	private State alarmSystemState;
	private Queue<Message> txQueue;
	private final Level logLevel = Level.FINER;

	public CommandInterpreter(State alarmState, Queue<Message> tx) {
		alarmSystemState = alarmState;
		txQueue = tx;
	}

	public String[] lookupRxCommand(String rxCommand) {
		String[] stringValue = new String[2];
		LogEngine.Log(Level.FINEST, "Searching for command [" + rxCommand + "]", this.getClass().getName());
		switch (rxCommand) {
		case Constants.MESSAGECOMMAND_PANELTYPE:
			stringValue[0] = "PANEL TYPE";
			stringValue[1] = "panelType";
			break;

		case Constants.MESSAGECOMMAND_AUTOMATIONEVENTLOST:
			stringValue[0] = "AUTOMATION EVENT LOST";
			stringValue[1] = "automationEventLost";
			break;

		case Constants.MESSAGECOMMAND_SELZONE:
			stringValue[0] = "SEND EQUIPMENT LIST - ZONE DATA";
			stringValue[1] = "zoneData";
			break;

		case Constants.MESSAGECOMMAND_SELPARTITION:
			stringValue[0] = "SEND EQUIPMENT LIST - PARTITION DATA";
			stringValue[1] = "partitionData";
			break;

		case Constants.MESSAGECOMMAND_SELSUPERBUSDEVICE:
			stringValue[0] = "SEND EQUIPMENT LIST - SuperBus DEVICE DATA";
			stringValue[1] = "noOp";
			//stringValue[1] = "sbDeviceData";
			break;

		case Constants.MESSAGECOMMAND_CADI:
			stringValue[0] = "CLEAR AUTOMATION DYNAMIC IMAGE";
			stringValue[1] = "noOp";
			break;

		case Constants.MESSAGECOMMAND_ZONESTATUS:
			stringValue[0] = "ZONE STATUS";
			stringValue[1] = "zoneStatus";
			break;

		case Constants.MESSAGECOMMAND_ARMINGLEVEL:
			stringValue[0] = "ARMING LEVEL";
			stringValue[1] = "armingLevel";
			break;

		case Constants.MESSAGECOMMAND_ALARMTROUBLE:
			stringValue[0] = "ALARM/TROUBLE";
			stringValue[1] = "alarmTrouble";
			break;

		case Constants.MESSAGECOMMAND_ENTRYEXITDELAY:
			stringValue[0] = "ENTRY/EXIT DELAY";
			stringValue[1] = "entryExitDelay";
			break;

		case Constants.MESSAGECOMMAND_SIRENSETUP:
			stringValue[0] = "SIREN SETUP";
			stringValue[1] = "sirenSetup";
			break;

		case Constants.MESSAGECOMMAND_SIRENSYNC:
			stringValue[0] = "SIREN SYNCHRONIZE";
			stringValue[1] = "sirenSync";
			break;

		case Constants.MESSAGECOMMAND_SIRENGO:
			stringValue[0] = "SIREN GO";
			stringValue[1] = "sirenGo";
			break;

		case Constants.MESSAGECOMMAND_TOUCHPADDISPLAY:
			stringValue[0] = "TOUCHPAD DISPLAY";
			stringValue[1] = "touchPadDisplay";
			break;

		case Constants.MESSAGECOMMAND_SIRENSTOP:
			stringValue[0] = "SIREN STOP";
			stringValue[1] = "sirenStop";
			break;

		case Constants.MESSAGECOMMAND_FEATURESTATE:
			stringValue[0] = "FEATURE STATE";
			stringValue[1] = "featureState";
			break;

		case Constants.MESSAGECOMMAND_TEMPERATURE:
			stringValue[0] = "TEMPERATURE";
			stringValue[1] = "temperature";
			break;

		case Constants.MESSAGECOMMAND_TIMEDATE:
			stringValue[0] = "TIME AND DATE";
			stringValue[1] = "timeAndDate";
			break;

		case Constants.MESSAGECOMMAND_LIGHTSSTATE:
			stringValue[0] = "LIGHTS STATE";
			stringValue[1] = "noOp";
			break;

		case Constants.MESSAGECOMMAND_USERLIGHTS:
			stringValue[0] = "USER LIGHTS COMMAND";
			stringValue[1] = "noOp";
			break;

		case Constants.MESSAGECOMMAND_KEYFOB:
			stringValue[0] = "KEYFOB COMMAND";
			stringValue[1] = "keyFob";
			break;

		default:
			stringValue[0] = "[" + rxCommand + "]";
			stringValue[1] = "noOp";
			break;
		}
		return stringValue;

	}

	public String noOp(Message currentMessage, String actionText) {
		alarmSystemState.setState(currentMessage.getCommandString(), 0, actionText);
		return "Ran NoOp";
	}

	public String sirenSync(Message currentMessage, String actionText) {
		alarmSystemState.setAllSirenState(Constants.SIREN_SYNC, actionText);
		return "Received Siren Synchronization";
	}

	public String sirenGo(Message currentMessage, String actionText) {
		alarmSystemState.setAllSirenState(Constants.SIREN_GO, actionText);
		return "Received Siren Go Command";
	}

	public String sirenStop(Message currentMessage, String actionText) {
		String data = currentMessage.getDataString();
		String partition = data.substring(0, 2);
		String area = data.substring(2, 4);
		String partitionArea = getPartitionAreaNumber(partition, area);

		alarmSystemState.getPartitionArea(partitionArea).getSiren().setState(actionText, Constants.SIREN_STOP);
		alarmSystemState.getPartitionArea(partitionArea).getSiren().turnOff();

		return "Received Siren Stop Command";
	}

	public String sirenSetup(Message currentMessage, String actionText) {
		String data = currentMessage.getDataString();
		String partition = data.substring(0, 2);
		String area = data.substring(2, 4);
		String partitionArea = getPartitionAreaNumber(partition, area);
		int repetitionCount = Integer.parseInt(data.substring(4, 6));
		String cadence = data.substring(6, 14);

		alarmSystemState.getPartitionArea(partitionArea).addSiren(repetitionCount, cadence);
		return "Received Siren Setup with Repetition Count " + repetitionCount + " and cadence [" + cadence + "]";
	}

	public String featureState(Message currentMessage, String actionText) {
		String data = currentMessage.getDataString();
		String partition = data.substring(0, 2);
		String area = data.substring(2, 4);
		String partitionArea = getPartitionAreaNumber(partition, area);
		int featureState = Integer.parseInt(data.substring(4, 6));

		alarmSystemState.getPartitionArea(partitionArea).setFeatureState(featureState);

		return "Received Feature State Update with Features [" + featureState + "]";
	}

	public String temperature(Message currentMessage, String actionText) {
		String data = currentMessage.getDataString();
		String partition = data.substring(0, 2);
		String area = data.substring(2, 4);
		String partitionArea = getPartitionAreaNumber(partition, area);
		int temperature = Integer.parseInt(data.substring(4, 6), 16);
		int ESLowSP = Integer.parseInt(data.substring(6, 8), 16);
		int ESHighSP = Integer.parseInt(data.substring(8, 10), 16);

		alarmSystemState.getPartitionArea(partitionArea).addTemperature(temperature, ESLowSP, ESHighSP);

		return "Received Temperature Command with Temp " + temperature + "ºF with HSP " + ESHighSP + " and LSP "
				+ ESLowSP;
	}

	public String timeAndDate(Message currentMessage, String actionText) {
		String data = currentMessage.getDataString();
		int hour = Integer.parseInt(data.substring(0, 2), 16);
		int minute = Integer.parseInt(data.substring(2, 4), 16);
		int month = Integer.parseInt(data.substring(4, 6), 16);
		int day = Integer.parseInt(data.substring(6, 8), 16);
		int year = Integer.parseInt(data.substring(8, 10), 16);
		
		alarmSystemState.setDate(year, month, day, hour, minute);

		return "Received Date and Time of " + alarmSystemState.getDate();
	}

	public String keyFob(Message currentMessage, String actionText) {
		String data = currentMessage.getDataString();
		String partition = data.substring(0, 2);
		String area = data.substring(2, 4);
		String partitionArea = getPartitionAreaNumber(partition, area);
		String zone = data.substring(4, 8);
		int keyFobCommand = Integer.parseInt(data.substring(8,10), 16);
		
		alarmSystemState.getZone(partitionArea, zone).setKeyFobCommand(keyFobCommand);

		return "Received KeyFOB Command " + keyFobCommand;
	}
	
	
	public String zoneStatus(Message currentMessage, String actionText) {
		String data = currentMessage.getDataString();
		String partition = data.substring(0, 2);
		String area = data.substring(2, 4);
		String partitionArea = getPartitionAreaNumber(partition, area);
		String zone = data.substring(4, 8);
		int zoneStatus = Integer.parseInt(data.substring(8, 10), 16);

		this.log("Setting Zone State");

		alarmSystemState.getZone(partitionArea, zone).setState(currentMessage.getCommandString(), zoneStatus);

		return "Received ZoneStatus [" + Constants.zoneStatusFlags(zoneStatus) + "] " + "in PartitionArea ["
				+ partitionArea + "] " + "in Zone [" + zone + "]";
	}

	public String zoneData(Message currentMessage, String actionText) {
		String data = currentMessage.getDataString();
		String partition = data.substring(0, 2);
		String area = data.substring(2, 4);
		String partitionArea = getPartitionAreaNumber(partition, area);
		String group = data.substring(4, 6);
		String zone = data.substring(6, 10);
		String zoneType = data.substring(10, 12);
		int zoneStatus = Integer.parseInt(data.substring(12, 14), 16);
		String zoneTextTokens[] = data.substring(14, data.length()).split("(?<=\\G.{2})");
		StringBuilder zoneTextToken = new StringBuilder();

		for (int i = 0; i < zoneTextTokens.length; i++) {
			zoneTextToken.append(Constants.textTokenName(zoneTextTokens[i]));
		}

		log("Setting Zone State");

		alarmSystemState.getZone(partitionArea, zone).updateZoneData(currentMessage.getCommandString(), zoneStatus,
				zoneType, group, zoneTextToken.toString());

		return "Received Zone Data " + "in Partition [" + partition + "] " + "in Area [" + area + "] " + "in Group ["
				+ group + "] " + "for Zone [" + zone + "] | " + "ZONE TYPE: [" + zoneType + "] " + "ZONE STATUS: ["
				+ Constants.zoneStatusFlags(zoneStatus) + "] " + "ZONE TEXT: [" + zoneTextToken.toString() + "]";
	}

	public String automationEventLost(Message currentMessage, String actionText) {
		// clear the state of the alarm and refresh it
		//alarmSystemState.clearAll();
		txQueue.add(new Message(Constants.FullEquipmentListCommand));
		txQueue.add(new Message(Constants.DynamicDataRefreshCommand));
		return "Received Automation Event Lost - sending Dynamic Data Refresh and Full Equipment List";
	}

	public String touchPadDisplay(Message currentMessage, String actionText) {
		String data = currentMessage.getDataString();
		String partition = data.substring(0, 2);
		String area = data.substring(2, 4);
		String partitionArea = getPartitionAreaNumber(partition, area);
		String messageType = data.substring(4, 6);
		String textTokens[] = data.substring(6, data.length()).split("(?<=\\G.{2})");
		StringBuilder textToken = new StringBuilder();

		for (int i = 0; i < textTokens.length; i++) {
			textToken.append(Constants.textTokenName(textTokens[i]));
		}

		alarmSystemState.getPartitionArea(partitionArea).getTouchPad().setState("TOUCHPAD DISPLAY",
				Integer.parseInt(messageType, 16), textToken.toString());

		return "Received Touchpad Text Token [" + textToken + "] " + "in Partition [" + partition + "] " + "in Area ["
				+ area + "] " + "as a " + Constants.messageTypeName(messageType) + " message type ";
	}

	public String panelType(Message currentMessage, String actionText) {
		String data = currentMessage.getDataString();
		String panelType = data.substring(0, 2);
		String hardwareRevision = data.substring(2, 6);
		String softwareRevision = data.substring(6, 10);
		String serialNumber = data.substring(10, 18);

		alarmSystemState.clearPanels();
		alarmSystemState.addPanel(panelType, hardwareRevision, softwareRevision, serialNumber);

		return "Received Panel Type [" + Constants.panelTypeName(panelType) + "] with HW Rev 57-" + hardwareRevision
				+ " and SW Rev 75-" + softwareRevision + " and Serial Number [" + serialNumber + "]";
	}

	public String armingLevel(Message currentMessage, String actionText) {
		String data = currentMessage.getDataString();
		String partition = data.substring(0, 2);
		String area = data.substring(2, 4);
		String partitionArea = getPartitionAreaNumber(partition, area);
		String userType = data.substring(4, 6);
		String userNumber = data.substring(6, 8);
		int armingLevel = Integer.parseInt(data.substring(8, 10));

		// make sure the user exists of the correct type
		alarmSystemState.getPartitionArea(partitionArea).getUser(userNumber).setType(userType);

		// set the new Arming Level
		alarmSystemState.getPartitionArea(partitionArea).addArmLevel(armingLevel,
				alarmSystemState.getPartitionArea(partitionArea).getUser(userNumber));

		// set the Partition/Area status
		alarmSystemState.getPartitionArea(partitionArea).setState(armingLevel + "",
				Integer.parseInt(currentMessage.getCommandString(), 16), Constants.armingLevelName(armingLevel),
				alarmSystemState.getPartitionArea(partitionArea).getUser(userNumber));

		return "Received Alarming Level " + Constants.armingLevelName(armingLevel) + "in Partition [" + partition + "] "
				+ "in Area [" + area + "] " + "for " + Constants.userNumberName(userNumber) + " [" + userNumber + "] ("
				+ userType + ":" + Constants.userTypeName(Integer.parseInt(userType, 16)) + ")";
	}

	public String entryExitDelay(Message currentMessage, String actionText) {
		String data = currentMessage.getDataString();
		String partition = data.substring(0, 2);
		String area = data.substring(2, 4);
		String partitionArea = getPartitionAreaNumber(partition, area);
		int delayFlags = Integer.parseInt(data.substring(4, 6), 16);
		int delayTime = (Integer.parseInt(data.substring(6, 8), 16) << 8) + Integer.parseInt(data.substring(8, 10), 16);

		alarmSystemState.getPartitionArea(partitionArea).setState(currentMessage.getCommandString(), delayFlags,
				"DELAY:" + delayTime + "|" + delayFlags);
		alarmSystemState.getPartitionArea(partitionArea).getArmingLevel().setDelayFlags(delayFlags);
		alarmSystemState.getPartitionArea(partitionArea).getArmingLevel().setDelayFlags(delayTime);

		return "Received Entry/Exit Delay of " + delayTime + "in Partition [" + partition + "] " + "in Area [" + area
				+ "] " + "with flags [" + Constants.exitEntryDelayFlags(delayFlags) + "]";
	}

	public String alarmTrouble(Message currentMessage, String actionText) {
		String data = currentMessage.getDataString();
		String partition = data.substring(0, 2);
		String area = data.substring(2, 4);
		String partitionArea = getPartitionAreaNumber(partition, area);
		int sourceType = Integer.parseInt(data.substring(4, 6), 16);
		String sourceNumber = data.substring(6, 12);

		if (sourceType == Constants.SOURCETYPE_ZONE) {
			// shorten the zone number to 2 bytes if it is a zone type
			sourceNumber = sourceNumber.substring(2);
		}

		int generalType = Integer.parseInt(data.substring(12, 14), 16);
		String generalTypeName = Constants.getGeneralType().get(generalType);
		int specificType = Integer.parseInt(data.substring(14, 16), 16);
		String specificTypeName = Constants.getSpecificType(generalType).get(specificType);
		String eventSpecificData = data.substring(16, 20);

		alarmSystemState.getPartitionArea(partitionArea).addAlarmTrouble(sourceType, sourceNumber, generalType,
				specificType, eventSpecificData);

		alarmSystemState.getPartitionArea(partitionArea).setState(sourceType + "",
				Integer.parseInt(currentMessage.getCommandString(), 16),
				"TROUBLE:" + generalTypeName + "|" + specificTypeName);

		alarmSystemState.getPartitionArea(partitionArea).getAlarmTrouble().setSourceType(sourceType);
		alarmSystemState.getPartitionArea(partitionArea).getAlarmTrouble().setSourceNumber(sourceNumber);
		alarmSystemState.getPartitionArea(partitionArea).getAlarmTrouble().setGeneralType(generalType);
		alarmSystemState.getPartitionArea(partitionArea).getAlarmTrouble().setSpecificType(specificType);
		alarmSystemState.getPartitionArea(partitionArea).getAlarmTrouble().setEventSpecificData(eventSpecificData);

		return "Received Alarm/Trouble notification " + "in Area [" + area + "] " + "in Partition [" + partition + "] "
				+ "from " + Constants.getSourceTypeName(sourceType) + " " + sourceNumber + " of type " + generalTypeName
				+ "." + specificTypeName + "with Event Specific Data ("
				+ Constants.getEventSpecificName(generalType, specificType) + ") [" + eventSpecificData + "]";

	}

	
	public String partitionData(Message currentMessage, String actionText) {
		String data = currentMessage.getDataString();
		String partition = data.substring(0, 2);
		String area = data.substring(2, 4);
		String partitionArea = getPartitionAreaNumber(partition, area);
		int armingLevel = Integer.parseInt(data.substring(4, 6));
		String textTokens[] = data.substring(6, data.length()).split("(?<=\\G.{2})");
		StringBuilder text = new StringBuilder();

		for (int i = 0; i < textTokens.length; i++) {
			text.append(Constants.textTokenName(textTokens[i]));
		}
		
		// set the new Arming Level
		alarmSystemState.getPartitionArea(partitionArea).addArmLevel(armingLevel, null);

		// set the Partition/Area status
		alarmSystemState.getPartitionArea(partitionArea).setState(armingLevel + "",
				Integer.parseInt(currentMessage.getCommandString(), 16), 
				Constants.armingLevelName(armingLevel) + ":" + text
		);

		return "Received Alarming Level " + Constants.armingLevelName(armingLevel) + "in Partition [" + partition + "] "
				+ "in Area [" + area + "] (" + text + ")";
	}

	public String sbDeviceData(Message currentMessage, String actionText) {
		String data = currentMessage.getDataString();
		String partition = data.substring(0, 2);
		String area = data.substring(2, 4);
		String partitionArea = getPartitionAreaNumber(partition, area);
		int armingLevel = Integer.parseInt(data.substring(4, 6));
		String text = data.substring(6, 8);

		// set the new Arming Level
		alarmSystemState.getPartitionArea(partitionArea).addArmLevel(armingLevel, null);

		// set the Partition/Area status
		alarmSystemState.getPartitionArea(partitionArea).setState(armingLevel + "",
				Integer.parseInt(currentMessage.getCommandString(), 16), 
				Constants.armingLevelName(armingLevel) + ":" + text
		);

		return "Received Alarming Level " + Constants.armingLevelName(armingLevel) + "in Partition [" + partition + "] "
				+ "in Area [" + area + "] (" + text + ")";
	}


	
	
	private String getPartitionAreaNumber(String partition, String area) {
		if (Integer.parseInt(partition) == 0) {
			return area;
		} else {
			return partition;
		}
	}

	private void log(String logMessage) {
		LogEngine.Log(this.logLevel, logMessage, this.getClass().getName());
	}

}
