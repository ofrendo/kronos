package kronos.test;

import static org.junit.Assert.fail;

import java.util.Observable;
import java.util.Observer;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

import kronos.sim.ConnectionHandler;
import kronos.sim.source.MessageListener;
import kronos.sim.source.SAReader;
import kronos.sim.source.SimSource;
import kronos.util.Log;

import org.junit.Test;

public class TestSimTopics extends TestUsesSim implements Observer {

	boolean erpRecieved = false;
	boolean machineDataRecieved = false;
	boolean saDataRecieved = false;
	
	@Test
	public void testSimTopics() throws JMSException, InterruptedException {
		ConnectionHandler connectionHandler = new ConnectionHandler();
		Connection connection = connectionHandler.doConnect();
		Session session = connectionHandler.doSession(connection);
		connectionHandler.createListeners(session);

		MessageListener listenerERP = connectionHandler.getListenerERP();
		listenerERP.addObserver(this);
		
		MessageListener listenerMachineData = connectionHandler.getListenerMachineData();
		listenerMachineData.addObserver(this);
		
		SAReader saReader = connectionHandler.getSAReader();
		saReader.addObserver(this);
		
		
		(new Thread(listenerERP)).start();
		(new Thread(listenerMachineData)).start();
		(new Thread(saReader)).start();
		
		Thread.sleep(120000);
		
		if (erpRecieved == false) {
			fail("Didn't recieve erpData");
		}
		if (machineDataRecieved == false) {
			fail("Didn't recieve machineData");
		}
		if (saDataRecieved == false) {
			fail("Didn't recieve SAData");
		}
	}

	public void update(Observable o, Object arg) {
		SimSource s = (SimSource) o;
		switch (s.getType()) {
		case erpData:
			erpRecieved = true;
			break;
		case machineData:
			machineDataRecieved = true;
			break;
		case saData:
			saDataRecieved = true;
			Log.info("_________ TEST: Recieved erpData");
			break;
		}
		
		//String message = (String) arg;
		//Log.info(message);
	}

}
