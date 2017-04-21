package concord4pi;


public class concord4pi {
	AlarmSystem alarmSystem;
	Config configuration;
	LogEngine LOG;
	API apiEngine; 
	
	public static void main(String[] args) {
		concord4pi program = new concord4pi();
		program.go();
	}

	public concord4pi() {
		LOG = new LogEngine();
		configuration = new Config();
		alarmSystem = new AlarmSystem();
		apiEngine = new API(alarmSystem.getTxQueue());
	}
	
	public void go() {
		alarmSystem.start();
		alarmSystem.shutdown();
	}
	
}
