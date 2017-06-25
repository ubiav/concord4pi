package concord4pi.logs;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import concord4pi.Config;

public class LogEngine {
	private static Logger LOG;
	
	public static void Log(Level level, String message, String className) {
		if(LOG == null) {
			setupLogger();
		}
		LOG.log(level, className + ":" + message);
	}
	
	private static void setupLogger() {
		FileInputStream input = null;
		try {
			LOG = Logger.getLogger("concord4pi");
			LOG.setLevel(Level.ALL);
			input = new FileInputStream(Config.getFilename());
		    LogEngine.Log(Level.INFO, "Loading Configuration for LogEngine", "LogEngine");
			LogManager.getLogManager().readConfiguration(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
					LogEngine.Log(Level.INFO, "Logger Started", "LogEngine");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
}
