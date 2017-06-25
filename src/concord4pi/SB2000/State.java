package concord4pi.SB2000;

import java.util.logging.Level;

import concord4pi.logs.LogEngine;

public class State extends AlarmObject {
	private Panel panel;
	private Partition partitions;
	
	public State() {
		super(null);
		LogEngine.Log(Level.FINEST, "Created new Alarm System State", this.getClass().getName());
		
	}
	
	public void clearAll() {
		clearPanels();
		
		partitions.clearAll();
		partitions.clean();
		partitions = null;
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
	
	public Partition getPartitions() {
		return partitions;
	}
	
	private Partition addPartition(String newPartID) {
		Partition newPartition = new Partition(newPartID, this);
		if(partitions == null) {
			partitions = newPartition;
		}
		else {
			partitions.add(newPartition);
		}
		return newPartition;
	}
	
	public Partition getPartition(String partitionID) {
		if(partitions == null) {
			return addPartition(partitionID);
		}
		else {
			Partition thePart = (Partition)partitions.find(partitionID);
			if(thePart == null) {
				return addPartition(partitionID);
			}
			else {
				return thePart;
			}
		}
	}
	
	public void clearPartition(String partitionNumber) {
		if(partitions.getID() == partitionNumber) {
			//fix up the head node for the linked list
			Partition newFirst = (Partition)partitions.getNext();
			Partition currentFirst = partitions;
			
			currentFirst.setNext(null);
			currentFirst.setPrevious(null);
			
			partitions = newFirst;
			partitions.setPrevious(null);
		}
		else {
			partitions.clear(partitionNumber);
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

	public Zone getZone(String partitionID, String areaID, String zoneID) {
		return getPartition(partitionID).getArea(areaID).getZone(zoneID);
	}
	
	/**
	 * 
	 * END ZONE CODE
	 * 
	 */
	
	/**
	 * 
	 * AREA CODE
	 * 
	 */
	
	public Area getArea(String partitionID, String areaID) {
		return getPartition(partitionID).getArea(areaID);
		
	}

	/**
	 * 
	 * END AREA CODE
	 * 
	 */
	
	public String toString() {
		StringBuilder newString = new StringBuilder();
		newString.append("\"alarmSystem\":{");
		
		if(panel != null) {newString.append(panel.toString() + ",");}
		
		newString.append("\"partitions\": {");
		Partition currentPartition = partitions;
		while(currentPartition != null) {
			newString.append(currentPartition.toString());
			currentPartition = (Partition)currentPartition.getNext();
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



