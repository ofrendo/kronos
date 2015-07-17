package kronos.model;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.google.gson.Gson;

import kronos.util.Log;

public class SimDataFactory {
	
	public static ERPData createERPData(String xml) {
		ERPData result = null;
		try {	
			JAXBContext _ctx = JAXBContext.newInstance(ERPData.class);
			Unmarshaller _unmarshaller = _ctx.createUnmarshaller();
			
			StringReader sReader = new StringReader(xml); 
			result = (ERPData) _unmarshaller.unmarshal(sReader);
			
			
			Log.info("###########ERPItem#############");
			Log.info("Material: " + result.getMaterialNumber());
			Log.info("Customer: "+ result.getCustomerNumber());
			Log.info("Order: " + result.getOrderNumber());
			Log.info("Timestamp: " + result.getTimeStamp());
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static OPCDataItem createOPCDataItem(String xml) {
		OPCDataItem result = null;
		try {	
			JAXBContext _ctx = JAXBContext.newInstance(OPCDataItem.class);
			Unmarshaller _unmarshaller = _ctx.createUnmarshaller();
			
			StringReader sReader = new StringReader(xml); 
		
			result = (OPCDataItem) _unmarshaller.unmarshal(sReader);
			
			
			
			Log.info("##########OPCItem#############");
			
			Log.info("Name: " + result.getItemName());
			Log.info("Status: " + result.getStatus());
			Log.info("Timestamp: " + result.getTimestamp());
			Log.info("Value: " + result.getValue());
			//Log.info("Timestamp: " + erpData2.getTimeStamp());
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static SAData createSAData(String json) {
		Gson gson = new Gson();
		SAData result = gson.fromJson(json, SAData.class);

		Log.info("######SAItem########");
		Log.info("Status: "+ result.getOverallStatus());
		Log.info("Start: " + result.getTs_start());
		Log.info("Stop: " + result.getTs_stop());
		
		return result;
	}
	
}
