package kronos.db;

import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import kronos.model.ERPData;
import kronos.model.Product;
import kronos.util.Log;

/**
 * Used for database connection. Singleton.
 * @author Matthias
 *
 */
public class DB {
	private static DB instance;
	private Connection conn;
	private final Path DB_PATH = Paths.get("data/historical.db");
	
	// table names
	public static final String TABLE_PRODUCT = "Product";
	public static final String TABLE_MEASURE = "Measure";
	// column names
	public static final String COL_PRODUCT_ID = "ID";
	public static final String COL_ORDER_NO = "OrderNo";
	public static final String COL_CUSTOMER_NO = "CustomerNo";
	public static final String COL_MATERIAL_NO = "MaterialNo";
	public static final String COL_PRODUCTION_START = "ProductionStart";
	public static final String COL_PRODUCTION_END = "ProductionEnd";
	public static final String COL_ANALYSIS_RESULT = "AnalysisResult";
	public static final String COL_MEASURE_ID = "MeasureID";
	public static final String COL_MEASURE = "Measure";
	public static final String COL_STATION = "Station";
	public static final String COL_VALUE = "Value";
	
	private DB() throws Exception {
		try {
			Log.info("DB: Loading JDBC drivers for sqlite...");
		    Class.forName("org.sqlite.JDBC");
		    Log.info("DB: JDBC drivers loaded successfully.");
		    
		    Log.info("DB: Connecting to sqlite database...");
		    conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
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
		    // create product table
			String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCT + " ("
					+ COL_PRODUCT_ID + " INTEGER PRIMARY KEY, "	// No auto_increment needed as we don't delete / modify any data
					+ COL_ORDER_NO + " TEXT NOT NULL, "
					+ COL_CUSTOMER_NO + " INTEGER NOT NULL, "
					+ COL_MATERIAL_NO + " INTEGER NOT NULL, "
					+ COL_PRODUCTION_START + " INTEGER NOT NULL, " 	// timestamp
					+ COL_PRODUCTION_END + " INTEGER NOT NULL, "	// timestamp
					+ COL_ANALYSIS_RESULT + " TEXT NOT NULL"
					+ ")";
		    stmt.executeUpdate(sql);
		    // create measurements table
		    sql = "CREATE TABLE IF NOT EXISTS " + TABLE_MEASURE + " ("
		    		+ COL_MEASURE_ID + " INTEGER PRIMARY KEY, "
		    		+ COL_PRODUCT_ID + " INTEGER REFERENCES " + TABLE_PRODUCT + "(" + COL_PRODUCT_ID + "), "
		    		+ COL_MEASURE + " TEXT NOT NULL, "
		    		+ COL_STATION + " TEXT NOT NULL, "
		    		+ COL_VALUE + " NUMERIC NOT NULL"
		    		+ ")";
		    stmt.executeUpdate(sql);
		    stmt.close();
		    conn.commit();
		    Log.info("DB: All tables created.");
		} catch (SQLException e) {
			Log.error("DB: Table creation failed: " + e.getMessage());
			throw e;
		}
	}
	
	public void deleteDatabase() throws IOException{
		Log.info("DB: Deleting database \"" + DB_PATH + "\"...");
		try {
			conn.close();
		} catch (SQLException e) {
			Log.error("DB: The database connection couldn't be closed: " + e.getMessage());
		}
		try {
		    Files.delete(DB_PATH);
		} catch (NoSuchFileException e) {
			// if the database didn't exist anyway --> don't throw an error
		    Log.warn("DB: The database to be deleted on path \"" + DB_PATH.toString() + "\" doesn't exist!");
		} catch (IOException e) {
		    // problems occurring with file permissions (like write lock etc.)
		    Log.error("DB: The database couldn't be deleted! " + e.getMessage());
		    throw e;
		}
		Log.info("DB: Database deleted.");
	}
	
	public void insertIntoDB(Product product) throws SQLException {
		try {
			// insert into product table
			PreparedStatement stmt = conn.prepareStatement(
					"INSERT INTO " + TABLE_PRODUCT + " (" + 
							COL_ORDER_NO + ", " + 
							COL_CUSTOMER_NO + ", " + 
							COL_MATERIAL_NO + ", " + 
							COL_PRODUCTION_START + ", " + 
							COL_PRODUCTION_END + ", " + 
							COL_ANALYSIS_RESULT + ") VALUES (?, ?, ?, ?, ?, ?)");
			ERPData erp = product.getErpData();
			stmt.setString(1, erp.getOrderNumber());
			stmt.setInt(2, erp.getCustomerNumber());
			stmt.setInt(3, erp.getMaterialNumber());
			stmt.setLong(4, erp.getTimeStamp().getTime());
			stmt.setLong(5, fromSomewhereProductionEnd);
			stmt.setLong(6, fromSomeWhereAnalysisResult);
			stmt.executeUpdate();
			stmt.close();
			// insert into measures table
			stmt= conn.prepareStatement(
					"INSERT INTO " + TABLE_MEASURE + " (" + 
							COL_PRODUCT_ID + ", " + 
							COL_MEASURE + ", " + 
							COL_STATION + ", " + 
							COL_VALUE + ") VALUES (?, ?, ?, ?)");
			// commit changes
			conn.commit();
		} catch (SQLException e) {
			Log.error("DB: Error on inserting a product into the database: " + e.getMessage());
			throw e;
		}
	}
	
	
}
