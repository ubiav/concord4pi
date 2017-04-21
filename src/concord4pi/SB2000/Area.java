package concord4pi.SB2000;

import java.util.ArrayList;
import java.util.HashMap;

public class Area {
	private HashMap<String, Zone> zones = new HashMap<String, Zone>();
	private ArrayList<User> users = new ArrayList<User>();
	private ArrayList<TouchPad> touchpads = new ArrayList<TouchPad>();


	public void addZone(String zoneNumber) {
		if(!zones.containsKey(zoneNumber)) {
			zones.put(zoneNumber, new Zone());	
		}
	}
	
	public void clear() {
		zones.clear();
	}
	
	public Zone getZone(String zoneNumber) {
		if(!zones.containsKey(zoneNumber)) {
			addZone(zoneNumber);
		}
		return zones.get(zoneNumber);
	}
	
	public int getNumberOfZones() {
		return zones.size();
	}
}