package concord4pi;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
	private final static String configFile = "config.properties";
	
	public static final String SERIALPORTDEVICE = "SerialDevice";
	
	public static final String APIRESTENABLE = "APIRESTEnable";
	public static final String APIRESTPORT = "APIRestPort";
	public static final String APICLIENTKEY = "APIClientKey";
	
	
	public static final String MQTTCONNECTIONSTRING = "MQTTConnectionString";
	public static final String MQTTCLIENTID = "MQTTClientID";
	public static final String MQTTUSER = "MQTTUsername";
	public static final String MQTTPASS = "MQTTPassword";	
	
	private static Properties prop = new Properties();
	private InputStream input = null;

	public Config() {
		try {
			input = new FileInputStream(configFile);
			prop.load(input);
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
			
		} finally {
			if(input != null) {
				try {
					input.close();
				}
				catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String getProperty(String key) {
		if(prop == null) {
			new Config();
		}
		return prop.getProperty(key);
	}
	
	public static String getFilename() {
		return configFile;
	}
	
	public static String getMqttCmdTopic() {
		if(getProperty(MQTTCLIENTID) == null) {
			return null;
		}
		else {
			return getProperty(MQTTCLIENTID) + "/cmd/#";
		}
		
	}
}
