package concord4pi.SB2000;

import java.util.Date;
import java.util.Hashtable;

public class AlarmObjectStatus {
	private String action;
	private int status;
	private Date timestamp;
	private String description;
	private User user;
	private Hashtable<String, String> extendedAttributes;
	
	public AlarmObjectStatus() {
		extendedAttributes = new Hashtable<String, String>();
	}
	
	public AlarmObjectStatus(String newAction, int newStatus, Date newTimestamp, String newDescription, User newUser) {
		action = newAction;
		status = newStatus;
		timestamp = newTimestamp;
		description = newDescription;
		user = newUser;
		extendedAttributes = new Hashtable<String, String>();
	}

	public AlarmObjectStatus(String newAction, int newStatus, Date newTimestamp, String newDescription, User newUser, Hashtable<String, String> newExtendedAttributes) {
		action = newAction;
		status = newStatus;
		timestamp = newTimestamp;
		description = newDescription;
		user = newUser;
		extendedAttributes = newExtendedAttributes;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(String newAction, int newStatus) {
		action = newAction;
		status = newStatus;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(Date newTimestamp) {
		timestamp = newTimestamp;
	}
	
	public String getDescription() {
		if(description == null) {
			return "";
		}
		else {
			return description;
		}
	}
	
	public void setDescription(String newDescription) {
		description = newDescription;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User newUser) {
		user = newUser;
	}

	public String getAction() {
		return action;
	}
	
	public void setAction(String newAction) {
		action = newAction;
	}
	
	public void setExtendedAttributes(Hashtable<String, String> newExtendedAttributes) {
		extendedAttributes = newExtendedAttributes;
	}
	
	public Hashtable<String, String> getExtendedAttributes() {
		return extendedAttributes;
	}
	
	public String toString() {
		StringBuilder newString = new StringBuilder();
		newString.append("\"action\":\"" + action + "\",");
		newString.append("\"status\":" + status + ",");
		newString.append("\"timestamp\":\"" + timestamp + "\",");
		newString.append("\"description\":\"" + description + "\"");

		if(user != null) {
			newString.append(",\"user" + user.getID() + "\": {");
			newString.append(user.toString());
			newString.append("}");
		}
		
		return newString.toString();
		
	}

}
