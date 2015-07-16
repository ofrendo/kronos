package kronos.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import kronos.util.Log;

/**
 * Used for database connection. Singleton.
 * @author Matthias
 *
 */
public class DB {
	private static DB instance;
	private Connection conn;
	
	private DB() throws Exception {
		try {
			Log.info("DB: Loading JDBC drivers for sqlite...");
		    Class.forName("org.sqlite.JDBC");
		    Log.info("DB: JDBC drivers loaded successfully.");
		    
		    Log.info("DB: Connecting to sqlite database...");
		    conn = DriverManager.getConnection("jdbc:sqlite:data/test.db");
		    Log.info("DB: Connection established.");
		} catch (Exception e) {
			conn = null;
			Log.error("DB: " + e.getMessage());
			throw e;
		}
	}
	
	public static DB getDB() throws Exception {
		if(instance == null){
			instance = new DB();
		}
		return instance;
	}
	
	public void createTables() throws SQLException {
		try {
			Log.info("DB: Creating tables on database...");
			Statement stmt = conn.createStatement();
		    String sql = "CREATE TABLE IF NOT EXISTS COMPANY " +
		                 "(ID INT PRIMARY KEY     NOT NULL," +
		                 " NAME           TEXT    NOT NULL, " + 
		                 " AGE            INT     NOT NULL, " + 
		                 " ADDRESS        CHAR(50), " + 
		                 " SALARY         REAL)"; 
		    stmt.executeUpdate(sql);
		    stmt.close();
		    Log.info("DB: All tables created.");
		} catch (SQLException e) {
			Log.error(e.getMessage());
			throw e;
		}
	}
	
	
}
