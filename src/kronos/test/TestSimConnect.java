package kronos.test;

import static org.junit.Assert.fail;

import javax.jms.Connection;
import javax.jms.JMSException;

import kronos.sim.ConnectionHandler;

import org.junit.Test;

public class TestSimConnect {

	@Test
	public void testConnect() {
		ConnectionHandler connectionHandler = new ConnectionHandler();
		
		try {
			Connection connection = connectionHandler.doConnect();
			connection.close();
		} catch (JMSException e) {
			fail("Failed to connect to sim.");
			e.printStackTrace();
		}
		
	}

}
