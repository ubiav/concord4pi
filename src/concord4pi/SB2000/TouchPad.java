package concord4pi.SB2000;

import java.util.logging.Level;

import concord4pi.logs.LogEngine;

public class TouchPad extends AlarmObject {

	public TouchPad(Area newParent) {
		super("0", newParent);
		LogEngine.Log(Level.FINEST, "Created new TouchPad", this.getClass().getName());
	}
	
	@Override
	public String toString() {
		StringBuilder newString = new StringBuilder();
		newString.append("\"touchpad\":{");
		newString.append(super.toString());
		newString.append("}");
		return newString.toString();
	}
}
