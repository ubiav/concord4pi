package concord4pi.SB2000;

import java.util.logging.Level;

import concord4pi.logs.LogEngine;

public class Partition extends AlarmObject {
	private Area areas;
	
	public Partition(State newParent) {
		super(newParent);
		LogEngine.Log(Level.FINEST, "Created new Partition", this.getClass().getName());
	}
	
	public Partition(String id, State newParent) {
		super(id, newParent);
		LogEngine.Log(Level.FINEST, "Created new Partition with ID [" + id + "]", this.getClass().getName());
	}
	
	public void clearAll() {
		areas.clearAll();
		areas.clean();
		areas = null;
	}
	
	/**
	 * 
	 * AREA Code
	 * 
	 */
	
	public Area getAreas() {
		return areas;
	}
	
	private Area addArea(String areaID) {
		Area newArea = new Area(areaID, this);
		if(areas == null) {
			areas = newArea;
		}
		else {
			areas.add(newArea);
		}
		return newArea;
	}
	
	public Area getArea(String areaID) {
		if(areas == null) {
			return addArea(areaID);
		}
		else {
			Area theArea = (Area)areas.find(areaID);
			if(theArea == null) {
				return addArea(areaID);
			}
			else {
				return theArea;
			}			
		}
	}
	
	public void clearArea(String areaNumber) {
		if(areas.getID() == areaNumber) {
			//fix up the head node for the linked list
			Area newFirst = (Area)areas.getNext();
			Area currentFirst = areas;
			
			currentFirst.setNext(null);
			currentFirst.setPrevious(null);
			
			areas = newFirst;
			areas.setPrevious(null);
		}
		else {
			areas.clear(areaNumber);
		}
	}
	
	/**
	 * 
	 * END AREA Code
	 * 
	 */
	
	@Override
	public String toString() {
		StringBuilder newString = new StringBuilder();
		newString.append("\"partition" + id + "\":{");
		newString.append(super.toString() + ",");
		
			newString.append("\"areas\": {");
				Area currentArea = areas;
				while(currentArea != null) {
					newString.append(currentArea.toString());
					currentArea = (Area)currentArea.getNext();
					if(currentArea != null) {
						newString.append(",");
					}
				}
			newString.append("}");
	
		newString.append("}");
		
		return newString.toString();
	}
}
