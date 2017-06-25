package concord4pi.API;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ContextHandler;


public class ZoneContext extends ContextHandler {

	
	public ZoneContext(String contextPath) {
		super(contextPath);
		this.setContextPath(contextPath);
		this.setHandler(new RequestHandler());
	}
	
	private class RequestHandler extends AbstractHandler {

		public RequestHandler() {
			super();
		}
		
		public void handle( String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException {
			response.setContentType("text/html; charset=utf-8");
			response.setStatus(HttpServletResponse.SC_OK);		
	        response.getWriter().println("Zone");
			baseRequest.setHandled(true);
		}

	}
}