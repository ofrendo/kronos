package kronos.model;

import kronos.sim.source.SimSourceType;

public abstract class SimData {
	
	public String toJSONString() {
		//TODO
		return "";
	}
	
	public String getTrigger(){
		return "";
		
	}
	
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
