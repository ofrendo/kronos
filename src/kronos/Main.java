package kronos;

import kronos.server.HTTPServer;
import kronos.server.ws.WSServer;
import kronos.sim.ConnectionHandler;
import kronos.sim.ProductHandler;
import kronos.sim.Simulation;

/**
 * This class serves as the entrance point for starting the sim, 
 * the connections to the sim, the WebSocket server and the HTTP
 * server.
 * @author D059373
 *
 */
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
