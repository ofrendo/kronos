package kronos.sim;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import kronos.sim.source.SimSource;

public class MessageHandler implements Observer {
	
	private ArrayList<MessageObject> messageQueue;
	
	public MessageHandler(){
		messageQueue = new ArrayList<MessageObject>();
	}
	
	public synchronized void update(Observable o, Object arg) {
		String text = (String) arg;
		
		// Add to queue
		messageQueue.add(new MessageObject(text, ((SimSource) o).getType()));
	}
	public boolean hasNextMessage() {
		return (messageQueue.size() > 0);
	}
	public MessageObject getNextMessage (){
		MessageObject result = messageQueue.get(0);
		messageQueue.remove(0);
		return result;
	}

	
	
}
