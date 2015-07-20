package kronos.test;

import static org.junit.Assert.*;

import org.junit.Test;

import kronos.db.DB;

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
}
