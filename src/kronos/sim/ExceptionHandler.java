package kronos.sim;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;

import kronos.util.Log;

public class ExceptionHandler implements ExceptionListener {

	@Override
	public void onException(JMSException arg0) {
		Log.error("Error: " + arg0.getMessage());
	}

}
