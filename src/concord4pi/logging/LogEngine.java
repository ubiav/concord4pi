package concord4pi.logging;

import org.tinylog.*;
//import org.tinylog.configuration.Configuration;
//import org.tinylog.writers.*;

import concord4pi.config.Config;

public class LogEngine {
			
	public LogEngine(Config appConfig) {
		log("Logger Initialized", Level.INFO);
	}
	
	public void log(String message, Level level) {
		String callingMethod = Thread.currentThread().getStackTrace()[2].getClassName() + "." + Thread.currentThread().getStackTrace()[2].getMethodName();
		log(message, level, callingMethod);
	}
	
	private void log(String message, Level level, String callingMethod) {
		if(level.equals(Level.TRACE  )) { Logger.trace(callingMethod + ": " + message);}
		if(level.equals(Level.DEBUG  )) { Logger.debug(callingMethod + ": " + message);}
		if(level.equals(Level.INFO   )) { Logger.info(callingMethod + ": " + message);}
		if(level.equals(Level.WARN)) { Logger.warn(callingMethod + ": " + message);}
		if(level.equals(Level.ERROR  )) { Logger.error(callingMethod + ": " + message);}
	}

	public void logObject(Object messageObject, Level level) {
		String callingMethod = Thread.currentThread().getStackTrace()[2].getClassName() + "." + Thread.currentThread().getStackTrace()[2].getMethodName();
		logObject(messageObject, level, callingMethod);
	}
	
	private void logObject(Object messageObject, Level level, String callingMethod) {
		if(level.equals(Level.TRACE  )) { Logger.trace(callingMethod + ": " + messageObject);}
		if(level.equals(Level.DEBUG  )) { Logger.debug(callingMethod + ": " + messageObject);}
		if(level.equals(Level.INFO   )) { Logger.info(callingMethod + ": " + messageObject);}
		if(level.equals(Level.WARN)) { Logger.warn(callingMethod + ": " + messageObject);}
		if(level.equals(Level.ERROR  )) { Logger.error(callingMethod + ": " + messageObject);}
	}

}

