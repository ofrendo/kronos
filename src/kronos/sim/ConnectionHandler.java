package kronos.sim;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

public class ConnectionHandler extends Thread {
	
	private final String simConnect = "tcp://localhost:61616";
	private final String topicERP = "m_orders";
	private final String topicMachineData = "m_opcitems";
	
	@Override
	public void run() {
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory (simConnect);
		
		try {
			// Create a Connection
			Connection connection = connectionFactory.createConnection();
			connection.start();
			
			connection.setExceptionListener(new ExceptionHandler());
			
			// Create a Session
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			// Create the destination (Topic or Queue)
			Destination destinationERP = session.createTopic(topicERP);
			Destination destinationMachineData = session.createTopic(topicMachineData);
			
			
		}
		catch (JMSException e) {
			
		}
		
	}
	
	
}
