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


public class RootContext extends BaseContext {

	
	public RootContext(String contextPath, State alarmState, Queue<Message> txQueue) {
		super(contextPath, alarmState, txQueue);
		this.setHandler(new RootRequestHandler());
	}
	
	private class RootRequestHandler extends RequestHandler {

		public RootRequestHandler() {
			super();
		}
		
		@Override
		public void processGETRequest(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
			try {
				response.setContentType("text/html; charset=utf-8");
				response.setStatus(HttpServletResponse.SC_OK);		
		        response.getWriter().println("concord4pi API OK");
				baseRequest.setHandled(true);
			}
			catch (IOException e) {
				LogEngine.Log(Level.WARNING, e.getMessage(), this.getClass().getName());
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				baseRequest.setHandled(true);
			}
		}

		@Override
		public void processPOSTRequest(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
			try {
				response.setContentType("text/html; charset=utf-8");
				response.setStatus(HttpServletResponse.SC_OK);		
		        response.getWriter().println("concord4pi API POST OK");
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
