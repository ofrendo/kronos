package kronos.sim;

import java.io.StringReader;
import java.util.Observable;
import java.util.Observer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import kronos.model.ERPData;
import kronos.util.Log;


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
			
			break;
			
		case saData:

			break;
		default:
			break;
		}
		
		
	}

	@Override
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
			
			Log.info("###################################");
			Log.info("Material: " + erpData2.getMaterialNumber());
			Log.info("Customer: "+ erpData2.getCustomerNumber());
			Log.info("Order: " + erpData2.getOrderNumber());
			Log.info("Timestamp: " + erpData2.getTimeStamp());
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		
		
		
	}
}
