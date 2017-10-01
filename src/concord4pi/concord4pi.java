package concord4pi;

import concord4pi.logs.LogEngine;

public class concord4pi {
	AlarmSystem alarmSystem;
	Config configuration;
	LogEngine LOG;
	
	public static void main(String[] args) {
		concord4pi program = new concord4pi();
		program.go();
	}

	public concord4pi() {
		LOG = new LogEngine();
		configuration = new Config();
		alarmSystem = new AlarmSystem();
	}
	
	public void go() {
		alarmSystem.start();
		alarmSystem.shutdown();
	}
	
}
