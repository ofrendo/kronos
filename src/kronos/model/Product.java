package kronos.model;

import java.util.ArrayList;

import kronos.fsm.PartStates;
import kronos.fsm.Triggers;

import com.github.oxo42.stateless4j.StateMachine;

public class Product {

	private ArrayList<SimData> simData;
	private StateMachine<PartStates, Triggers> stateMachine;
	public ERPData erpData;
	
	public Product(ERPData erpData, StateMachine<PartStates, Triggers> stateMachine) {
		this.erpData = erpData;
		this.stateMachine = stateMachine;
	}
	
	public void fireEvent(SimData simData) {
		stateMachine.fire(simData.getTrigger());
	}
	public boolean canFire(SimData simData) {
		return stateMachine.canFire(simData.getTrigger());
	}
	
	public PartStates getState() {
		return stateMachine.getState();
	}

	public ERPData getErpData() {
		return erpData;
	}
	
}
