package concord4pi.API;

import java.io.IOException;
import java.util.Queue;
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;

import concord4pi.SB2000.Message;
import concord4pi.SB2000.State;
import concord4pi.logs.LogEngine;


public class ZoneContext extends BaseContext {

	public ZoneContext(String contextPath, State alarmState, Queue<Message> txQueue) {
		super(contextPath, alarmState, txQueue);
		this.setHandler(new ZoneRequestHandler());
	}
	
	private class ZoneRequestHandler extends RequestHandler {

		public ZoneRequestHandler() {
			super();
		}
		
		@Override
		public void processGETRequest(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
			try {
			
				String zoneID = target.substring(1);
				response.setContentType("text/html; charset=utf-8");
				if(getAlarmSystemState().getExistingZone(zoneID) != null) {
					response.setStatus(HttpServletResponse.SC_OK);
					response.getWriter().println("{" + getAlarmSystemState().getExistingZone(zoneID).toString() + "}");
				}
				else {
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					response.getWriter().println("{ }");
				}
				baseRequest.setHandled(true);
			}
			catch (IOException e) {
				LogEngine.Log(Level.WARNING, e.getMessage(), this.getClass().getName());
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				baseRequest.setHandled(true);
			}
		}

	}
}
