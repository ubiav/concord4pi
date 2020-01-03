package concord4pi;

import concord4pi.config.*;
import concord4pi.logging.*;

public class concord4pi {

	private MainController masterControl;
	
	public static void main(final String[] args) {
		final concord4pi program = new concord4pi();
		program.go();
	}

	public concord4pi() {
		Config appConfiguration;
		LogEngine logger;
		
		//Get the application config & verify
		appConfiguration = new Config();
		if(!appConfiguration.valid()) {
			System.out.println("The config file could not be found or is not configured correctly.");
			System.out.println("Please fix your config file (./concord4pi.config) and retry.");
			System.exit(0);
		}
		else {	
			//Set up the Logger
			logger = new LogEngine(appConfiguration);
			masterControl = new MainController(appConfiguration, logger);
		}
	}
	
	public void go() {
		masterControl.start();
		masterControl.shutdown();
	}
	
	
}
