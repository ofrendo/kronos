package kronos.test;

import kronos.sim.Simulation;
import kronos.util.Log;

import org.junit.After;
import org.junit.Before;

public abstract class TestUsesSim {

	protected Simulation sim;
	
	@Before
	public void setUp() throws Exception {
		Log.info("____________ TEST: Starting sim... _____________");
		sim = new Simulation();
		sim.start();
		Thread.sleep(2000);
	}

	@After
	public void tearDown() throws Exception {
		Log.info("____________ Test: Stopping sim... _____________");
		sim.stopSim();
	}


}
