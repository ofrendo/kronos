package kronos.test;

import org.junit.Test;

import kronos.model.ERPData;
import kronos.model.SAData;
import kronos.sim.CEventProcessor;

/**
 * There is no useful test yet because of missing functionality!!!
 * @author Matthias
 *
 */

public class TestCEventProcessor {
	
	@Test
	public void test(){
		CEventProcessor processor = new CEventProcessor();
		processor.init();
		ERPData erpData = new ERPData();
		erpData.setCustomerNumber(10);
		processor.sendEvent(erpData);
		ERPData erpData2 = new ERPData();
		erpData2.setCustomerNumber(20);
		processor.sendEvent(erpData2);
		SAData saData = new SAData();
		saData.setOverallStatus("OK");
		processor.sendEvent(saData);
	}
}
