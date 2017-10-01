package concord4pi.API;

import java.util.Queue;
import java.util.logging.Level;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

import concord4pi.Config;
import concord4pi.SB2000.Message;
import concord4pi.SB2000.State;
import concord4pi.logs.LogEngine;

public class RESTServer {

	private State AlarmState;
	private Server WebServer;
	private Queue<Message> txQueue;
	
	public RESTServer(State AlarmSystemState, Queue<Message> txQueue) {
		AlarmState = AlarmSystemState;
		this.setTxQueue(txQueue);
	}
	
	public void start() {
		setUpWebServer();
	}
	
	public void stop() {
		try {
			WebServer.stop();
		} catch (Exception e) {
			LogEngine.Log(Level.INFO, "Could not stop API Service!", this.getClass().getName());
		}
	}
	
	private void setUpWebServer() {
		WebServer = new Server(Integer.parseInt(Config.getProperty(Config.APIRESTPORT)));
		
		//set up all the contexts to handle the REST calls
        WebServer.setHandler(getContexts());
		try {
			WebServer.start();
			//WebServer.dumpStdErr();
			//WebServer.join();
		} catch (Exception e) {
			LogEngine.Log(Level.WARNING, "Could not start API Service!", this.getClass().getName());
		}
	}
	
	private Handler getContexts() {
		ContextHandlerCollection contextCollection = new ContextHandlerCollection();
		contextCollection.setHandlers(
			new Handler[] { 
					new RootContext("/", AlarmState, getTxQueue()),
					new AlarmContext("/system", AlarmState, getTxQueue()),
					new PartitionAreaContext("/partitionarea", AlarmState, getTxQueue()),
					new PartitionAreaContext("/partition", AlarmState, getTxQueue()),
					new PartitionAreaContext("/area", AlarmState, getTxQueue()),
					new ZoneContext("/zone", AlarmState, getTxQueue())
			}
		);

		return contextCollection;
	}

	private Queue<Message> getTxQueue() {
		return txQueue;
	}

	private void setTxQueue(Queue<Message> txQueue) {
		this.txQueue = txQueue;
	}
}