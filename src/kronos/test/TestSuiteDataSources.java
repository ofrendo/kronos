package kronos.test;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import kronos.util.Log;

import org.apache.activemq.ActiveMQConnectionFactory;

public class TestSuiteDataSources implements ExceptionListener {
	
	public void doStuff() {
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		System.out.println("Current relative path is: " + s);
		
		try {
			Log.info("Trying to connect...");
				
			String conStr = "tcp://localhost:61616";
			ConnectionFactory connectionFactory =
					new ActiveMQConnectionFactory (conStr);
		
			// Create a Connection
			Connection connection = connectionFactory.createConnection();
			connection.start();
		
			connection.setExceptionListener(this);
        
			// Create a Session
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			// Create the destination (Topic or Queue)
			Destination destination = session.createTopic("m_orders");
			
			// Create a MessageConsumer from the Session to the Topic or Queue
			MessageConsumer consumer = session.createConsumer(destination);

	        // Wait for a message
			while(true){
		        Message message = consumer.receive(10000);
		
		        if (message instanceof TextMessage) {
		            TextMessage textMessage = (TextMessage) message;
		            String text = textMessage.getText();
		            System.out.println("Received text: " + text);
		        } else {
		            System.out.println("Received: " + message);
		        }
			}
	    } catch (Exception e) {
	        Log.error("Caught: " + e);
	        e.printStackTrace();
	    }
	}
	
	public static void main(String[] args) {
		(new TestSuiteDataSources()).doStuff();
	}

	public synchronized void onException(JMSException arg0) {
		System.out.println("JMS Exception occured.  Shutting down client.");
	}

}
