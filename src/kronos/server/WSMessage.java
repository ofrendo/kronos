package kronos.server;

import kronos.fsm.PartStates;
import kronos.model.SimData;
import kronos.sim.source.SimSourceType;

public class WSMessage {
	public String orderNumber;
	public SimSourceType type;
	public PartStates state;
	public SimData simData;
	
	public WSMessage(String orderNumber, 
			SimSourceType type, 
			PartStates state,
			SimData simData) {
		this.orderNumber = orderNumber;
		this.type = type;
		this.state = state;
		this.simData = simData;
	}
}
