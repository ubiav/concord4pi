package concord4pi;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
	private final static String configFile = "config.properties";
	
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
}
