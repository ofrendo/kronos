package kronos.sim;

import java.util.Observable;
import java.util.Observer;


public class MessageHandler extends Observable implements Observer {
	
	protected MessageHandler() {}
	
	protected synchronized void onMessage(String text) {
		//Use something like this in observers
		//SimSource source = (SimSource) o;
		//switch (source.getType()) {
		//case erpData:
		//}
		this.notifyObservers(text);
	}

	@Override
	public void update(Observable o, Object arg) {
		String text = (String) arg;
		onMessage(text);
	}

	
}
