package kronos.sim;

import java.util.Observable;
import java.util.Observer;


public class MessageHandler extends Observable implements Observer {
	
	protected MessageHandler() {}
	
	protected synchronized void onMessage(String text) {
		this.notifyObservers(text);
	}

	@Override
	public void update(Observable o, Object arg) {
		String text = (String) arg;
		onMessage(text);
		
	}

	
}
