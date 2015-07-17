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
import kronos.model.SimDataFactory;
import kronos.util.Log;

import com.google.gson.Gson;

public class MessageHandler implements Observer {
	
	protected MessageHandler() {}
	
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
		
	}
	
	public void handleOPC(String xml){
		//TODO Matthias
		OPCDataItem opcDataItem = SimDataFactory.createOPCDataItem(xml);
		
	}
	
	public void handleSA(String json){
		//TODO Matthias
		SAData saData = SimDataFactory.createSAData(json);
		
	}
}
