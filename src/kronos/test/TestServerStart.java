package kronos.test;

import kronos.server.HTTPServer;
import kronos.server.WSServer;

import org.junit.Test;

public class TestServerStart {

	@Test
	public void testServerStart() {
		HTTPServer httpServer = new HTTPServer();
		httpServer.run();
		
		WSServer wsServer = new WSServer();
		wsServer.run();
	}

}
