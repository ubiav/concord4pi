package concord4pi.SB2000;

public class Panel {
	private String panelType;
	private String panelTypeName;
	private String hardwareRevision;
	private String softwareRevision;
	private String serialNumber;
	
	public Panel(String type, String hwRev, String swRev, String serNum) {
		setType(type);
		hardwareRevision = hwRev;
		softwareRevision = swRev;
		serialNumber = serNum;
	}
	
	public void setType(String type) {
		panelType = type;
		panelTypeName = Constants.panelTypeName(type);
	}
	
	public String getType() {
		return panelType;
	}
	
	public String getTypeName() {
		return panelTypeName;
	}
	
	public void setHWRevision(String hwRev) {
		hardwareRevision = hwRev;
	}	
	
	public String getHWRevision() {
		return hardwareRevision;
	}
	
	public void setSWRevision(String swRev) {
		softwareRevision = swRev;
	}
	
	public String getSWRevision() {
		return softwareRevision;
	}
	
	public void setSerialNumber(String serNum) {
		serialNumber = serNum;
	}
	
	public String getSerialNumber() {
		return serialNumber;
	}
	
}
