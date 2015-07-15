package kronos.test;

import static org.junit.Assert.fail;

import java.util.Observable;
import java.util.Observer;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

import kronos.sim.ConnectionHandler;
import kronos.sim.MessageHandler;
import kronos.sim.MessageListener;

import org.junit.Test;

public class TestSimTopics extends TestUsesSim implements Observer {

	boolean erpRecieved = false;
	boolean machineDataRecieved = false;
	
	@Test
	public void test() throws JMSException, InterruptedException {
		ConnectionHandler connectionHandler = new ConnectionHandler();
		Connection connection = connectionHandler.doConnect();
		Session session = connectionHandler.doSession(connection);
		connectionHandler.createListeners(session);

		MessageHandler handler = connectionHandler.getMessageHandler();
		
		MessageListener listenerERP = connectionHandler.getListenerERP();
		listenerERP.addObserver(this);
		listenerERP.addObserver(handler);
		
		MessageListener listenerMachineData = connectionHandler.getListenerMachineData();
		listenerMachineData.addObserver(this);
		listenerMachineData.addObserver(handler);
		
		(new Thread(listenerERP)).start();
		(new Thread(listenerMachineData)).start();
		
		Thread.sleep(10000);
		
		if (erpRecieved == false) {
			fail("Didn't recieve erpData");
		}
		if (machineDataRecieved == false) {
			fail("Didn't recieve machineData");
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		MessageListener listener = (MessageListener) o;
		if (listener.getTopic().equals(ConnectionHandler.topicERP)) {
			erpRecieved = true;
		}
		else if (listener.getTopic().equals(ConnectionHandler.topicMachineData)) {
			machineDataRecieved = true;
		}
		
		//String message = (String) arg;
		//Log.info(message);
	}

}
