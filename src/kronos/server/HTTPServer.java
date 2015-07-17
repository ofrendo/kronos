package kronos.server;

import kronos.util.Log;


public class HTTPServer extends javaxt.http.Server {
	
	private static final int port = 9002;
	
	public HTTPServer() {
		super(port, 100, new HTTPServlet());
	}
	
	@Override
	public void run() {
		Log.info("HTTPServer: Starting http server on " + port + "...");
		super.run();
	}
	
}
