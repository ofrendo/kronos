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
	
	/**
	 * A product represents one item on the factory line. 
	 * @param erpData Metadata to refer to the product
	 * @param stateMachine Linear sequence of states the product can be in
	 */
	public Product(ERPData erpData, StateMachine<PartStates, Triggers> stateMachine) {
		this.erpData = erpData;
		this.stateMachine = stateMachine;
		this.opcDataItems = new ArrayList<OPCDataItem>();
	}
	
	/**
	 * Adds one opc event to the product.
	 * @param opcDataItem Event to be added
	 */
	public void addSimData(OPCDataItem opcDataItem) {
		opcDataItems.add(opcDataItem);
	}
	/**
	 * Returns the list of events added to this specific product.
	 * @return Current list of opc events
	 */
	public ArrayList<OPCDataItem> getOPCDataItems() {
		return opcDataItems;
	}
	
	/**
	 * Assigns spectral analysis data to a product. 
	 * @param saData The spectral analysis data
	 */
	public void setSAData(SAData saData) {
		this.saData = saData;
	}
	/**
	 * Returns the spectral analysis assigned to the product.
	 * @return The spectral analysis data
	 */
	public SAData getSAData() {
		return saData;
	}
	
	/**
	 * Fires any event on a product, thus also setting the new
	 * state. 
	 * @param simData A subclass of simData
	 */
	public void fireEvent(SimData simData) {
		stateMachine.fire(simData.getTrigger());
	}
	/**
	 * Check if a certain event can be fired on a product.
	 * @param simData A subclass of simData
	 * @return True for can fire, false for can't fire
	 */
	public boolean canFire(SimData simData) {
		return stateMachine.canFire(simData.getTrigger());
	}
	
	/**
	 * Returns the current state of the product.
	 * @return
	 */
	public PartStates getState() {
		return stateMachine.getState();
	}
	
	
}
