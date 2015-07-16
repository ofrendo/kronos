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
	
	/**
	 * Servlet for serving files statically
	 */
	public HTTPServlet() {}
	
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
        
        //Construct a physical file path using the url
        File file = new File("http/" + path);
        
        //If the file doesn't exist, return an error
        if (!file.exists() || file.isDirectory()) {
        	response.setStatus(404);
        	Log.info("HTTPServlet: Returning 404");
            return;
        }
        else{ //Dump the file content to the servlet output stream
            response.write(file, getContentType(file), true);
            Log.info("HTTPServlet: Returning file contents");
        }
        
	}

	public String getContentType(File file){
		 
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


























