package concord4pi.data;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.tinylog.Level;

import concord4pi.logging.LogEngine;
import concord4pi.messaging.NotificationPacket;

public class AlarmSiren implements IAlarmObject {

	private final int maxMessageHistory = 5;
	
	private AlarmAreaPartition parent;
	private LogEngine logger;
	
	private int repetitionCount = 0;
	private int cadence = 0;
	private boolean isOn = false;
	private List<Date> syncEvent = new LinkedList<Date>();
	
	public AlarmSiren(AlarmAreaPartition parent, LogEngine logger) {
		this.parent = parent;
		this.logger = logger;
		updateTrigger(new NotificationPacket(myAddress(), "", NotificationPacket.NPACTION_NEW));
	}
	
	public void goSiren() {
		isOn = true;
		updateTrigger(new NotificationPacket(myAddress() + "/state", "" + isSirenOn(), NotificationPacket.NPACTION_UPDATE));
	}
	
	public void stopSiren() {
		isOn = false;
		updateTrigger(new NotificationPacket(myAddress() + "/state", "" + isSirenOn(), NotificationPacket.NPACTION_UPDATE));
	}
	
	public boolean isSirenOn() {
		return isOn;
	}
	
	/**
	 * START REPETITION COUNT SECTION
	 */
	
	public void setRepetitionCount(int repetitionCount) {
		this.repetitionCount = repetitionCount;
		updateTrigger(new NotificationPacket(myAddress() + "/repetitionCount", "" + repetitionCount, NotificationPacket.NPACTION_UPDATE));
		logger.log("Set Siren Repetition Count to [" + repetitionCount + "]", Level.TRACE);
	}
	
	public int getRepetitionCount() {
		return repetitionCount;
	}
	
	public String getRepetitionCountText() {
		return String.valueOf(repetitionCount);
	}
	
	/**
	 * END REPETITION COUNT SECTION
	 */
	
	/**
	 * START CADENCE SECTION
	 */
	
	public void setCadence(int cadence) {
		this.cadence = cadence;
		updateTrigger(new NotificationPacket(myAddress() + "/cadence", "" + cadence, NotificationPacket.NPACTION_UPDATE));
		updateTrigger(new NotificationPacket(myAddress() + "/cadence/binary", getCadenceText(), NotificationPacket.NPACTION_UPDATE));
		logger.log("Set Siren Cadence to [" + getCadenceText() + "]", Level.TRACE);
	}
	
	public void setCadenceFromString(String cadenceString) {
		
		if(cadenceString.length() != (Integer.BYTES * 2)) {
			logger.log("Invalid Siren Cadence [" + cadenceString + "]", Level.ERROR);
		}
		else {
			int byte1 = Integer.parseInt(cadenceString.substring(0, 2), 16) << 24;
			int byte2 = Integer.parseInt(cadenceString.substring(2, 4), 16) << 16;
			int byte3 = Integer.parseInt(cadenceString.substring(4, 6), 16) << 8;
			int byte4 = Integer.parseInt(cadenceString.substring(6, 8), 16);
			
			setCadence(byte1 + byte2 + byte3 + byte4);
		}
	}
	
	public int getCadence() {
		return this.cadence;
	}
	
	public String getCadenceText() {
		return Integer.toBinaryString(this.cadence);
	}
	
	
	/**
	 * END CADENCE SECTION
	 */
	
	
	/**
	 * START SirenSync SECTION
	 */
	
	public Date addSyncEvent() {
		Date newSyncEvent = new Date(); 
		syncEvent.add(newSyncEvent);
		
		updateTrigger(new NotificationPacket(myAddress() + "/sync", "" + newSyncEvent, NotificationPacket.NPACTION_UPDATE));
		
		while(syncEvent.size() > maxMessageHistory) {
			syncEvent.remove(syncEvent.get(syncEvent.size()-1));
		}
		
		logger.log("Added new Siren Sync Event [" + newSyncEvent + "]", Level.TRACE);
		return newSyncEvent;
	}
	
	public Date getMostRecentSirenSyncEvent() {
		return getPastSirenSyncEvent(0);
	}
	
	public Date getPastSirenSyncEvent(int eventPastIndex) {
		if(eventPastIndex < syncEvent.size()) {
			return syncEvent.get(eventPastIndex);
		}
		else {
			return null;
		}
	}
	
	/**
	 * END SirenSync SECTION
	 */

	@Override
	public String toJSON() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String myAddress() {
		return parent.myAddress() + "/siren";
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
