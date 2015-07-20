package kronos.sim;

import kronos.model.ERPData;
import kronos.model.OPCDataItem;
import kronos.model.SAData;
import kronos.model.SimDataFactory;
import kronos.sim.source.SimSourceType;

public class MessageWorker extends Thread {
	
	private MessageHandler messageHandler;
	
	public MessageWorker(MessageHandler messageHandler) {
		productHandler = ProductHandler.getProductHandler();
		this.messageHandler = messageHandler;
	}
	
	private ProductHandler productHandler;
	
	@Override
	public void run(){
		while (ConnectionHandler.getKeepListening()){
			if (messageHandler.hasNextMessage()) {
				MessageObject message = messageHandler.getNextMessage();
				handleMessage(message.text, message.type);
			}
			try {
				sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	protected void handleMessage(String text, SimSourceType type) {
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
