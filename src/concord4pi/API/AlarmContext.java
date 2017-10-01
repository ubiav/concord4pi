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


public class AlarmContext extends BaseContext {

	public AlarmContext(String contextPath, State alarmState, Queue<Message> txQueue) {
		super(contextPath, alarmState, txQueue);
		this.setHandler(new AlarmRequestHandler());
	}
	
	private class AlarmRequestHandler extends RequestHandler {

		public AlarmRequestHandler() {
			super();
		}
		
		@Override
		public void processGETRequest(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
			try {
				response.setContentType("text/html; charset=utf-8");
				response.setStatus(HttpServletResponse.SC_OK);
		        response.getWriter().println("{" + getAlarmSystemState() + "}");
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
