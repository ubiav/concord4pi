package concord4pi.SB2000;

import java.util.ArrayList;
import java.util.HashMap;

public class State {
	private Panel panel;
	private HashMap<String, Partition> partitions = new HashMap<String, Partition>();
	
	public State() {
		
	}
	
	public void clearAll() {
		clearPanels();
		clearPartitions();
	}
	
	public void addPanel(String type, String hwRev, String swRev, String serNum) {
		panel = new Panel(type,hwRev,swRev,serNum);
	}
	
	public void clearPanels() {
		panel = null;
	}
	
	public Panel getPanel() {
		return panel;
	}
	
	public void addPartition(String partitionNumber) {
		if(!partitions.containsKey(partitionNumber)) {
			partitions.put(partitionNumber, new Partition());	
		}
	}
	
	public void clearPartitions() {
		partitions.clear();
	}
	
	public Partition getPartition(String partitionNumber) {
		return partitions.get(partitionNumber);
	}
	
	public int getNumberOfPartitions() {
		return partitions.size();
	}

	
}

