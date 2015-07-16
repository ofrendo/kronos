package kronos;

import kronos.server.WSServer;

public class Main {
	
	public static void main(String[] args) {
		WSServer server = new WSServer();
		server.run();
	}
	
}
