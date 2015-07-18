package kronos.sim.source;

import java.util.Observable;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import kronos.sim.ConnectionHandler;
import kronos.util.Log;

public class MessageListener extends Observable implements Runnable, SimSource {
	
	/**
	 * JMS doing the actual listening to the topic
	 */
	private MessageConsumer messageConsumer;
	private int delay = 15000;
	private String topic;
	
	/**
	 * Listens to a certain topic in a separate thread
	 * @param session
	 * @param topic
	 * @throws JMSException
	 */
	public MessageListener(Session session, String topic) throws JMSException {
		Log.info("MessageListener: Constructor on " + topic);
		this.topic = topic;
		
		//Create destination from topic and messageconumser
		Destination destination = session.createTopic(topic);
		this.messageConsumer = session.createConsumer(destination);
	}
	
	public String getTopic() {
		return topic;
	}
	
	public void run() {
		while (ConnectionHandler.getKeepListening() == true) {
			try {
				//Log.info("MessageListener: Starting listen " + topic);
				
				// Wait for next message to arrive
				Message message = this.messageConsumer.receive(delay);
				
				if (message != null)  {
					TextMessage textMessage = (TextMessage) message;
					String text = textMessage.getText();
					//Log.info("MessageListener: " + topic + ": " + text);
					
					// Send message to observers
					this.setChanged();
					this.notifyObservers(text);
				}
			} catch (JMSException e) {
				Log.error(e.getMessage());
			}
		}
	}

	public SimSourceType getType() {
		return (topic.equals(ConnectionHandler.topicERP)) ? 
				SimSourceType.erpData :
				SimSourceType.machineData;
	}
	
}
