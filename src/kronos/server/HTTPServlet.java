package kronos.server;

import java.io.File;
import java.io.IOException;

import javaxt.http.servlet.HttpServlet;
import javaxt.http.servlet.HttpServletRequest;
import javaxt.http.servlet.HttpServletResponse;
import javaxt.http.servlet.ServletException;

import kronos.util.Log;

/**
 * See http://www.javaxt.com/javaxt-server/Overview.
 * @author D059373
 *
 */
public class HTTPServlet extends HttpServlet {
	
	private RouteHandler routeHandler;
	
	/**
	 * Servlet for serving files statically from directory /http
	 */
	public HTTPServlet() {
		this.routeHandler = new RouteHandler();		
	}
	
	/**
	 * Processes an HTTP request: 
	 * "/" ==> writes contents of index.html to response
	 * A valid file in /http/ ==> writes that file to response
	 * An invalid file ==> writes a 404 error to response
	 */
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		//Get requested path
        String path = request.getURL().getPath();
        if (path.length()>1 && path.startsWith("/")) path = path.substring(1);
		
        Log.info("HTTPServlet: Recieved request for " + path);
        
        //Peel off the first part of the path. We'll use this to determine whether to serve
        //static or dynamic content.
        String service = path.toLowerCase();
        if (service.contains("/")) service = service.substring(0, service.indexOf("/"));
        
        if (path.equals("/")) {
        	path = "index.html";
        }
        
        //Construct a physical file path using the url
        File file = new File("http/" + path);
        
        //If the file doesn't exist, check REST API or return an error
        if (!file.exists() || file.isDirectory()) {
        	if (routeHandler.handleRoute(path) == false) {
        		response.setStatus(404);
            	Log.info("HTTPServlet: Returning 404");
        	}
        	return;
        }
        else{ //Dump the file content to the servlet output stream
            response.write(file, getContentType(file), true);
            Log.info("HTTPServlet: Returning file contents");
        }
        
	}
	
	/**
	 * Gets the content type from a file name to be used for HTTP
	 * @param file File to be analysed
	 * @return Possible return values: "text/css", "text/javascript", "text/html"
	 */
	public String getContentType(File file) {
		String fileName = file.getName();
		String fileExtension = "";
        if (fileName.contains(".")){
            fileExtension = fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase();
        }
 
        if (fileExtension.equals("css")) return "text/css";
        if (fileExtension.equals("js"))  return "text/javascript";
         return "text/html";
	}	
	
}


























