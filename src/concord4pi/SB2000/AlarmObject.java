package concord4pi.SB2000;

import java.util.Date;

import concord4pi.Config;
import concord4pi.MQTT.MQTTService;

public class AlarmObject implements IAlarmObject {
	protected String id;

	protected AlarmObjectStatus currentState;
	protected AlarmObjectStatus lastState;

	protected boolean sendUpdateOnlyIfChanged = true;
	
	private Object parent;

	private AlarmObject next;
	private AlarmObject previous;
	
	protected MQTTService MQTTClient;
	
	public AlarmObject(Object newParent) {
		this("", newParent);
	}

	public AlarmObject(String id, Object newParent) {
		this.id = id;
		parent = newParent;
		setMQTTFromParent();
	}

	@Override
	public void setState(String action, int state) {
		setState(action, state, "", null);
	}

	public void setState(String action, int state, String description) {
		setState(action, state, description, null);
	}

	public void setState(String action, int state, String description, Object newUser) {
		lastState = currentState;
		currentState = new AlarmObjectStatus(action, state, new Date(), description, (User) newUser);
		sendStandardMQTTMessage();
	}

	@Override
	public AlarmObjectStatus getCurrentState() {
		return currentState;
	}

	@Override
	public Date getCurrentStateTimeStamp() {
		return currentState.getTimestamp();
	}

	@Override
	public AlarmObjectStatus getLastState() {
		return lastState;
	}

	@Override
	public Date getLastActiveStateTimeStamp() {
		return lastState.getTimestamp();
	}

	@Override
	public boolean is(String id) {
		return (this.id == id);
	}

	@Override
	public String getID() {
		return id;
	}

	@Override
	public void setParent(Object newParent) {
		this.parent = newParent;
	}

	@Override
	public AlarmObject getParent() {
		return (AlarmObject)parent;
	}

	@Override
	public AlarmObject find(String id) {
		if (getID().equals(id)) {
			return this;
		} else {
			if (this.next == null) {
				return null;
			} else {
				return this.next.find(id);
			}
		}
	}

	@Override
	public boolean exists(String id) {
		return (find(id) != null);
	}

	@Override
	public void add(AlarmObject newAlarmObject) {
		if (getID().equals(newAlarmObject.getID())) {
			// this object already exists
			return;
		} else if (getNext() == null) {
			this.setNext(newAlarmObject);
			newAlarmObject.setPrevious(this);
		} else {
			getNext().add(newAlarmObject);
		}
	}

	@Override
	public void setPrevious(AlarmObject previousObject) {
		this.previous = previousObject;
	}

	@Override
	public void setNext(AlarmObject nextObject) {
		this.next = nextObject;
	}

	@Override
	public AlarmObject getNext() {
		return next;
	}

	@Override
	public AlarmObject getPrevious() {
		return previous;
	}

	@Override
	public void clear(String id) {
		AlarmObject theObject = find(id);

		if (theObject != null) {
			if (theObject.getPrevious() == null) {
				// the beginning of the list;
				// TODO figure this out
			} else {
				theObject.getPrevious().setNext(theObject.getNext());
				this.previous = null;
				this.next = null;
			}
		}

		if (this.id.equals(id)) {

		}
	}

	@Override
	public void clean() {
		if (this.getNext() != null) {
			this.getNext().clean();
		}
		// we're at the end of the list so start deleting the list backwards
		// set this previous link to null
		this.setPrevious(null);

		// set the link from the previous node to this one to null
		this.getPrevious().setNext(null);
	}
	
	@Override
	public int getLength() {
		int i = 0;
		AlarmObject currentObj = this;
		while(currentObj != null) {
			i++;
			currentObj = currentObj.getNext();
		}
		return i;
	}
	
	@Override
	public String toString() {
		StringBuilder newString = new StringBuilder();
		newString.append("\"id\":\"" + id + "\",");
		newString.append("\"FullID\":\"" + getFullID() + "\",");

		newString.append("\"currentState\": {");
		if(currentState != null) {
			newString.append(currentState.toString());
		}
		newString.append("},");

		newString.append("\"lastState\": {");
		if(lastState != null) {
			newString.append(lastState.toString());
		}
		newString.append("}");
		
		return newString.toString();
	}
	
	public void setMQTTService(MQTTService newMQTTClient) {
		MQTTClient = newMQTTClient;
	}
	
	public MQTTService getMQTTService() {
		return MQTTClient;
	}
	
	public String getMQTTTopic() {
		return Config.getProperty(Config.MQTTCLIENTID) + "/" + this.getFullID();
	}
	
	protected void sendStandardMQTTMessage() {
		if(!sendUpdateOnlyIfChanged || (getLastState() == null)) {
			if(getCurrentState().getAction() != null) {
				sendMQTTMessage(getCurrentState().getAction(), "/action");
			}
			
			sendMQTTMessage(getCurrentState().getStatus(), "/status");

			if(getCurrentState().getDescription() != null) {
				sendMQTTMessage(getCurrentState().getDescription(), "/status/text");
			}
			
			if(getCurrentState().getUser() != null) {
				sendMQTTMessage(getCurrentState().getUser().getID(), "/user");
			}
		}
		else {
			
			if(!getCurrentState().getAction().equals(getLastState().getAction())) { 
				if(getCurrentState().getAction() != null) {
					sendMQTTMessage(getCurrentState().getAction(), "/action");
				}
			}
			if(getCurrentState().getStatus() != getLastState().getStatus()) { 
				sendMQTTMessage(getCurrentState().getStatus(), "/status");
			}
			if(!getCurrentState().getDescription().equals(getLastState().getDescription())) { 
				if(getCurrentState().getDescription() != null) {
					sendMQTTMessage(getCurrentState().getDescription(), "/status/text");
				}
			}
			
			if((getCurrentState().getUser() != null) && getLastState().getUser() != null) {
				if((getCurrentState().getUser().getID() != null) && (getLastState().getUser().getID() != null))
				{
					if(!getCurrentState().getUser().getID().equals(getLastState().getUser().getID())) {
						sendMQTTMessage(getCurrentState().getUser().getID(), "/user");
					}
				}
			}
			
		}
	}
	
	protected void sendMQTTMessage(String message, String topicSuffix) {
		MQTTClient.sendMessage(message, getMQTTTopic() + topicSuffix);
	}

	protected void sendMQTTMessage(int message, String topicSuffix) {
		MQTTClient.sendMessage(message, getMQTTTopic() + topicSuffix);
	}
	
	public String getFullID() {
		return getFullID(this);
	}
	
	private String getFullID(AlarmObject searchObject) {
		AlarmObject thisParent = searchObject.getParent();
		if(thisParent == null) {
			return searchObject.getClass().getSimpleName().toLowerCase() + searchObject.getID();
		}
		else {
			return getFullID(thisParent) + "/" + searchObject.getClass().getSimpleName().toLowerCase() + searchObject.getID();
		}
	}
	
	private void setMQTTFromParent() {
		if(this.parent != null) {
			MQTTClient = ((AlarmObject)this.parent).getMQTTService();
		}
	}
}
