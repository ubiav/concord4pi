package concord4pi.MQTT;

import java.util.Queue;
import java.util.logging.Level;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import concord4pi.Config;
import concord4pi.SB2000.Constants;
import concord4pi.SB2000.Message;
import concord4pi.SB2000.State;
import concord4pi.logs.LogEngine;


public class MQTTService {
	private MqttClient clientConnection;
	private Queue<Message> txQueue;

	private final String PartMarker = "partition";
	private final String AreaMarker = "area";
	
	private State alarmSystem;

	public MQTTService(Queue<Message> tx) {
		txQueue = tx;
		try {
			clientConnection = new MqttClient(
									Config.getProperty(Config.MQTTCONNECTIONSTRING), 
									Config.getProperty(Config.MQTTCLIENTID)
								);	
		} catch (MqttException e) {
			LogEngine.Log(Level.SEVERE, "Could not create MQTT Client Connection", this.getClass().getName());
			System.exit(0);
		}
	}
	
	public boolean connect() {
		final String username = Config.getProperty(Config.MQTTUSER);
		final String password = Config.getProperty(Config.MQTTPASS);
		
		try {
			
			MqttConnectOptions connectionOptions = new MqttConnectOptions();
			connectionOptions.setAutomaticReconnect(true);
			
			if(!username.isEmpty()) {
				connectionOptions.setUserName(username);
			}
			
			if(!password.isEmpty()) {
				connectionOptions.setPassword(password.toCharArray());
			}

			clientConnection.setCallback(new MQTTMessageHandler());
			clientConnection.connect(connectionOptions);
			startListening(Config.getMqttCmdTopic());
			
		} catch (MqttException e) {
			LogEngine.Log(Level.SEVERE, "Could not connect to MQTT Broker", this.getClass().getName());
			return false;
		}
		return true;
	}
	
	public boolean disconnect() {
		try {
			clientConnection.disconnect();
		} catch (MqttException e) {
			LogEngine.Log(Level.WARNING, "Could not disconnect from MQTT Broker", this.getClass().getName());
			return false;
		}
		return true;
	}
	
	public boolean startListening(String topic) {
		try {
			clientConnection.subscribe(topic);
			LogEngine.Log(Level.INFO, "Listening for input (" + topic + ")", this.getClass().getName());
		} catch (MqttException e) {
			LogEngine.Log(Level.WARNING, "Failed to start listening", this.getClass().getName());
			return false;
		}
		return true;
	}
	
	public boolean stopListening(String topic) {
		try {
			clientConnection.unsubscribe(topic);
			LogEngine.Log(Level.INFO, "No longer listening for input (" + topic + ")", this.getClass().getName());
		} catch (MqttException e) {
			LogEngine.Log(Level.WARNING, "Failed to stop listening", this.getClass().getName());
			return false;
		}
		return true;
	}
	
	public boolean sendMessage(String messageText, String topic, boolean retainedMessage) {
		MqttMessage newMessage = new MqttMessage();
		newMessage.setPayload(messageText.getBytes());
		newMessage.setRetained(retainedMessage);
		try {
			LogEngine.Log(Level.FINE, "Sent Message {" + messageText + "} in topic: " + topic, this.getClass().getName());
			clientConnection.publish(topic, newMessage);
		} catch (MqttPersistenceException e) {
			LogEngine.Log(Level.INFO, "MQTT Persistence Error: " + e.getMessage(), this.getClass().getName());
			return false;
		} catch (MqttException e) {
			LogEngine.Log(Level.INFO, "Could not send MQTT Message: " + e.getMessage(), this.getClass().getName());
			return false;
		}
		return true;		
	}
	
	public boolean sendMessage(String messageText, String topic) {
		return sendMessage(messageText, topic, true);
	}
	
	public boolean sendMessage(int messageStatus, String topic) {
		return sendMessage(Integer.toString(messageStatus), topic);
	}

	
	private class MQTTMessageHandler implements MqttCallback {

		@Override
		public void connectionLost(Throwable ConnectionLost) {
			LogEngine.Log(Level.SEVERE, "MQTT Connection Lost! - " + ConnectionLost.toString(), this.getClass().getName());
		}

		@Override
		public void deliveryComplete(IMqttDeliveryToken DeliveryToken) {
			LogEngine.Log(Level.FINE, "MQTT Message Sent", this.getClass().getName());
		}

		@Override
		public void messageArrived(String topic, MqttMessage message) throws Exception {
			LogEngine.Log(Level.FINE, "MQTT Message Received", this.getClass().getName());
			handleMessage(topic, message.toString());
		}
		
		private void handleMessage(String topic, String messageText) {
			String[] newTopicArray = topic.substring(Config.getMqttCmdTopic().length()-1).split("/");
			LogEngine.Log(Level.INFO, "Received message with short topic " + String.join("/", newTopicArray), this.getClass().getName());
			Message newMessage;
			
			/*if(newTopicArray[0].equalsIgnoreCase("raw")) {
				/*Message newMessage = new Message(messageText.trim());
				if(newMessage.validCheckSum()) {
					txQueue.add(newMessage);
					LogEngine.Log(Level.FINE, "Added message (" + newMessage + ") to Trasmit Queue", this.getClass().getName());
				}
				else {
					LogEngine.Log(Level.WARNING, "Message is malformed!  Could not add to Transmit Queue.", this.getClass().getName());
				}
			}*/

		
			if(newTopicArray[0].contains(PartMarker) || newTopicArray[0].contains(AreaMarker)) {
				//got a command for a partition/area
				String partitionID;
				String areaID;
				
				if(newTopicArray[0].equals("area")) {
					partitionID = "00";
					areaID = newTopicArray[1];
				}
				else {
					partitionID = newTopicArray[1];
					areaID = "00";
				}

				if(messageText.equals("arm")) {
					newMessage = new Message(Constants.MESSAGECOMMAND_KEYPRESS, 
							partitionID + 
							areaID + 
							Constants.KEYPRESS_KEYFOBARM
					);
					addMessageToTXQueue(newMessage, this);
				}
				else if(messageText.equals("disarm")) {
					newMessage = new Message(Constants.MESSAGECOMMAND_KEYPRESS, 
							partitionID + 
							areaID + 
							Constants.KEYPRESS_KEYFOBDISARM
					);
					addMessageToTXQueue(newMessage, this);
				}
				
			}
			else if(newTopicArray[0].contains("discovery")) {
				sendMessage(alarmSystem.toString(), Config.getProperty(Config.MQTTCLIENTID) + "/discovery", false);				
			}
			else {
				LogEngine.Log(Level.WARNING, "Invalid MQTT Topic Received - {" + String.join("/", newTopicArray) + "|" + messageText + "}", this.getClass().getName());
			}

		}
		
		private void addMessageToTXQueue(Message theMessage, Object caller) {
			LogEngine.Log(Level.FINER, "Adding message " + theMessage.toString() + " to TXQueue", caller.getClass().getName());
			txQueue.add(theMessage);
		}
		
	}
	
}
