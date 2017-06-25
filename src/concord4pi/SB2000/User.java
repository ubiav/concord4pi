package concord4pi.SB2000;

import java.util.logging.Level;

import concord4pi.logs.LogEngine;

public class User extends AlarmObject {
	private String userType;
	private String userTypeName;

	public User(String id, Area newParent) {
		super(id, newParent);
		LogEngine.Log(Level.FINEST, "Created new User with ID [" + id + "]", this.getClass().getName());
	}

	public void setType(String newType) {
		userType = newType;
		userTypeName = Constants.userTypeName(Integer.parseInt(userType,16));
		LogEngine.Log(Level.FINEST, "Set User with ID [" + id + "] to type [" + userTypeName + "]", this.getClass().getName());
	}
	
	@Override
	public String toString() {
		StringBuilder newString = new StringBuilder();
		newString.append("\"user" + id + "\":{");
		newString.append(super.toString() + ",");
		newString.append("\"userType\":\"" + userType + "\",");
		newString.append("\"userTypeName\":\"" + userTypeName + "\"");
		newString.append("}");
		return newString.toString();
	}
}
