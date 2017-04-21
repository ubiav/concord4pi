package concord4pi.tests;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import concord4pi.SB2000.Message;

public class SB2000Message_Test {

	private static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		setUpLogger();
		
		Message newMessage = new Message(0x0a);
		if(newMessage.validCheckSum()) {
			System.out.println("Message Checksum Valid");
		}
		else {
			System.out.println("Message Checksum INVALID");
		}
		
		System.out.println(newMessage);
		
		newMessage = new Message("02", "020405");
		System.out.println(newMessage);	
	}
	
	private static void setUpLogger() {
		LOGGER.setLevel(Level.INFO);
		ConsoleHandler handler = new ConsoleHandler();
		handler.setFormatter(new SimpleFormatter());
		handler.setLevel(Level.ALL);
		LOGGER.addHandler(handler);
	}

}
