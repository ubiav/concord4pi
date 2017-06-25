package concord4pi.SB2000;

import java.util.logging.Level;

import concord4pi.logs.LogEngine;

public class Area extends AlarmObject {
	private Zone zones;
	private User users;
	private TouchPad touchpads;

	public Area(Partition newParent) {
		super(newParent);
		touchpads = new TouchPad(this);
		LogEngine.Log(Level.FINEST, "Created new Area", this.getClass().getName());

	}
	
	public Area(String id, Partition newParent) {
		super(id, newParent);
		touchpads = new TouchPad(this);
		LogEngine.Log(Level.FINEST, "Created new Area with ID [" + id + "]", this.getClass().getName());
	}
	

	public void clearAll() {
		zones.clean();
		zones = null;
		
		users.clean();
		users = null;
		
		touchpads.clean();
		touchpads = null;
	}


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
			Zone theZone = (Zone)zones.find(zoneID);
			if(theZone == null) {
				return addZone(zoneID);
			}
			else {
				return theZone;
			}
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
		newString.append("\"area" + id + "\":{");
		newString.append(super.toString() + ",");
		
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
}