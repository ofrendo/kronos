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
		productHandler = ProductHandler.getProductHandler();
	}
	
	private ProductHandler productHandler;
	
	
	
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
		ERPData erpData = SimDataFactory.createERPData(xml);
		productHandler.createNewProduct(erpData);
	}
	
	public void handleOPC(String xml){
		OPCDataItem opcDataItem = SimDataFactory.createOPCDataItem(xml);
		productHandler.fireOPCEvent(opcDataItem);
	}
	
	public void handleSA(String json){
		SAData saData = SimDataFactory.createSAData(json);
		productHandler.fireSAEvent(saData);
	}
	
}
