package concord4pi.API;

import java.io.IOException;
import java.util.Queue;
import java.util.logging.Level;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;

import concord4pi.SB2000.Constants;
import concord4pi.SB2000.Message;
import concord4pi.SB2000.State;
import concord4pi.logs.LogEngine;


public class PartitionAreaContext extends BaseContext {

	public PartitionAreaContext(String contextPath, State alarmState, Queue<Message> txQueue) {
		super(contextPath, alarmState, txQueue);
		this.setHandler(new PARequestHandler());
	}
	
	private class PARequestHandler extends RequestHandler {

		public PARequestHandler() {
			super();
		}
		
		@Override
		public void processGETRequest(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
			try {
			
				String partitionAreaID = target.substring(1);
				response.setContentType("text/html; charset=utf-8");
				if(getAlarmSystemState().getExistingPartitionArea(partitionAreaID) != null) {
					response.setStatus(HttpServletResponse.SC_OK);
			        response.getWriter().println("{" + getAlarmSystemState().getExistingPartitionArea(partitionAreaID).toString() + "}");
				}
				else {
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					response.getWriter().println("{ }");
				}
			}
			catch (IOException e) {
				LogEngine.Log(Level.WARNING, e.getMessage(), this.getClass().getName());
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
			baseRequest.setHandled(true);
		}
		
		@Override
		public void processPOSTRequest(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
			Message theMessage;
			
			try {
				String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
				String[] targetParts = target.split("/");
				String action = targetParts[2];
				String partitionID;
				String areaID;
				
				//set up the right area IDs based on how the API was called
				if(getContextPath() == "area") {
					partitionID = "00";
					areaID = targetParts[1];
				}
				else {
					partitionID = targetParts[1];
					areaID = "00";
				}
				
				switch(action) {
				
					case "arm" :
						//this is an arm request
						response.setStatus(HttpServletResponse.SC_OK);
						theMessage = new Message(Constants.MESSAGECOMMAND_KEYPRESS, 
								partitionID + 
								areaID + 
								Constants.KEYPRESS_KEYFOBARM
						);
						addMessageToTXQueue(theMessage, this);
						break;
					
					case "disarm" :
						//this is a arm request
						response.setStatus(HttpServletResponse.SC_OK);
						theMessage = new Message(Constants.MESSAGECOMMAND_KEYPRESS, 
								partitionID + 
								areaID + 
								Constants.KEYPRESS_KEYFOBDISARM
						);
						addMessageToTXQueue(theMessage, this);
						break;
						
					case "keypress" :
						//this is an keypress request
						response.setStatus(HttpServletResponse.SC_OK);
						response.getWriter().println(Constants.MESSAGECOMMAND_KEYPRESS + partitionID + areaID + requestBody);
						theMessage = new Message(Constants.MESSAGECOMMAND_KEYPRESS, 
								partitionID + 
								areaID + 
								requestBody
						);
						addMessageToTXQueue(theMessage, this);
						break;
						
					default:
						response.setStatus(HttpServletResponse.SC_NOT_FOUND);
						response.getWriter().println("NOT FOUND");
						response.getWriter().println("Target: " + target);
						response.getWriter().println("ID: " + partitionID + "|" + areaID);
						response.getWriter().println("Action: " + action);
						break;
				
				
				}
			}
			catch (IOException e) {
				LogEngine.Log(Level.WARNING, "Could not handle request: " + request, getClass().getName());
			}
			baseRequest.setHandled(true);
		}
		
	}
}
