package kronos;

import kronos.server.HTTPServer;
import kronos.server.WSServer;

public class Main {
	
	public static void main(String[] args) {
		WSServer wsServer = new WSServer();
		wsServer.start();

		HTTPServer httpServer = new HTTPServer();
		httpServer.run();
	}
	
}
