package kronos.sim;

import java.util.Observable;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import kronos.util.Log;

public class MessageListener extends Observable implements Runnable {
	
	/**
	 * JMS doing the actual listening to the topic
	 */
	private MessageConsumer messageConsumer;
	
	
	/**
	 * Listens to a certain topic in a separate thread
	 * @param session
	 * @param topic
	 * @throws JMSException
	 */
	public MessageListener(Session session, String topic) throws JMSException {
		//Create destination from topic and messageconumser
		Destination destination = session.createTopic(topic);
		this.messageConsumer = session.createConsumer(destination);
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				// Wait for next message to arrive
				TextMessage message = (TextMessage) this.messageConsumer.receive();
				String text = message.getText();
				
				// Send message to observers
				this.notifyObservers(text);	
			} catch (JMSException e) {
				Log.error(e.getMessage());
			}
		}
	}
	
}
