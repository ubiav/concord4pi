package concord4pi.messaging;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.tinylog.Level;

import concord4pi.SB2000.IOMessage;
import concord4pi.logging.LogEngine;

public class MQTTService implements Runnable, IBroadcaster {

	private Queue<NotificationPacket> notificationQueue = new ConcurrentLinkedQueue<NotificationPacket>();
	private Queue<IOMessage> returnMessages = new ConcurrentLinkedQueue<IOMessage>();
	private MqttClient clientConnection;
	private LogEngine logger;
	
	private String username;
	private String password;
	private String topicBase;
	private String listenTopic;
	private boolean enableCommands = false;
	
	private boolean isRunning = false;
	
	private final boolean retainMessages = true;
	
	public MQTTService(LogEngine logger, String connectionString, String clientID, String username, String password, String topicBase, String listenTopic, boolean enableCommands) {
		this.logger = logger;
		this.username = username;
		this.password = password;
		this.topicBase = topicBase;
		this.listenTopic = listenTopic;
		this.enableCommands = enableCommands;
		
		try {
			clientConnection = new MqttClient(
					connectionString, 
					clientID
					);	
			logger.log("Created MQTT Service connection", Level.DEBUG);
			isRunning = connect();
			startListening();
		} catch (MqttException e) {
			logger.log("Exception while connecting to MQTT Service!  Exiting...", Level.ERROR);
			System.exit(0);
		}
	}
	
	public void shutdown() {
		isRunning = false;
		while(clientConnection.isConnected());
		logger.log("Shutdown MQTT Service", Level.INFO);
	}

	@Override
	public void sendNotification(NotificationPacket packet) {
		logger.log("Added Notification Packet to MQTT Queue [" + packet + "]", Level.DEBUG);
		notificationQueue.offer(packet);		
	}

	@Override
	public void run() {
		while(isRunning) {
			//pull items off the of the Queue for transmitting via MQTT
			while(!notificationQueue.isEmpty()) {
				//send the messages in the notification queue
				NotificationPacket packet = notificationQueue.poll();
				logger.log("Sending Notification packet via MQTT Service [" + packet + "]", Level.DEBUG);
				sendMessage(packet);
			}

		}
		
		stopListening();		
		isRunning = disconnect();
	}
	
	private boolean connect() {
		try {
			
			MqttConnectOptions connectionOptions = new MqttConnectOptions();
			connectionOptions.setAutomaticReconnect(true);
			
			if((username != null) && (!username.isEmpty())) {
				connectionOptions.setUserName(username);
			}
			
			if((password != null) && (!password.isEmpty())) {
				connectionOptions.setPassword(password.toCharArray());
			}

			clientConnection.setCallback(new MQTTMessageHandler(logger));
			clientConnection.connect(connectionOptions);
			logger.log("Connection successful to MQTT Service!", Level.INFO);
			
		} catch (MqttException e) {
			logger.log("Could not connect to MQTT Broker... Disabling service!", Level.WARN);
			return false;
		}
		return true;
	}
	
	private boolean disconnect() {
		try {
			clientConnection.disconnect();
		} catch (MqttException e) {
			logger.log("Exception while trying to disconnect from MQTT Service!", Level.WARN);
			return false;
		}
		return true;
	}
	
	
	private boolean startListening() {
		try {
			clientConnection.subscribe(topicBase + "/" + listenTopic);
			logger.log("Listening for input on MQTT topic [" + topicBase + "/" + listenTopic + "]", Level.TRACE);
		} catch (MqttException e) {
			logger.log("Exception while trying to listen for input on MQTT topic [" + topicBase + "]", Level.TRACE);
			return false;
		}
		return true;
	}
	
	private boolean stopListening() {
		try {
			clientConnection.unsubscribe(topicBase + "/" + listenTopic);
			logger.log("No longer listening for input on MQTT topic [" + topicBase + "/" + listenTopic + "]", Level.TRACE);
		} catch (MqttException e) {
			logger.log("Exception while trying to stop listening for input on MQTT topic [" + topicBase + "]", Level.TRACE);
			return false;
		}
		return true;
	}
	
	public void sendMessage(NotificationPacket packet) {
		String messageTopic = packet.key;
		String message = packet.value;
		
		if((packet.action == NotificationPacket.NPACTION_NEW) && (message.isEmpty())) {
			logger.log("New object detected with no value... skipping MQTT messaging for this update.", Level.TRACE);
			//in the future we should really delete these topics or put in "null" as the message or something
		}
		else {
		
			//setup a new MQTT message
			MqttMessage newMessage = new MqttMessage();
			newMessage.setPayload(message.getBytes());
			newMessage.setRetained(retainMessages);
			
			try {
				clientConnection.publish(topicBase + messageTopic, newMessage);
				logger.log("Sent MQTT Message [" + message + "] in topic [" + messageTopic + "]", Level.TRACE);
			} catch (MqttPersistenceException e) {
				logger.log("MQTT Persistence Exception when trying to send MQTT Message [" + message + "] in topic [" + messageTopic + "]", Level.TRACE);
			} catch (MqttException e) {
				logger.log("General MQTT Exception when trying to send MQTT Message [" + message + "] in topic [" + messageTopic + "]", Level.TRACE);
			}
		}
	}
	
	@Override
	public boolean messageWaiting() {
		return !returnMessages.isEmpty();
	}
	
	@Override
	public ArrayList<IOMessage> getMessageList() {
		ArrayList<IOMessage> responses = new ArrayList<IOMessage>();
		while(messageWaiting()) {
			//move the message to the Array list
			//helps with the threading to make sure there are fewer race conditions
			responses.add(returnMessages.poll());
		}
		return responses;
	}
	
	private class MQTTMessageHandler implements MqttCallback {
		
		private LogEngine logger;
		
		public MQTTMessageHandler(LogEngine logger) {
			this.logger = logger;
			logger.log("Created MQTTMessageHandler Object", Level.TRACE);
		}
		
		@Override
		public void connectionLost(Throwable connectionLost) {
			logger.log("MQTTService Connection Lost! Attempting to Reconnect!", Level.WARN);
			//stopListening();
			//disconnect();
			//connect();
			//startListening();
		}

		@Override
		public void deliveryComplete(IMqttDeliveryToken deliveryToken) {
			logger.log("MQTT Message Delivery is completed", Level.TRACE);
		}

		@Override
		public void messageArrived(String topic, MqttMessage message) throws Exception {
			if(enableCommands) { 
				handleMessage(topic, message.toString()); 
			}
			
		}
		
		private void handleMessage( String topic, String message) {
			final String monitorTopic = topicBase + "/" + listenTopic;
			final String responseTopic = "/" + listenTopic + "/receipt";

			if(topic.contentEquals(monitorTopic)) {
				//only handle if it is specifically for the topic that I am listening for
				//if we need to handle sub-topics later, we can add an "else if" to handle that subtopic specifically
				//the message was just broadcast in the base topic... handle as a command.
				logger.log("Received MQTT Message on listen topic... treating as raw command", Level.DEBUG);
				IOMessage newMessage = new IOMessage(message.getBytes());
				if(newMessage.isValid()) {
					returnMessages.offer(newMessage);
					logger.log("Received valid MQTT message [" + message + "]", Level.DEBUG);
					sendMessage(new NotificationPacket(responseTopic + "/text", "Received valid command [" + newMessage + "]", NotificationPacket.NPACTION_NEW));
					sendMessage(new NotificationPacket(responseTopic + "/code", "success", NotificationPacket.NPACTION_NEW));
					sendMessage(new NotificationPacket(responseTopic + "/message", newMessage.toString() , NotificationPacket.NPACTION_NEW));
				}
				else {
					logger.log("Received the MQTT message [" + message + "], but not a valid Alarm System Command.  Ignoring...", Level.DEBUG);
					sendMessage(new NotificationPacket(responseTopic + "/text", "Invalid valid command [" + newMessage + "]", NotificationPacket.NPACTION_NEW));
					sendMessage(new NotificationPacket(responseTopic + "/code", "fail", NotificationPacket.NPACTION_NEW));
					sendMessage(new NotificationPacket(responseTopic + "/message", newMessage.toString() , NotificationPacket.NPACTION_NEW));
				}
			}
			else {
				//this is an unhandled topic or empty topic or something like that.
				//this should never really happen
				//We want to know when this is happening, but nothing is supported to handle this
				logger.log("Received message to invalid topic [" + topic + ":" + message + "].  Ignoring...", Level.DEBUG);
				sendMessage(new NotificationPacket(responseTopic + "/text", "Received message to invalid topic [" + topic + ":" + message + "]", NotificationPacket.NPACTION_NEW));
				sendMessage(new NotificationPacket(responseTopic + "/code", "fail", NotificationPacket.NPACTION_NEW));
				sendMessage(new NotificationPacket(responseTopic + "/message", message , NotificationPacket.NPACTION_NEW));

			}
			
		
			
		}
		
	}
}
