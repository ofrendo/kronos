package kronos.sim;

import java.io.IOException;

import kronos.util.Log;

public class Simulation extends Thread {
	
	@Override
	public void run() {
		Log.info("Simulation: Starting sim...");
		try {
			Runtime rt = Runtime.getRuntime();
			rt.exec("CMD /C start sim\\startSim.bat");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void stopSim() {
		Log.info("Simulation: Stopping sim...");
		try {
			Runtime.getRuntime().exec("taskkill /im cmd.exe");
		} catch (IOException e) {
			Log.error("Error killing cmd: " + e.getMessage());
		}
	}
	
}
