package concord4pi.SB2000;

import java.util.ArrayList;
import java.util.HashMap;

public class Partition {
	private HashMap<String, Area> areas = new HashMap<String, Area>();

	public void addArea(String areaNumber) {
		if(!areas.containsKey(areaNumber)) {
			areas.put(areaNumber, new Area());	
		}
	}
	
	public void clear() {
		areas.clear();
	}
	
	public Area getArea(String areaNumber) {
		return areas.get(areaNumber);
	}
	
	public int getNumberOfAreas() {
		return areas.size();
	}
}
