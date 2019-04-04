package concord4pi;

import java.util.ArrayList;
import java.util.Calendar;

import org.tinylog.Level;

import concord4pi.SB2000.IOMessage;
import concord4pi.SB2000.SB2000Constants;
import concord4pi.data.AlarmAreaPartition;
import concord4pi.data.AlarmTemperature;
import concord4pi.data.AlarmArmingLevel;
import concord4pi.data.AlarmSiren;
import concord4pi.data.AlarmSystemData;
import concord4pi.data.AlarmTouchpad;
import concord4pi.data.AlarmTrouble;
import concord4pi.data.AlarmZone;
import concord4pi.logging.LogEngine;

public class MessageProcessor {
	
	private AlarmSystemData alarmData;
	private LogEngine logger;
	
	public MessageProcessor(AlarmSystemData alarmData, LogEngine logger) {
		this.alarmData = alarmData;
		this.logger = logger;
	}
	
	public ArrayList<IOMessage> processMessage(IOMessage theMessage) {
		ArrayList<IOMessage> responses = new ArrayList<IOMessage>();
		
		switch (theMessage.getCommandCodeString()) {
		
		case SB2000Constants.COMMANDCODE_PANELTYPE:
			panelType(theMessage);
			break;
			
		case SB2000Constants.COMMANDCODE_AUTOMATIONEVENTLOST:
			responses.addAll(automationEventLost(theMessage));
			break;
			
		case SB2000Constants.COMMANDCODE_SELZONE:
			zoneData(theMessage);
			break;
		
		case SB2000Constants.COMMANDCODE_SELPARTITION:
			partitionData(theMessage);
			break;
			
		case SB2000Constants.COMMANDCODE_ZONESTATUS:
			zoneStatus(theMessage);
			break;
			
		case SB2000Constants.COMMANDCODE_ARMINGLEVEL:
			armingLevel(theMessage);
			break;
		
		case SB2000Constants.COMMANDCODE_ALARMTROUBLE:
			alarmTrouble(theMessage);
			break;
			
		case SB2000Constants.COMMANDCODE_ENTRYEXITDELAY:
			entryExitDelay(theMessage);
			break;
			
		case SB2000Constants.COMMANDCODE_SIRENSETUP:
			sirenSetup(theMessage);
			break;
		
		case SB2000Constants.COMMANDCODE_SIRENSYNC:
			sirenSync(theMessage);
			break;
		
		case SB2000Constants.COMMANDCODE_SIRENGO:
			sirenSignal(theMessage, true);
			break;
		
		case SB2000Constants.COMMANDCODE_SIRENSTOP:
			sirenSignal(theMessage, false);
			break;
			
		case SB2000Constants.COMMANDCODE_TOUCHPADDISPLAY:
			touchpadDisplay(theMessage);
			break;		
			
		case SB2000Constants.COMMANDCODE_FEATURESTATE:
			featureState(theMessage);
			break;		

		case SB2000Constants.COMMANDCODE_TEMPERATURE:
			temperature(theMessage);
			break;	

		case SB2000Constants.COMMANDCODE_TIMEDATE:
			timeAndDate(theMessage);
			break;	

		case SB2000Constants.COMMANDCODE_KEYFOB:
			keyFob(theMessage);
			break;
			
		default :
			logger.log("Received Message " + theMessage.toString() + " without handler", Level.WARN);
			break;
			
			
		}

		return responses;
	}
	
	private void panelType(IOMessage theMessage) {
		String data = theMessage.getDataString();
		String panelType = data.substring(0, 2);
		String hardwareRevision = data.substring(2, 6);
		String softwareRevision = data.substring(6, 10);
		String serialNumber = data.substring(10, 18);
		logger.log("Received Panel Type Update Message", Level.INFO);
		alarmData.getPanel().updatePanelData(panelType, hardwareRevision, softwareRevision, serialNumber);
	}
	
	private ArrayList<IOMessage> automationEventLost(IOMessage theMessage) {
		ArrayList<IOMessage> responses = new ArrayList<IOMessage>();
		
		// send a message to refresh the alarm state & clear up current alarm State
		responses.add(new IOMessage(SB2000Constants.FULLCOMMAND_FullEquipmentList));
		responses.add(new IOMessage(SB2000Constants.FULLCOMMAND_DynamicDataRefresh));
		logger.log("Received Automation Event Lost - sending Dynamic Data Refresh and Full Equipment List", Level.INFO);
		return responses;
	}
	
	private void zoneData(IOMessage theMessage) {
		String data = theMessage.getDataString();
		String partitionArea = data.substring(0, 4);
		int group = Integer.parseInt(data.substring(4, 6), 16);
		String zone = data.substring(6, 10);
		int zoneType = Integer.parseInt(data.substring(10, 12), 16);
		int zoneStatus = Integer.parseInt(data.substring(12, 14), 16);
		String zoneTextTokens[] = data.substring(14, data.length()).split("(?<=\\G.{2})");
		StringBuilder zoneText = new StringBuilder();

		//convert the tokens into a valid text string
		for (int i = 0; i < zoneTextTokens.length; i++) {
			zoneText.append(SB2000Constants.convertTextToken(zoneTextTokens[i]));
		}
		
		AlarmAreaPartition theAP = alarmData.getAreaPartitionByID(partitionArea);
		
		AlarmZone theZone = theAP.getZoneByID(zone);		
		theZone.updateZoneData(group, zoneType, (byte)zoneStatus, zoneText.toString());
		
		logger.log("Received Zone Data " + "in PartitionArea [" + partitionArea + "] for Zone [" + zone + "] from Message " + theMessage, Level.INFO);
	}

	private void partitionData(IOMessage theMessage) {
		String data = theMessage.getDataString();
		String partitionArea = data.substring(0, 4);
		int armingLevel = Integer.parseInt(data.substring(4, 6));
		String textTokens[] = data.substring(6, data.length()).split("(?<=\\G.{2})");
		StringBuilder text = new StringBuilder();

		for (int i = 0; i < textTokens.length; i++) {
			text.append(SB2000Constants.convertTextToken(textTokens[i]));
		}
		
		// set the new Arming Level
		alarmData.getAreaPartitionByID(partitionArea).getArmingLevel().setArmingLevel(armingLevel);
	}

	private void zoneStatus(IOMessage theMessage) {
		String data = theMessage.getDataString();
		String partitionArea = data.substring(0, 4);
		String zone = data.substring(4, 8);
		int zoneStatus = Integer.parseInt(data.substring(8, 10), 16);

		AlarmAreaPartition theAP = alarmData.getAreaPartitionByID(partitionArea);
		AlarmZone theZone = theAP.getZoneByID(zone);
		theZone.setStatus(zoneStatus);
		logger.log("Received ZoneStatus Update [" + theZone.getStatusText() + "] for PartitionArea [" + partitionArea + "] " + "in Zone [" + zone + "]", Level.INFO);
	}

	private void armingLevel(IOMessage theMessage) {
		String data = theMessage.getDataString();
		String partitionArea = data.substring(0, 4);
		String userID = data.substring(4, 8);
		int armingLevel = Integer.parseInt(data.substring(8, 10));
		
		AlarmAreaPartition theAP = alarmData.getAreaPartitionByID(partitionArea);
		AlarmArmingLevel newArmingLevel = theAP.getArmingLevel();
		newArmingLevel.setArmingLevel(armingLevel);
		newArmingLevel.setArmingUserByID(userID);
		
		logger.log("Received Arming Level [" + newArmingLevel.getArmingLevelText() + "] in PartitionArea [" + theAP.getID() + "] by user [" + userID +" (" + newArmingLevel.getArmingUser().getType() + ")]", Level.INFO);
	}
	
	private void alarmTrouble(IOMessage theMessage) {
		String data = theMessage.getDataString();
		String partitionArea = data.substring(0, 4);
		int sourceType = Integer.parseInt(data.substring(4, 6), 16);
		String sourceNumber = data.substring(6, 12);

		if (sourceType == SB2000Constants.SOURCETYPE_ZONE) {
			// shorten the zone number to 2 bytes if it is a zone type
			sourceNumber = sourceNumber.substring(2);
		}

		int generalType = Integer.parseInt(data.substring(12, 14), 16);
		int specificType = Integer.parseInt(data.substring(14, 16), 16);
		String eventData = data.substring(16, 20);

		AlarmAreaPartition theAP = alarmData.getAreaPartitionByID(partitionArea);
		AlarmTrouble theEvent = theAP.addAlarmTroubleEvent(generalType, specificType, sourceType, sourceNumber, eventData);

		logger.log("Received Alarm/Trouble notification " + "in PartitionArea [" + partitionArea + "] " 
					+ "from " + theEvent.getSourceTypeText() + " " + theEvent.getSourceNumber() + " of type "
					+ theEvent.getGeneralTypeText() + "/" + theEvent.getSpecificTypeText() + " [" 
					+ theEvent.getGeneralType() + "." + theEvent.getSpecificType() + "] with Event Specific Data [" + theEvent.getEventData() + "]"
				, Level.INFO);
	}
	
	private void entryExitDelay(IOMessage theMessage) {
		String data = theMessage.getDataString();
		String partitionArea = data.substring(0, 4);
		int delayFlags = Integer.parseInt(data.substring(4, 6), 16);
		int delayTime = (Integer.parseInt(data.substring(6, 8), 16) << 8) + Integer.parseInt(data.substring(8, 10), 16);

		AlarmAreaPartition theAP = alarmData.getAreaPartitionByID(partitionArea);
		AlarmArmingLevel armingLevel = theAP.getArmingLevel();
		
		armingLevel.setDelayFlags(delayFlags);
		armingLevel.setDelayTime(delayTime);

		logger.log("Received Entry/Exit Delay of " + delayTime + "in Partition [" + partitionArea + "] " +
				"with flags [" + armingLevel.getDelayFlagsText() + "]", Level.INFO);
	}
	
	private void sirenSetup(IOMessage theMessage) {
		String data = theMessage.getDataString();
		String partitionArea = data.substring(0, 4);
		int repetitionCount = Integer.parseInt(data.substring(4, 6), 16);
		String cadence = data.substring(6, 14);

		AlarmAreaPartition theAP = alarmData.getAreaPartitionByID(partitionArea);
		AlarmSiren siren = theAP.getSiren();
		
		siren.setRepetitionCount(repetitionCount);
		siren.setCadenceFromString(cadence);
		
		logger.log("Received Siren Setup with Repetition Count " + repetitionCount + " and cadence [" + siren.getCadenceText() + "]", Level.INFO);
	}
	
	private void sirenSync(IOMessage theMessage) {
		alarmData.sendSirenSync();
		logger.log("Received Siren Synchronization", Level.INFO);
	}
	
	private void sirenSignal(IOMessage theMessage, boolean go) {
		alarmData.sendSirenSignal(go);
		logger.log("Received Siren GO/STOP Signal + [" + go + "]", Level.INFO);
	}
	
	private void touchpadDisplay(IOMessage theMessage) {
		String data = theMessage.getDataString();
		String partitionArea = data.substring(0, 4);
		int messageType = Integer.parseInt(data.substring(4, 6), 16);
		String textTokens = data.substring(6, data.length());
		
		AlarmAreaPartition theAP = alarmData.getAreaPartitionByID(partitionArea);
		AlarmTouchpad touchpad = theAP.getTouchpad();
		touchpad.setTextFromTokens(textTokens);
		touchpad.setBroadcastFlag(messageType == 1);

		logger.log("Received Touchpad Text Token [" + touchpad.getText() + "] " + "in Partition [" + partitionArea + "] " +
				"as a " + touchpad.getBroadcastFlagText() + " message type ", Level.INFO);
		
	}


	private void featureState(IOMessage theMessage) {
		String data = theMessage.getDataString();
		String partitionArea = data.substring(0, 4);
		int featureState = Integer.parseInt(data.substring(4, 6));

		AlarmAreaPartition theAP = alarmData.getAreaPartitionByID(partitionArea);
		
		theAP.setFeatureSet((byte)featureState);
		
		logger.log("Received Feature State Update with Features [" + theAP.getFeatureSetText() + "]", Level.INFO);
	}
	
	private void temperature(IOMessage theMessage) {
		String data = theMessage.getDataString();
		String partitionArea = data.substring(0, 4);
		int temperature = Integer.parseInt(data.substring(4, 6), 16);
		int LSP = Integer.parseInt(data.substring(6, 8), 16);
		int HSP = Integer.parseInt(data.substring(8, 10), 16);

		AlarmAreaPartition theAP = alarmData.getAreaPartitionByID(partitionArea);
		AlarmTemperature temp = theAP.getTemperature();
		
		temp.setTemperatureAll(temperature, HSP, LSP);

		logger.log("Received Temperature Command with Temp " + temperature + "oF with HSP " + HSP + " and LSP " + LSP, Level.INFO);
	}

	private void timeAndDate(IOMessage theMessage) {
		String data = theMessage.getDataString();
		int hour = Integer.parseInt(data.substring(0, 2), 16);
		int minute = Integer.parseInt(data.substring(2, 4), 16);
		int month = Integer.parseInt(data.substring(4, 6), 16);
		int day = Integer.parseInt(data.substring(6, 8), 16);
		int year = Integer.parseInt(data.substring(8, 10), 16);
		
		Calendar dateTime = Calendar.getInstance();
		dateTime.set(year, month, day, hour, minute);
		
		alarmData.setDate(dateTime.getTime());

		logger.log("Received Date and Time of " + dateTime.getTime(), Level.INFO);
	}

	private void keyFob(IOMessage theMessage) {
		String data = theMessage.getDataString();
		String partitionArea = data.substring(0, 4);
		String zone = data.substring(4, 8);
		int keyFobCommand = Integer.parseInt(data.substring(8,10), 16);

		AlarmAreaPartition theAP = alarmData.getAreaPartitionByID(partitionArea);
		AlarmZone theZone = theAP.getZoneByID(zone);
		theZone.setKeyFobCommand(keyFobCommand);
	
		logger.log("Received KeyFob Command [" + theZone.getKeyFobCommandText() + "]", Level.INFO);
	}
	
}
 