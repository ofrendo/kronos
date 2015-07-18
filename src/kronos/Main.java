package kronos;

import kronos.server.HTTPServer;
import kronos.server.WSServer;
import kronos.sim.ConnectionHandler;
import kronos.sim.ProductHandler;
import kronos.sim.Simulation;

public class Main {
	
	public static void main(String[] args) {
		Simulation sim = new Simulation();
		sim.start();
		
		ConnectionHandler connectionHandler = new ConnectionHandler();
		connectionHandler.start();
		
		WSServer wsServer = new WSServer();
		wsServer.start();
		
		ProductHandler.getProductHandler().setWSListener(wsServer);
		
		HTTPServer httpServer = new HTTPServer();
		httpServer.run();
	}
	
}
