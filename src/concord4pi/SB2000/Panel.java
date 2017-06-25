package concord4pi.SB2000;

import java.util.logging.Level;

import concord4pi.logs.LogEngine;

public class Panel extends AlarmObject {
	private String panelType;
	private String panelTypeName;
	private String hardwareRevision;
	private String softwareRevision;
	private String serialNumber;
	
	private final int _panelTypeUpdate = 1;
	private final int _panelHWUpdate = 2;
	private final int _panelSWUpdate = 3;
	private final int _panelSerNumUpdate = 4;
	
	public Panel(String type, String hwRev, String swRev, String serNum, Object parent) {
		super(parent);
		setType(type);
		hardwareRevision = hwRev;
		softwareRevision = swRev;
		serialNumber = serNum;
		LogEngine.Log(Level.FINEST, "Created new Panel with type [" + type + "] - HW Rev 57-" + hwRev + " - SW Rev 75-" + swRev + " - Serial " + serNum, this.getClass().getName());
	}
	
	public void setType(String type) {
		panelType = type;
		panelTypeName = Constants.panelTypeName(type);
		super.setState(panelType, _panelTypeUpdate, "Set Panel Type");
	}
	
	public String getType() {
		return panelType;
	}
	
	public String getTypeName() {
		return panelTypeName;
	}
	
	public void setHWRevision(String hwRev) {
		hardwareRevision = hwRev;
		super.setState(hardwareRevision, _panelHWUpdate, "Set HW Revision");
	}	
	
	public String getHWRevision() {
		return hardwareRevision;
	}
	
	public void setSWRevision(String swRev) {
		softwareRevision = swRev;
		super.setState(softwareRevision, _panelSWUpdate, "Set SW Revision");
	}
	
	public String getSWRevision() {
		return softwareRevision;
	}
	
	public void setSerialNumber(String serNum) {
		serialNumber = serNum;
		super.setState(serialNumber, _panelSerNumUpdate, "Set Serial Number");
	}
	
	public String getSerialNumber() {
		return serialNumber;
	}
	
	@Override
	public String toString() {
		StringBuilder newString = new StringBuilder();
		newString.append("\"panel\":{");
		newString.append("\"panelType\":\"" + panelType + "\",");
		newString.append("\"panelTypeName\":\"" + panelTypeName + "\",");
		newString.append("\"hardwareRevision\":\"" + hardwareRevision + "\",");
		newString.append("\"softwareRevision\":\"" + softwareRevision + "\",");
		newString.append("\"serialNumber\":\"" + serialNumber + "\",");
		newString.append(super.toString());
		newString.append("}");
		return newString.toString();
	}
}
