package kronos.sim;

import java.io.IOException;

import kronos.util.Log;

public class Simulation extends Thread {
	
	Process p;
	
	@Override
	public void run() {
		Log.info("Simulation: Starting sim...");
		Runtime rt = Runtime.getRuntime();
		try {
			rt.exec("CMD /C start sim\\startSim.bat");
			
		} catch (IOException e) {
			Log.error(e.getMessage());
			e.printStackTrace();
			try {
				p = rt.exec("./sim/startSim.sh");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void stopSim() {
		Log.info("Simulation: Stopping sim...");
		try {
			p.destroyForcibly();
			Runtime.getRuntime().exec("taskkill /im cmd.exe");
		} catch (Exception e) {
			Log.error("Error killing cmd: " + e.getMessage());
			
		}
	}
	
}
