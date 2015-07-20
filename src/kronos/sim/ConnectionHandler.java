package kronos.sim;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;

import kronos.sim.source.MessageListener;
import kronos.sim.source.SAReader;
import kronos.util.Log;

import org.apache.activemq.ActiveMQConnectionFactory;

public class ConnectionHandler extends Thread {
	
	/**
	 * String to connect to locally running sim server
	 */
	public final String simConnect = "tcp://localhost:61616";
	
	/**
	 * Topic name for ERP data
	 */
	public static final String topicERP = "m_orders";
	
	/**
	 * Topic name for machine data
	 */
	public static final String topicMachineData = "m_opcitems";
	
	/**
	 * Set whether listeners should keep listening to sim
	 */
	private static boolean keepListening = true;
	
	/**
	 * Observer for each sim data source
	 */
	private final MessageHandler messageHandler;
	
	private MessageListener listenerERP;
	private MessageListener listenerMachineData;
	private SAReader saReader;
	
	/**
	 * Main handler for connecting to the sim server
	 */
	public ConnectionHandler() {
		this.messageHandler = new MessageHandler();
		MessageWorker worker = new MessageWorker(messageHandler);
		worker.start();
	}
	
	/**
	 * Returns the observer for each sim data source
	 * @return
	 */
	public MessageHandler getMessageHandler() {
		return messageHandler;
	}
	
	public static void setKeepListening(boolean listen) {
		keepListening = listen;
	}
	public static boolean getKeepListening() {
		return keepListening;
	}
	public MessageListener getListenerERP() {
		return listenerERP;
	}
	public MessageListener getListenerMachineData() {
		return listenerMachineData;
	}
	public SAReader getSAReader() {
		return saReader;
	}
	
	/** 
	 * Creates and starts a connection to the sim
	 * @return
	 * @throws JMSException
	 */
	public Connection doConnect() throws JMSException {
		Log.info("======= Connecting.... =======");
		
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(simConnect);
		
		Connection connection = connectionFactory.createConnection();
		connection.start();
		
		return connection;
	}
	
	public Session doSession(Connection connection) throws JMSException {
		return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	}
	
	/**
	 * Creates three listeners, one for each topic and one for the json
	 * @param session
	 * @throws JMSException
	 */
	public void createListeners(Session session) throws JMSException {
		listenerERP = new MessageListener(session, topicERP);
		listenerERP.addObserver(messageHandler);
		
		listenerMachineData = new MessageListener(session, topicMachineData);
		listenerMachineData.addObserver(messageHandler);
		
		saReader = new SAReader();
		saReader.addObserver(messageHandler);
	}
	/**
	 * Starts all three listeners
	 */
	public void startListeners() {
		Thread thread1 = new Thread(listenerERP);
		thread1.start();
		
		Thread thread2 = new Thread(listenerMachineData);
		thread2.start();
		
		Thread thread3 = new Thread(saReader);
		thread3.start();
	}
	
	
	@Override
	public void run() {
		try {
			// Create and start Connection
			Connection connection = doConnect();
			
			// Create a Session
			Session session = doSession(connection);
			
			// Create and start messagehandlers
			createListeners(session);
			startListeners();
		}
		catch (JMSException e) {
			Log.error(e.getMessage());
		}
		
	}
	
	
}
