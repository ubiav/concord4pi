package concord4pi.SB2000;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

import concord4pi.MQTT.MQTTService;
import concord4pi.logs.LogEngine;

public class State extends AlarmObject {
	private Panel panel;
	private PartitionArea partitionAreas;
	private Date dateTimeStamp;
	
	
	public State() {
		super(null);
		LogEngine.Log(Level.FINEST, "Created new Alarm System State", this.getClass().getName());
	}
	
	public void addMQTTService(MQTTService mqtt) {
		MQTTClient = mqtt;
	}

	public void clearAll() {
		clearPanels();
		
		partitionAreas.clearAll();
		partitionAreas.clean();
		partitionAreas = null;
	}
	
	public void setAllSirenState(int state, String action) {
		PartitionArea currentPA = partitionAreas;
		while(currentPA != null) {
			currentPA.getSiren().setState(action, state);
			
			//if Siren Go command, turn the siren on
			if(state == Constants.SIREN_GO) {
				currentPA.getSiren().turnOn();				
			}
			currentPA = (PartitionArea)currentPA.getNext();
		}
	}
	
	public Date getDate() {
		return dateTimeStamp;
	}

	public void setDate(int year, int month, int date, int hourOfDay, int minute) {
		Calendar newCal = Calendar.getInstance();
		newCal.set(year, month - 1, date, hourOfDay, minute);
		setDate(newCal.getTime());
	}
	
	public void setDate(Date newDateTimeStamp) {
		dateTimeStamp = newDateTimeStamp;
		sendMQTTMessage(dateTimeStamp.toString(), "/dateTimeStamp");		
	}
	
	/**
	 *
	 * START PANELS
	 * 
	 */
	
	public void addPanel(String type, String hwRev, String swRev, String serNum) {
		panel = new Panel(type,hwRev,swRev,serNum,this);
	}
	
	public void clearPanels() {
		panel = null;
	}
	
	public Panel getPanel() {
		return panel;
	}

	/**
	 *
	 * END PANELS
	 * 
	 */
	
	/**
	 * 
	 * PARTITION Code
	 * 
	 */
	
	public PartitionArea getPartitionAreas() {
		return partitionAreas;
	}
	
	private PartitionArea addPartitionArea(String newPartAreaID) {
		PartitionArea newPartitionArea = new PartitionArea(newPartAreaID, this);
		if(partitionAreas == null) {
			partitionAreas = newPartitionArea;
		}
		else {
			partitionAreas.add(newPartitionArea);
		}
		return newPartitionArea;
	}
	
	public PartitionArea getPartitionArea(String partitionAreaID) {
		if(partitionAreas == null) {
			return addPartitionArea(partitionAreaID);
		}
		else {
			if(partitionAreaExists(partitionAreaID)) {
				return (PartitionArea)partitionAreas.find(partitionAreaID);
			}
			else {
				return addPartitionArea(partitionAreaID);
			}
		}
	}
	
	public PartitionArea getExistingPartitionArea(String partitionAreaID) {
		if(partitionAreaExists(partitionAreaID)) {
			return getPartitionArea(partitionAreaID);
		}
		else {
			return null;
		}
	}
	
	public boolean partitionAreaExists(String partitionAreaID) {
		PartitionArea thePart = (PartitionArea)partitionAreas.find(partitionAreaID);
		if(thePart == null) {
			return false;
		}
		else {
			return true;
		}
	}
	
	public void clearPartitionArea(String partitionAreaNumber) {
		if(partitionAreas.getID() == partitionAreaNumber) {
			//fix up the head node for the linked list
			PartitionArea newFirst = (PartitionArea)partitionAreas.getNext();
			PartitionArea currentFirst = partitionAreas;
			
			currentFirst.setNext(null);
			currentFirst.setPrevious(null);
			
			partitionAreas = newFirst;
			partitionAreas.setPrevious(null);
		}
		else {
			partitionAreas.clear(partitionAreaNumber);
		}
	}
	
	/**
	 * 
	 * END PARTITION Code
	 * 
	 */
	
	/**
	 * 
	 * ZONE CODE
	 * 
	 */

	public Zone getZone(String partitionAreaID, String zoneID) {
		return getPartitionArea(partitionAreaID).getZone(zoneID);
	}
	
	public Zone getExistingZone(String zoneID) {
		PartitionArea currentPA = partitionAreas;
		while(currentPA != null) {
			if(currentPA.zoneExists(zoneID)) {
				return currentPA.getZone(zoneID);
			}
			currentPA = (PartitionArea)currentPA.getNext();
		}
		return null;
	}
	
	/**
	 * 
	 * END ZONE CODE
	 * 
	 */
	
	public String toString() {
		StringBuilder newString = new StringBuilder();
		newString.append("\"alarmSystem\":{");
		
		if(panel != null) {newString.append(panel.toString() + ",");}
		
		newString.append("\"partitionAreas\": {");
		PartitionArea currentPartition = partitionAreas;
		while(currentPartition != null) {
			newString.append(currentPartition.toString());
			currentPartition = (PartitionArea)currentPartition.getNext();
			if(currentPartition != null) {
				newString.append(",");
			}
		}
		newString.append("},");
		newString.append(super.toString());
		newString.append("}");

		return newString.toString();
	}
}



