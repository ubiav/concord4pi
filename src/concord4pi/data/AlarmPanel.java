package concord4pi.data;

import org.tinylog.Level;

import concord4pi.logging.LogEngine;
import concord4pi.messaging.NotificationPacket;

public class AlarmPanel implements IAlarmObject {

	private LogEngine logger;
	private String panelType;
	private String name;
	private String hwRev;
	private String swRev;
	private String serialNum;
	private AlarmSystemData parent;
	
	
	public AlarmPanel(AlarmSystemData parent, LogEngine logger) {
		this.parent = parent;
		this.logger = logger;
		updateTrigger(new NotificationPacket(myAddress(), "", NotificationPacket.NPACTION_NEW));
	}
	
	public void updatePanelData(String panelType, String hwRev, String swRev, String serial) {
		this.panelType = panelType;
		this.name = getPanelTypeText(panelType);
		this.hwRev = hwRev;
		this.swRev = swRev;
		this.serialNum = serial;
		updateTrigger(new NotificationPacket(myAddress() + "/type", panelType, NotificationPacket.NPACTION_UPDATE));
		updateTrigger(new NotificationPacket(myAddress() + "/name", getPanelTypeText(panelType), NotificationPacket.NPACTION_UPDATE));
		updateTrigger(new NotificationPacket(myAddress() + "/hwrev", hwRev, NotificationPacket.NPACTION_UPDATE));
		updateTrigger(new NotificationPacket(myAddress() + "/swrev", swRev, NotificationPacket.NPACTION_UPDATE));
		updateTrigger(new NotificationPacket(myAddress() + "/serial", serial, NotificationPacket.NPACTION_UPDATE));
		logger.log("Received update for " + this, Level.DEBUG);
	}

	public String getPanelTypeText(String panelType) {
		String panelTypeName;
		switch(panelType) {
			case "14" :
				panelTypeName = "Concord";
				break;
			case "0B" :
				panelTypeName = "Express";
				break;
			case "1E" :
				panelTypeName = "Express 4";
				break;
			case "0E" :
				panelTypeName = "Euro";
				break;
			default :
				panelTypeName = "UNKNOWN";
				break;
		}
		return panelTypeName;
	}
	
	@Override
	public String getID() {
		return serialNum;
	}
	
	@Override
	public String toString() {
		return name + " Panel of Type [" + panelType + "] with HW Rev 57-" + hwRev + 
						" and SW Rev 75-" + swRev + " and Serial Number [" + serialNum + "]";
	}
	
	@Override
	public String toJSON() {
		return "";
	}
	
	@Override
	public void updateTrigger(NotificationPacket packet) {
		parent.updateTrigger(packet);
	}

	@Override
	public String myAddress() {
		return parent.myAddress() + "/panel";
	}

}
