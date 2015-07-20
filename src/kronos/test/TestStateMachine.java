package kronos.test;

import static org.junit.Assert.*;

import java.util.Date;

import kronos.fsm.StateMachineHandler;
import kronos.model.ERPData;
import kronos.model.OPCDataItem;
import kronos.model.Product;

import org.junit.Test;

public class TestStateMachine {
	
	@Test
	public void testStateMachine(){
		
		ERPData erpData = new ERPData(); 
		erpData.setCustomerNumber(4711);
		erpData.setMaterialNumber(42);
		erpData.setOrderNumber("123 ABC 456");
		erpData.setTimeStamp(new Date());
		
		StateMachineHandler handler = new StateMachineHandler();
		Product p = new Product(erpData, handler.createStateMachine());
		
		OPCDataItem opcDataItem = new OPCDataItem();
		opcDataItem.setItemName("Lichtschranke 1");
		opcDataItem.setValue(false);
		// Should work
		p.fireEvent(opcDataItem);
		
		try {
			p.fireEvent(opcDataItem);
			fail("Should have gotten an exception");
		}
		catch (IllegalStateException e) {
			// Successfully gotten an Exception
		}
		catch (Exception e) {
			fail("Got a wrong exception: " + e.getMessage());
		}
		
		
	}
}
