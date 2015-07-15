package kronos.test;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class SimulationThread extends Thread {
	public void run(){
		File batch = new File("sim/startSim.bat");
		try {
			Desktop.getDesktop().open(batch);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[]args){
		(new SimulationThread()).start();
	}
}
