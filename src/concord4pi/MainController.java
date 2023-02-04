package concord4pi;

import concord4pi.config.*;
import concord4pi.data.AlarmSystemData;
import concord4pi.logging.*;
import concord4pi.messaging.IBroadcaster;
import concord4pi.messaging.MQTTService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

import org.tinylog.Level;
import org.tinylog.provider.ProviderRegistry;

import concord4pi.SB2000.*;

public class MainController {
	
	private Config appConfig;
	private LogEngine logger;
	private SB2000Controller alarmSystem;
	private AlarmSystemData alarmData;
	private MessageProcessor messageProcessor;
	private LinkedList<IBroadcaster> broadcasters = new LinkedList<IBroadcaster>();
	
	
	private static boolean isRunning = true;
	
	public MainController(Config appConfig, LogEngine logger) {
		this.appConfig = appConfig;
		this.logger = logger;
		alarmSystem = new SB2000Controller(appConfig.getSerialDeviceName(), logger);
		alarmData = new AlarmSystemData(logger, broadcasters);
		messageProcessor = new MessageProcessor(alarmData, logger);
		addBroadcasters();
		Runtime.getRuntime().addShutdownHook(new appTerminator());
	}
	
	public void start() {
		//start up the broadcasters		
		startBroadcasters();
		
		//start the new thread for the alarm system
		new Thread(alarmSystem).start();
		
		//loop to do work
		while(isRunning) {
			//reset messagesToSend on each loop
			//initialize to a zero-based array (blank array)
			ArrayList<IOMessage> messagesToSend = new ArrayList<IOMessage>();
			
			if(alarmSystem.messageWaiting()) {
				//process the next message coming from the Automation Module
				//queue up any response messages to transmit back
				messagesToSend.addAll(
					messageProcessor.processMessage(
						alarmSystem.getNextMessage()
					)
				);
			}
			
			//process any commands coming in from the Broadcasters
			//this allows remote command execution, so we should be
			//careful about security here at some point
			ListIterator<IBroadcaster> broadcasterList = broadcasters.listIterator();
			
			while(broadcasterList.hasNext()) {
				IBroadcaster theBroadcaster = broadcasterList.next();
				if(theBroadcaster.messageWaiting()) {
					messagesToSend.addAll(
							theBroadcaster.getMessageList()
					);
				}
			}			
		
			//queue up the messages for transmission
			if(!messagesToSend.isEmpty()) {
				alarmSystem.sendMessages(messagesToSend.toArray(new IOMessage[0]));
			}

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void shutdown() {
		shutdownBroadcasters();
		alarmSystem.shutdown();
		
		//This should be the last on the list
		try {
			ProviderRegistry.getLoggingProvider().shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void addBroadcasters() {
		if(appConfig.getProperty("MQTTBroadcaster").equals("true")) {
			boolean enableCommands = (appConfig.getProperty("MQTTEnableCommands").equals("true"));
			//MQTT is enabled, so add it
			MQTTService mqtt = new MQTTService(
					logger,
					appConfig.getProperty("MQTTConnectionString"),
					appConfig.getProperty("MQTTClientID"),
					appConfig.getProperty("MQTTUsername"),
					appConfig.getProperty("MQTTPassword"),
					appConfig.getProperty("MQTTBaseTopic"),
					appConfig.getProperty("MQTTCommandTopic"),
					enableCommands
					);
			
			//add to the broadcaster pool
			broadcasters.offer(mqtt);
		}
	}
	
	private void startBroadcasters() {
		ListIterator<IBroadcaster> broadcasterList = broadcasters.listIterator();
		
		while(broadcasterList.hasNext()) {
			new Thread((Runnable) broadcasterList.next()).start();
		}
	}
	
	private void shutdownBroadcasters() {
		ListIterator<IBroadcaster> broadcasterList = broadcasters.listIterator();
		
		while(broadcasterList.hasNext()) {
			IBroadcaster theBroadcaster = broadcasterList.next();
			theBroadcaster.shutdown();
		}
	}

	
	class appTerminator extends Thread {
		@Override
		public void run() {
			logger.log("Received termination signal from system", Level.WARN);
			isRunning = false;
			shutdown();
		}
	}
}
