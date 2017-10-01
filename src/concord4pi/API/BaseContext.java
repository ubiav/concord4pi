package concord4pi.API;

import java.util.Queue;
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;

import concord4pi.Config;
import concord4pi.SB2000.Message;
import concord4pi.SB2000.State;
import concord4pi.logs.LogEngine;

public class BaseContext extends ContextHandler {
	protected State alarmSystemState;
	protected Queue<Message> txQueue;
	
	public BaseContext(String contextPath, State alarmState, Queue<Message> newTxQueue) {
		super(contextPath);
		this.setAlarmSystemState(alarmState);
		this.setTxQueue(newTxQueue);
		this.setContextPath(contextPath);
	}
	
	protected State getAlarmSystemState() {
		return alarmSystemState;
	}

	protected void setAlarmSystemState(State alarmSystemState) {
		this.alarmSystemState = alarmSystemState;
	}

	protected Queue<Message> getTxQueue() {
		return txQueue;
	}

	protected void setTxQueue(Queue<Message> txQueue) {
		this.txQueue = txQueue;
	}

	protected class RequestHandler extends AbstractHandler {

		public RequestHandler() {
			super();
		}
		
		public void handle( String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response ) {
			
			if((request.getHeader(Config.APICLIENTKEY) == null) || !request.getHeader(Config.APICLIENTKEY).equals(Config.getProperty(Config.APICLIENTKEY))) {
				//not authorized to use this API
				//so send to non-authorized methods
				if(request.getMethod().equalsIgnoreCase("GET")) {
					processGETRequest(target, baseRequest, request, response);
				}
				else if (request.getMethod().equalsIgnoreCase("POST")) {
					processPOSTRequest(target, baseRequest, request, response);
				}
				else if (request.getMethod().equalsIgnoreCase("PUT")) {
					processPUTRequest(target, baseRequest, request, response);
				}
				else if (request.getMethod().equalsIgnoreCase("DELETE")) {
					processDELETERequest(target, baseRequest, request, response);
				}
				else {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					baseRequest.setHandled(true);
				}
			}
			else {
			
				if(request.getMethod().equalsIgnoreCase("GET")) {
					processAuthenticatedGETRequest(target, baseRequest, request, response);
				}
				else if (request.getMethod().equalsIgnoreCase("POST")) {
					processAuthenticatedPOSTRequest(target, baseRequest, request, response);
				}
				else if (request.getMethod().equalsIgnoreCase("PUT")) {
					processAuthenticatedPUTRequest(target, baseRequest, request, response);
				}
				else if (request.getMethod().equalsIgnoreCase("DELETE")) {
					processAuthenticatedDELETERequest(target, baseRequest, request, response);
				}
				else {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					baseRequest.setHandled(true);
				}
			}
		}
		
		protected void processGETRequest(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			baseRequest.setHandled(true);
		}

		protected void processPOSTRequest(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			baseRequest.setHandled(true);
		}

		protected void processPUTRequest(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			baseRequest.setHandled(true);
		}

		protected void processDELETERequest(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			baseRequest.setHandled(true);
		}
		
		protected void processAuthenticatedGETRequest(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
			response.setStatus(HttpServletResponse.SC_OK);
			baseRequest.setHandled(true);
		}

		protected void processAuthenticatedPOSTRequest(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
			response.setStatus(HttpServletResponse.SC_OK);
			baseRequest.setHandled(true);
		}

		protected void processAuthenticatedPUTRequest(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
			response.setStatus(HttpServletResponse.SC_OK);
			baseRequest.setHandled(true);
		}

		protected void processAuthenticatedDELETERequest(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
			response.setStatus(HttpServletResponse.SC_OK);
			baseRequest.setHandled(true);
		}
		
		protected void addMessageToTXQueue(Message theMessage, Object caller) {
			LogEngine.Log(Level.FINER, "Adding message " + theMessage.toString() + " to TXQueue", caller.getClass().getName());
			getTxQueue().add(theMessage);
		}

	}
}
