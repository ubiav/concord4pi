package concord4pi.SB2000;

import java.util.logging.Level;

import concord4pi.logs.LogEngine;

public class Zone extends AlarmObject {
	private String type = Constants.ZONETYPE_NONE;
	private String group;
	private int keyFobCommand;
	
	public Zone(PartitionArea newParent) {
		super(newParent);
		LogEngine.Log(Level.FINEST, "Created new Zone", this.getClass().getName());
		
	}
	
	public Zone(String id, PartitionArea newParent) {
		super(id, newParent);
		LogEngine.Log(Level.FINEST, "Created new Zone with ID [" + id + "]", this.getClass().getName());
	}
	
	public void setGroup(String newGroup) {
		group = newGroup;
		sendMQTTMessage(group, "/group");
	}
	
	public String getGroup() {
		return group;
	}
	
	public void setKeyFobCommand(int newKeyFobCommand) {
		keyFobCommand = newKeyFobCommand;
		sendMQTTMessage(keyFobCommand, "/keyfob");
	}
	
	public int getKeyFobCommand() {
		return keyFobCommand;
	}
	
	public void setType(String newType) {
		type = newType;
		sendMQTTMessage(type, "/type");
	}
	
	public String getType() {
		return type;
	}

	public void updateZoneData(String action, int state, String type, String group, String text) {
		setState(action, state, text);
		setType(type);
		setGroup(group);
	}
	
	@Override
	public String toString() {
		StringBuilder newString = new StringBuilder();
		newString.append("\"zone" + id + "\":{");
		newString.append("\"type\":\"" + type + "\",");
		newString.append("\"group\":\"" + group + "\",");
		newString.append(super.toString());
		newString.append("}");
		return newString.toString();
	}

}
