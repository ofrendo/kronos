package kronos.sim;

import java.util.ArrayList;

import javax.swing.plaf.synth.SynthSpinnerUI;

import kronos.db.DB;
import kronos.fsm.StateMachineHandler;
import kronos.model.ERPData;
import kronos.model.OPCDataItem;
import kronos.model.Product;
import kronos.model.SAData;
import kronos.model.SimData;
import kronos.server.WSServer;
import kronos.util.Log;

public class ProductHandler {

	ArrayList<Product> products;
	private static ProductHandler productHandler;
	
	private StateMachineHandler stateMachineHandler;
	private CEventProcessor cEventProcessor;
	private WSServer wsServer;
	
	public static ProductHandler getProductHandler(){
		if (productHandler == null) {
			productHandler = new ProductHandler();
		}
		
		return productHandler;
	}
	
	private ProductHandler () {
		products = new ArrayList<Product>();
		stateMachineHandler = new StateMachineHandler();
		cEventProcessor = new CEventProcessor();
		cEventProcessor.init();
	}
	
	public void setWSListener(WSServer wsServer) {
		this.wsServer = wsServer;
	}
	
	public void createNewProduct (ERPData erpData) {
		Product p = new Product(erpData, stateMachineHandler.createStateMachine());
		products.add(p);
		// System.out.println("new product in Queue");
		
		sendToWS(p, erpData);
	}
	
	public void fireSAEvent(SAData saData) {
		Product p = this.fireEvent(saData);
		p.setSAData(saData);
		
		// Notify DB that product is finished
		try {
			DB.getDB().insertIntoDB(p);
		} catch (Exception e) {
			Log.error(e.getMessage());
		}
		
		// Notify EsperTech
		
		// Notify WS
		sendToWS(p, saData);
	}
	public void fireOPCEvent(OPCDataItem opcDataItem) {
		Product p = this.fireEvent(opcDataItem);
		p.addSimData(opcDataItem);
		
		// Notify EsperTech
		
		// Notify WS
		sendToWS(p, opcDataItem);
	}
	
	public Product fireEvent(SimData simData) {
		Product result = null;
		for (Product p : products) {
			if (p.canFire(simData)) {
				p.fireEvent(simData);
				result = p;
				break;
			}
		}
		// System.out.println(result.getState());

		
		return result;
	}
	
	
	public void sendToWS(Product p, SimData simData) {
		if (wsServer != null) {
			wsServer.onSimData(p, simData);
		}
	}	
	
	
}
