package concord4pi.SB2000;

import java.util.Date;

public class AlarmObjectStatus {
	private String action;
	private int status;
	private Date timestamp;
	private String description;
	private User user;
	
	public AlarmObjectStatus() {
		
	}
	
	public AlarmObjectStatus(String newAction, int newStatus, Date newTimestamp, String newDescription, User newUser) {
		action = newAction;
		status = newStatus;
		timestamp = newTimestamp;
		description = newDescription;
		user = newUser;
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
		return description;
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
