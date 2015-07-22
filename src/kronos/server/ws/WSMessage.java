package kronos.server.ws;

import kronos.fsm.PartStates;
import kronos.model.SimData;
import kronos.sim.source.SimSourceType;

public class WSMessage {
	public String orderNumber;
	public SimSourceType type;
	public PartStates state;
	public SimData simData;
	
	/**
	 * Represents a message to be sent to the frontend 
	 * for real time visualization of a product on the factory line.
	 * It is used to generate a JSON string.
	 * @param orderNumber
	 * @param type
	 * @param state
	 * @param simData
	 */
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
