package concord4pi.SB2000;

import java.util.logging.Level;

import concord4pi.logs.LogEngine;

public class Zone extends AlarmObject {
	private String type = Constants.ZONETYPE_NONE;
	private String text;
	private String group;

	public Zone(Area newParent) {
		super(newParent);
		LogEngine.Log(Level.FINEST, "Created new Zone", this.getClass().getName());
		
	}
	
	public Zone(String id, Area newParent) {
		super(id, newParent);
		LogEngine.Log(Level.FINEST, "Created new Zone with ID [" + id + "]", this.getClass().getName());
	}
	
	public void setGroup(String newGroup) {
		group = newGroup;
	}
	
	public String getGroup() {
		return group;
	}
	
	public void setType(String newType) {
		type = newType;
	}
	
	public String getType() {
		return type;
	}

	public void setText(String newText) {
		text = newText;
	}
	
	public String getText() {
		return text;
	}
	
	public void updateZoneData(String action, int state, String type, String group, String text) {
		setState(action, state);
		setType(type);
		setGroup(group);
		setText(text);
	}
	
	@Override
	public String toString() {
		StringBuilder newString = new StringBuilder();
		newString.append("\"zone" + id + "\":{");
		newString.append("\"type\":\"" + type + "\",");
		newString.append("\"text\":\"" + text + "\",");
		newString.append("\"group\":\"" + group + "\",");
		newString.append(super.toString());
		newString.append("}");
		return newString.toString();
	}

}
