package kronos.test;

import java.util.Observable;
import java.util.Observer;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

import kronos.sim.ConnectionHandler;
import kronos.sim.MessageHandler;

import org.junit.Test;

public class TestSimTopics implements Observer {

	String erpRecieved = null;
	String machineDataRecieved = null;
	
	@Test
	public void test() throws JMSException {
		ConnectionHandler connectionHandler = new ConnectionHandler();
		Connection connection = connectionHandler.doConnect();
		Session session = connectionHandler.doSession(connection);
		connectionHandler.createListeners(session);
		
		MessageListenerERP listenerERP = connectionHandler.getListenerERP();
		
		
		MessageHandler handler = connectionHandler.getMessageHandler();
		handler.addObserver(this);
		
	}

	@Override
	public void update(Observable o, Object arg) {
		String message = (String) arg;
	}

}
