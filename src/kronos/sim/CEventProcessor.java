package kronos.sim;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

import kronos.model.ERPData;
import kronos.model.OPCDataItem;
import kronos.model.SAData;
import kronos.util.Log;

public class CEventProcessor implements UpdateListener {
	
	private EPServiceProvider service;
	private boolean running = false;
	
	public void init(){
		service = EPServiceProviderManager.getDefaultProvider();
		String expression = "SELECT avg(customerNumber) from kronos.model.ERPData.win:time(10 sec)"; 
		EPStatement statement = service.getEPAdministrator().createEPL(expression);
		statement.addListener(this);
		running = true;
	}

	public void update(EventBean[] arg0, EventBean[] arg1) {
		Log.info("CEventProcessor: Event arrived!");
		Log.info("average costumernumber: " + arg0[0].get("avg(customerNumber)"));
	}
	
	public void sendEvent(ERPData erpData){
		if(!running){
			Log.error("CEventProcessor: Event processing aborted: You have to run the processor before sending any events to it!");
			return;
		}
		service.getEPRuntime().sendEvent(erpData);
	}
	
	public void sendEvent(OPCDataItem opcData){
		if(!running){
			Log.error("CEventProcessor: Event processing aborted: You have to run the processor before sending any events to it!");
			return;
		}
		service.getEPRuntime().sendEvent(opcData);
	}
	
	public void sendEvent(SAData saData){
		if(!running){
			Log.error("CEventProcessor: Event processing aborted: You have to run the processor before sending any events to it!");
			return;
		}
		service.getEPRuntime().sendEvent(saData);
	}
}
