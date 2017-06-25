package concord4pi.API;

import java.util.logging.Level;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

import concord4pi.Config;
import concord4pi.SB2000.State;
import concord4pi.logs.LogEngine;

public class RESTServer {

	private State AlarmState;
	private Server WebServer;
	
	public RESTServer(State AlarmSystemState) {
		AlarmState = AlarmSystemState;
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
		WebServer = new Server(Integer.parseInt(Config.getProperty("API_REST_PORT")));
		
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
					new AlarmContext("/system", AlarmState)
			}
		);

		return contextCollection;
	}
}