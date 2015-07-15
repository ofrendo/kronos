package kronos.sim;

import java.util.Observable;
import java.util.Observer;


public class MessageHandler implements Observer {
	
	protected MessageHandler() {}
	
	protected synchronized void onMessage(String text) {
		
	}

	@Override
	public void update(Observable o, Object arg) {
		System.out.println("o: " + o);
		System.out.println("arg: " + arg);
	}
	
}
