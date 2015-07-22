package kronos.test;

import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Test;

import kronos.db.DB;
import kronos.fsm.StateMachineHandler;
import kronos.model.ERPData;
import kronos.model.OPCDataItem;
import kronos.model.Product;
import kronos.model.SAData;

public class TestDB {
	
	@Test
	public void testConn(){
		try {
			@SuppressWarnings("unused")
			DB db = DB.getDB();
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testTableCreationDeletion(){
		try {
			DB db = DB.getDB();
			db.createTables();
			db.deleteDatabase();
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testTableInsertion(){
		try {
			DB db = DB.getDB();
			db.createTables();
			// create erp data
			ERPData erpData = new ERPData();
			erpData.setCustomerNumber(4711);
			erpData.setMaterialNumber(42);
			erpData.setOrderNumber("123 ABC 456");
			erpData.setTimeStamp(new Date());
			// create product
			StateMachineHandler handler = new StateMachineHandler();
			Product p = new Product(erpData, handler.createStateMachine());
			// create opc items
			OPCDataItem opc = new OPCDataItem(true, DB.stations[0]);
			p.addSimData(opc);
			Thread.sleep(100);
			opc = new OPCDataItem(false, DB.stations[0]);
			p.addSimData(opc);
			opc = new OPCDataItem(3.5, DB.stations[1]);
			p.addSimData(opc);
			// add sa data
			SAData saData = new SAData();
			saData.setTs_stop((new Date()).getTime());
			saData.setOverallStatus("OK");
			p.setSAData(saData);
			// insert into db
			db.insertIntoDB(p);
			db.logDB();
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
