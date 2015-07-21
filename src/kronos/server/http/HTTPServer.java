package kronos.server.http;

import kronos.util.Log;


public class HTTPServer extends javaxt.http.Server {
	
	private static final int port = 9002;
	
	/**
	 * Used for starting an HTTP server to serve static
	 * files from port 9002 and directory /http.
	 */
	public HTTPServer() {
		super(port, 100, new HTTPServlet());
	}
	
	/**
	 * Startes the HTTP server.
	 */
	@Override
	public void run() {
		Log.info("HTTPServer: Starting http server on " + port + "...");
		super.run();
	}
	
}
