package kronos.sim;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;

import kronos.util.Log;

import org.apache.activemq.ActiveMQConnectionFactory;

public class ConnectionHandler extends Thread {
	
	public final String simConnect = "tcp://localhost:61616";
	public final String topicERP = "m_orders";
	public final String topicMachineData = "m_opcitems";
	
	/**
	 * Observer for each sim data source
	 */
	private final MessageHandler messageHandler;
	
	public ConnectionHandler() {
		this.messageHandler = new MessageHandler();
	}
	
	/**
	 * Returns the observer for each sim data source
	 * @return
	 */
	public MessageHandler getMessageHandler() {
		return messageHandler;
	}
	
	/** 
	 * Creates and starts a connection to the sim
	 * @return
	 * @throws JMSException
	 */
	public Connection doConnect() throws JMSException {
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory (simConnect);
		
		Connection connection = connectionFactory.createConnection();
		connection.start();
		
		connection.setExceptionListener(new ExceptionHandler());
		
		return connection;
	}
	
	/**
	 * Creates and starts two listeners, one for each topic
	 * @param session
	 * @throws JMSException
	 */
	public void startListeners(Session session) throws JMSException {
		MessageListener handlerERP = new MessageListener(session, topicERP);
		handlerERP.addObserver(messageHandler);
		
		Thread thread1 = new Thread(handlerERP);
		thread1.start();
		
		
		MessageListener handlerMachineData = new MessageListener(session, topicMachineData);
		handlerMachineData.addObserver(messageHandler);
		
		Thread thread2 = new Thread(handlerMachineData);
		thread2.start();
	}
	
	@Override
	public void run() {
		try {
			// Create and start Connection
			Connection connection = doConnect();
			
			// Create a Session
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			// Create and start messagehandlers
			startListeners(session);
		}
		catch (JMSException e) {
			Log.error(e.getMessage());
		}
		
	}
	
	
}
