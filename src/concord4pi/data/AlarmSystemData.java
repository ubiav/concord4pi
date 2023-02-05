package concord4pi.data;

import java.util.Date;
import java.util.LinkedList;
import java.util.ListIterator;

import org.tinylog.Level;

import concord4pi.logging.LogEngine;
import concord4pi.messaging.IBroadcaster;
import concord4pi.messaging.NotificationPacket;

public class AlarmSystemData implements IAlarmObject {

	private AlarmPanel panel;
	private LinkedList<AlarmAreaPartition> areaPartitions = new LinkedList<AlarmAreaPartition>();
	private LinkedList<IBroadcaster> broadcasters;
	private LogEngine logger;
	private Date dateTime;
	
	public AlarmSystemData(LogEngine logger, LinkedList<IBroadcaster> broadcasters) {
		this.logger = logger;
		this.broadcasters = broadcasters;
		this.panel = new AlarmPanel(this, logger);
		updateTrigger(new NotificationPacket(myAddress(), "", NotificationPacket.NPACTION_NEW));
	}
	
	public AlarmPanel getPanel() {
		return panel;
	}
	
	//Gets the AreaPartition if exists.
	//If AreaPartition with the specified ID doesn't exist, then
	//this adds a new one with the specified ID
	public AlarmAreaPartition getAreaPartitionByID(String ID) {
		ListIterator<AlarmAreaPartition> apList = areaPartitions.listIterator();
		
		logger.log("Looking for PartitionArea by ID " + ID, Level.TRACE);
		
		while(apList.hasNext()) {
			AlarmAreaPartition theAP = apList.next();
			if(theAP.getID().equals(ID)) {
				logger.log("Found PartitionArea " + ID, Level.TRACE);
				return theAP;
			}
		}
		
		logger.log("PartitionArea " + ID + " not found! Adding new partition of ID: " + ID, Level.TRACE);
		return addAreaPartitionByID(ID);
	}
	
	private AlarmAreaPartition addAreaPartitionByID(String ID) {
		AlarmAreaPartition newAP = new AlarmAreaPartition(ID, this, logger); 
		areaPartitions.add(newAP);
		return newAP;
	}

	public String getID() {
		return "concord4pi";
	}
	
	public void sendSirenSync() {
		ListIterator<AlarmAreaPartition> apList = areaPartitions.listIterator();
		
		while(apList.hasNext()) {
			AlarmAreaPartition theAP = apList.next();
			logger.log("Sending Siren Sync to PartitionArea [" + theAP.getID() + "]", Level.TRACE);
			theAP.getSiren().addSyncEvent();
		}
	}
	
	public void sendSirenSignal(boolean go) {
		ListIterator<AlarmAreaPartition> apList = areaPartitions.listIterator();
		
		while(apList.hasNext()) {
			AlarmAreaPartition theAP = apList.next();
			logger.log("Sending Siren Signal [" + go + "] to PartitionArea [" + theAP.getID() + "]", Level.TRACE);
			if(go) { theAP.getSiren().goSiren(); }
			else { theAP.getSiren().stopSiren(); }
		}
	}
	
	public void setDate(Date date) {
		updateTrigger(new NotificationPacket(myAddress() + "/date", "" + date, NotificationPacket.NPACTION_UPDATE));
		this.dateTime = date;
	}
	
	public Date getDate() {
		return this.dateTime;
	}
	
	@Override
	public String toJSON() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String myAddress() {
		return "";
	}

	@Override
	public void updateTrigger(NotificationPacket packet) {
		ListIterator<IBroadcaster> broadcasterList = broadcasters.listIterator();
		
		while(broadcasterList.hasNext()) {
			broadcasterList.next().sendNotification(packet);
		}

		// send out notification of the update
		//logger.log("NOTIFICATION PACKET RECEIVED FOR BROADCAST => \n\tkey ->\t" + packet.key + "\n\tvalue ->\t" + packet.value + "\n\taction ->\t" + packet.action, Level.TRACE);
	}

}
