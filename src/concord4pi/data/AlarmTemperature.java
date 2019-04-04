package concord4pi.data;

import org.tinylog.Level;

import concord4pi.logging.LogEngine;
import concord4pi.messaging.NotificationPacket;

public class AlarmTemperature implements IAlarmObject {
	private AlarmAreaPartition parent;
	private LogEngine logger;
	
	private int temperature;
	private int HSP;
	private int LSP;
	
	public AlarmTemperature(AlarmAreaPartition parent, LogEngine logger) {
		this.parent = parent;
		this.logger = logger;
		updateTrigger(new NotificationPacket(myAddress(), "", NotificationPacket.NPACTION_NEW));
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
		updateTrigger(new NotificationPacket(myAddress(), "" + temperature, NotificationPacket.NPACTION_UPDATE));
		logger.log("Set Temperature to [" + temperature + "]", Level.TRACE);			
	}
	
	public void setHSP(int HSP) {
		this.HSP = HSP;
		updateTrigger(new NotificationPacket(myAddress() + "/HSP", "" + HSP, NotificationPacket.NPACTION_UPDATE));
		logger.log("Set High Set Point Temperature to [" + HSP + "]", Level.TRACE);			
	}
	
	public void setLSP(int LSP) {
		this.LSP = LSP;
		updateTrigger(new NotificationPacket(myAddress() + "/LSP", "" + LSP, NotificationPacket.NPACTION_UPDATE));
		logger.log("Set Low Set Point Temperature to [" + LSP + "]", Level.TRACE);			
	}
	
	public void setTemperatureAll(int temperature, int HSP, int LSP) {
		setTemperature(temperature);
		setHSP(HSP);
		setLSP(LSP);
	}
	
	public int getTemperature() {
		return temperature;
	}
	
	public int getHSP() {
		return HSP;
	}
	
	public int getLSP() {
		return LSP;
	}

	@Override
	public String toJSON() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String myAddress() {
		return parent.getID() + "/temperature";
	}

	@Override
	public String getID() {
		return null;
	}
	
	@Override
	public void updateTrigger(NotificationPacket packet) {
		parent.updateTrigger(packet);
	}
}
