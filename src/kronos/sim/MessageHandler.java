package kronos.sim;

import java.io.StringReader;
import java.util.Observable;
import java.util.Observer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import kronos.model.ERPData;
import kronos.model.OPCDataItem;
import kronos.model.SAData;
import kronos.util.Log;

import com.google.gson.Gson;

public class MessageHandler implements Observer {
	
	protected MessageHandler() {}
	

	
	protected synchronized void onMessage(String text, SimSourceType type) {
		//Use something like this in observers
		//SimSource source = (SimSource) o;
		//switch (source.getType()) {
		//case erpData:
		//}

		
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
		try {	
			JAXBContext _ctx = JAXBContext.newInstance(ERPData.class);
			Unmarshaller _unmarshaller = _ctx.createUnmarshaller();
			
			StringReader sReader = new StringReader(xml); 
			ERPData erpData2 = (ERPData) _unmarshaller.unmarshal(sReader);
			
			
			Log.info("###########ERPItem#############");
			Log.info("Material: " + erpData2.getMaterialNumber());
			Log.info("Customer: "+ erpData2.getCustomerNumber());
			Log.info("Order: " + erpData2.getOrderNumber());
			Log.info("Timestamp: " + erpData2.getTimeStamp());
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
	}
	
	public void handleOPC(String xml){
		try {	
			JAXBContext _ctx = JAXBContext.newInstance(OPCDataItem.class);
			Unmarshaller _unmarshaller = _ctx.createUnmarshaller();
			
			StringReader sReader = new StringReader(xml); 
		
			OPCDataItem dataItem = (OPCDataItem) _unmarshaller.unmarshal(sReader);
			
			
			
			Log.info("##########OPCItem#############");
			
			Log.info("Name: " + dataItem.getItemName());
			Log.info("Status: " + dataItem.getStatus());
			Log.info("Timestamp: " + dataItem.getTimestamp());
			Log.info("Value: " + dataItem.getValue());
			//Log.info("Timestamp: " + erpData2.getTimeStamp());
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	public void handleSA(String json){
		Gson gson = new Gson();

		//String jsonString = gson.toJson(json);
	
		SAData tmpData = gson.fromJson(json, SAData.class);
		
		tmpData.print();
	}
}
