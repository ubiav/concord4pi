package concord4pi.MQTT;

import java.util.logging.Level;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import concord4pi.logs.LogEngine;


public class MQTTService {
	private MqttClient clientConnection;

	public MQTTService(String connectionString, String clientID) {
		
		try {
			clientConnection = new MqttClient(connectionString, clientID);
		} catch (MqttException e) {
			LogEngine.Log(Level.SEVERE, "Could not create MQTT Client Connection", this.getClass().getName());
			System.exit(0);
		}
		
	}
	
	public boolean connect() {
		try {
			clientConnection.connect();
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
	
	public boolean sendMessage(String messageText, String topic) {
		MqttMessage newMessage = new MqttMessage();
		newMessage.setPayload(messageText.getBytes());
		newMessage.setRetained(true);
		//newMessage.setQos(1);
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
	
	public boolean sendMessage(int messageStatus, String topic) {
		return sendMessage(Integer.toString(messageStatus), topic);
	}
}
