package kronos.model;

import java.util.ArrayList;

import kronos.fsm.PartStates;
import kronos.fsm.Triggers;

import com.github.oxo42.stateless4j.StateMachine;

public class Product {

	private ArrayList<OPCDataItem> opcDataItems;
	private StateMachine<PartStates, Triggers> stateMachine;
	public ERPData erpData;
	public SAData saData;
	
	public Product(ERPData erpData, StateMachine<PartStates, Triggers> stateMachine) {
		this.erpData = erpData;
		this.stateMachine = stateMachine;
		this.opcDataItems = new ArrayList<OPCDataItem>();
	}
	
	public void addSimData(OPCDataItem opcDataItem) {
		opcDataItems.add(opcDataItem);
	}
	public ArrayList<OPCDataItem> getSimDataItems() {
		return null;
	}
	
	public void setSAData(SAData saData) {
		this.saData = saData;
	}
	public SAData getSAData() {
		return saData;
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
	
	
}
