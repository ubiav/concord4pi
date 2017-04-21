package concord4pi;

import java.util.Queue;

import concord4pi.SB2000.Message;

public class API {

	Queue<Message> txQueue;
	
	public API(Queue<Message> tx) {
		txQueue = tx;
		
	}
	
}
