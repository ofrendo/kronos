package kronos.server;

import kronos.model.SimData;
import kronos.sim.source.SimSourceType;

public class WSMessage {
	public int productID;
	public SimSourceType type;
	public SimData simData;
	
	public WSMessage(int productID, SimSourceType type, SimData simData) {
		this.productID = productID;
		this.type = type;
		this.simData = simData;
	}
}
