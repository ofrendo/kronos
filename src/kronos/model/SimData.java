package kronos.model;

import kronos.fsm.Triggers;
import kronos.sim.source.SimSourceType;

public abstract class SimData {
	
	public abstract Triggers getTrigger();
	
	/**
	 * Check which type of data a certain event is.
	 * @param simData A subclass of SimData
	 * @return An item of SimSourceType
	 */
	public static SimSourceType getType(SimData simData) {
		if (simData instanceof ERPData) {
			return SimSourceType.erpData;
		}
		else if (simData instanceof OPCDataItem) {
			return SimSourceType.machineData;
		}
		else {
			return SimSourceType.saData;
		}
	}
	
}
