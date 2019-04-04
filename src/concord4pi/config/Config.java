package concord4pi.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
	
	private static Properties prop;
	private static final String configFileName = "config/concord4pi.config";

	public Config() {
		prop = new Properties();
		setupConfigReader();
	}
	
	private void setupConfigReader() {
		InputStream configFileStream = null;
		try {
			configFileStream = new FileInputStream(configFileName);
			prop.load(configFileStream);
			configFileStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			
		} finally {
			if(configFileStream != null) {
				try {
					configFileStream.close();
				}
				catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public String getSerialDeviceName() {
		return getProperty("SerialDevice");
	}
	
	public String getLogFilename() {
		return getProperty("Log_Filename");
	}
	
	public int getDefaultLogLevel() {
		return getPropertyInt("Log_DefaultLevel");
	}
	
	public int getPropertyInt(String key) {
		return Integer.parseInt(prop.getProperty(key));
	}
	
	public String getProperty(String key) {
		return prop.getProperty(key);
	}
	
	public boolean valid() {
		boolean isValid = true;
		
		//validity checks here
		if(getSerialDeviceName().isEmpty()) { isValid = false; }
		
		return isValid;
	}
}
