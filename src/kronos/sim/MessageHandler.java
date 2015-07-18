package kronos.sim;

import java.util.Observable;
import java.util.Observer;

import kronos.model.ERPData;
import kronos.model.OPCDataItem;
import kronos.model.SAData;
import kronos.model.SimData;
import kronos.model.SimDataFactory;
import kronos.sim.source.SimSource;
import kronos.sim.source.SimSourceType;

public class MessageHandler implements Observer {
	
	protected MessageHandler() {
		products = ProductHandler.getProductHandler();
	}
	
	private ProductHandler products;
	
	
	
	protected synchronized void onMessage(String text, SimSourceType type) {
		switch (type) {
		case erpData:
			handleERP(text);
			break;
		case machineData:
			handleOPC(text);
			break;
		case saData:
			handleSA(text);
			break;
		default:
			break;
		}
		
		
	}

	public void update(Observable o, Object arg) {
		String text = (String) arg;
		onMessage(text, ((SimSource) o).getType());
	}
	
	public void handleERP(String xml){
		//TODO Matthias
		ERPData erpData = SimDataFactory.createERPData(xml);
		products.createNewProduct(erpData);
		
		TEST_sendToWS(erpData);
	}
	
	public void handleOPC(String xml){
		//TODO Matthias
		OPCDataItem opcDataItem = SimDataFactory.createOPCDataItem(xml);
		
		TEST_sendToWS(opcDataItem);
	}
	
	public void handleSA(String json){
		//TODO Matthias
		SAData saData = SimDataFactory.createSAData(json);
		
		TEST_sendToWS(saData);
	}
	
	private void TEST_sendToWS(SimData simData) {
		ProductHandler.getProductHandler().TEST_onSimData(simData);
	}
}
