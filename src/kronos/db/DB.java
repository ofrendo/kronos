package kronos.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import kronos.model.ERPData;
import kronos.model.OPCDataItem;
import kronos.model.Product;
import kronos.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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
	public static final String COL_TIMESTAMP = "Timestamp";
	public static final String COL_ANALYSIS_TIME = "AnalysisTime";
	
	// stations to be logged to database
	public static final String[] stations = {
			"Milling Station",	// light barrier of milling station
			"Milling Speed",
			"Milling Heat",
			"Drilling Station",	// light barrier of drilling station
			"Drilling Speed",
			"Drilling Heat"
			};
	private static final String MILLING_STATION = stations[0];
	//private static final String MILLING_SPEED = stations[1];
	//private static final String MILLING_HEAT = stations[2];
	private static final String DRILLING_STATION = stations[3];
	//private static final String DRILLING_SPEED = stations[4];
	//private static final String DRILLING_HEAT = stations[5];
	
	private DB() {
		try {
			Log.info("DB: Loading JDBC drivers for sqlite...");
		    Class.forName("org.sqlite.JDBC");
		    Log.info("DB: JDBC drivers loaded successfully.");
		    
		    Log.info("DB: Connecting to sqlite database...");
		    conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
		    createTables();
		    createViews();
		    Log.info("DB: Connection established.");
		} catch (Exception e) {
			conn = null;
			Log.error("DB: " + e.getMessage());
		}
	}
	
	public void createViews() {
		Log.info("DB: Creating views on database...");
		
		// Create aggregated product
		createView("ProdMinMaxAvgMeasures.sql");
		
		// Create Group by analysisresult
		createView("AnalysisResultMinMaxAvgMeasures.sql");
		
		// create Group by MatNo views
		createView("MatNoOkPercentage.sql");
		createView("MatNoMinMaxAvgMeasures.sql");
		
		// Create Group by MatGrp views
		createView("ProdGroups.sql");
		createView("ProdGroupOkPercentage.sql");
		
		Log.info("DB: Views created successfully.");
	}
	
	private void createView(String sqlFile) {
		try {
			Statement stmt = conn.createStatement();
			String sql = getSqlFromFile(sqlFile);
			stmt.executeUpdate(sql);
			stmt.close();
		}
		catch (SQLException e) {
			Log.error("DB: Error creating view from " + sqlFile + ": " + e.getMessage());
		}
	}

	public static DB getDB() throws Exception {
		if(instance == null){
			instance = new DB();
		}
		return instance;
	}
	
	public void createTables() throws Exception {
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
					+ COL_ANALYSIS_RESULT + " TEXT NOT NULL, "
					+ COL_ANALYSIS_TIME + " INTEGER NOT NULL"
					+ ")";
		    stmt.executeUpdate(sql);
		    // create measurements table
		    sql = "CREATE TABLE IF NOT EXISTS " + TABLE_MEASURE + " ("
		    		+ COL_MEASURE_ID + " INTEGER PRIMARY KEY, "
		    		+ COL_PRODUCT_ID + " INTEGER REFERENCES " + TABLE_PRODUCT + "(" + COL_PRODUCT_ID + "), "
		    		+ COL_STATION + " TEXT NOT NULL, "
		    		+ COL_TIMESTAMP + " INTEGER NOT NULL, "
		    		+ COL_VALUE + " NUMERIC NOT NULL"
		    		+ ")";
		    stmt.executeUpdate(sql);
		    stmt.close();
		    Log.info("DB: All tables created if they didn't exist.");
		} catch (SQLException e) {
			Log.error("DB: Table creation failed: " + e.getMessage());
			throw e;
		}
	}
	
	public void deleteDatabase() throws Exception {
		Log.info("DB: Deleting database \"" + DB_PATH + "\"...");
		try {
			conn.close();
		} catch (SQLException e) {
			Log.error("DB: The database connection couldn't be closed: " + e.getMessage());
		}
		try {
		    Files.delete(DB_PATH);
		    conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
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
	
	public void insertIntoDB(Product product) throws Exception {
		try {
			// insert into product table
			PreparedStatement stmt = conn.prepareStatement(
					"INSERT INTO " + TABLE_PRODUCT + " (" + 
							COL_ORDER_NO + ", " + 
							COL_CUSTOMER_NO + ", " + 
							COL_MATERIAL_NO + ", " + 
							COL_PRODUCTION_START + ", " + 
							COL_PRODUCTION_END + ", " + 
							COL_ANALYSIS_RESULT + ", " + 
							COL_ANALYSIS_TIME + ") VALUES (?, ?, ?, ?, ?, ?, ?)");
			ERPData erp = product.getErpData();
			stmt.setString(1, erp.getOrderNumber());
			stmt.setInt(2, erp.getCustomerNumber());
			stmt.setInt(3, erp.getMaterialNumber());
			stmt.setLong(4, erp.getTimeStamp().getTime());
			stmt.setLong(5, product.getSAData().getTs_stop());
			stmt.setString(6, product.getSAData().getOverallStatus());
			stmt.setLong(7, product.getSAData().getTs_stop() - product.getSAData().getTs_start());
			int no_rows = stmt.executeUpdate();
			int prodId = -1;
			if(no_rows == 1){
				ResultSet generatedKeys = stmt.getGeneratedKeys();
				if(generatedKeys.next()){
					prodId = generatedKeys.getInt(1);
				}
			}
			stmt.close();
			// insert into measures table
			stmt= conn.prepareStatement(
					"INSERT INTO " + TABLE_MEASURE + " (" + 
							COL_PRODUCT_ID + ", " + 
							COL_STATION + ", " + 
							COL_TIMESTAMP + ", " + 
							COL_VALUE + ") VALUES (?, ?, ?, ?)");
			ArrayList<OPCDataItem> opcs = product.getOPCDataItems();
			for(int i = 0; i < opcs.size(); i++){
				OPCDataItem opc = opcs.get(i);
				if(Arrays.asList(stations).contains(opc.getItemName())){
					if((opc.getItemName().equals(MILLING_STATION) || opc.getItemName().equals(DRILLING_STATION))){ // it is a light barrier
						if((Boolean) opc.getValue()){ // it is an interrupting light barrier
							// get closing light barrier to calculate time
							long time = -1;
							for(int j = i+1; j < opcs.size(); j++){
								OPCDataItem opc1 = opcs.get(j);
								if(opc.getItemName().equals(opc1.getItemName())){ // it is the same light barrier
									if(!(Boolean)opc1.getValue()){ // it is a closing light barrier
										time = opc1.getTimestamp() - opc.getTimestamp();
									}
								}
							}
							// catch if no data was found
							if(time == -1){
								throw new Exception("The data is corrupt! Light barrier " + opc.getItemName() + " has no reconnecting event!");
							}
							// insert data on database
							stmt.setInt(1, prodId);
							stmt.setString(2, opc.getItemName());
							stmt.setLong(3, opc.getTimestamp());
							stmt.setLong(4, time);
							stmt.executeUpdate();
						}
					} else { // it isn't a light barrier
						stmt.setInt(1, prodId);
						stmt.setString(2, opc.getItemName());
						stmt.setLong(3, opc.getTimestamp());
						// get the proper data type
						if(opc.getValue() instanceof Integer){
							stmt.setInt(4, (Integer) opc.getValue());
						} else if(opc.getValue() instanceof Double){
							stmt.setDouble(4, (Double) opc.getValue());
						} else if(opc.getValue() instanceof Long){
							stmt.setLong(4, (Long) opc.getValue());
						} else {
							throw new Exception("Couldn't insert value into database: Data type " + opc.getValue().getClass() + " not supported yet!");
						}
						stmt.executeUpdate();
					}
				}
			}
			
			
		} catch (SQLException e) {
			Log.error("DB: Error on inserting a product into the database: " + e.getMessage());
			throw e;
		}
	}
	
	public void logDB(){
		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' ORDER BY name");
			while(rs.next()){
				logTable(rs.getString(1));
			}
		} catch (Exception e) {
			Log.error("DB: Database couldn't be logged!");
		}
	}
	
	private void logTableContent(String tableName, ArrayList<String> types) throws Exception{
		String log = "";
		Statement stmt = conn.createStatement();
		String sql = "SELECT * FROM " + tableName;
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()){
			for (int i = 1; i <= types.size(); i++) {
				String type = types.get(i-1);
				if (type.equals("INTEGER")) {
					log += rs.getLong(i);
				}
				else if (type.equals("NUMERIC")) {
					log += rs.getDouble(i);
				}
				else if (type.equals("TEXT")) {
					log += rs.getString(i);
				}
				else {
					throw new Exception("DB: Data type \"" + types.get(i) + "\" is not supported for logging yet!");
				}
				log += " ";
			}
			log += "\n";
		}
		Log.info("DB: " + log);
	}

	public void logTable(String tableName) {
		try {
			ArrayList<String> types = new ArrayList<String>();
			String columns = "";
			String sql = "PRAGMA table_info(" + tableName + ");";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				columns += rs.getString(2) + " ";
				types.add(rs.getString(3));
			}
			Log.info("DB: Table \"" + tableName + "\": " + columns);
			logTableContent(tableName, types);
		} catch (Exception e) {
			Log.error("DB: Table \"" + tableName + "\" couldn't be logged!");
		}
	}
	
	
	public String getSqlFromFile(String fileName) {
		String result = null;
		try {
			File file = new File("doc/" + fileName);
			FileInputStream fis = new FileInputStream(file);
			byte[] data = new byte[(int) file.length()];
			fis.read(data);
			fis.close();

			result = new String(data, "UTF-8");
		}
		catch (Exception e) {
			Log.error("DB: Error getting file: " + e.getMessage());
		}
		return result;
	}
	
	public String getLastProducts() {
		String result = null;
		try {
			String sql = "SELECT * FROM ProdMinMaxAvgMeasures ORDER BY ProductionStart DESC LIMIT 25";
			result = getJsonData(sql);
		}
		catch (Exception e) {
			Log.error("DB: Error getting getLastProducts: " + e.getMessage());
		}
		return result;
	}
	
	public String getDataByAnalysisResult() {
		String result = null;
		try {
			String sql = "SELECT * FROM AnalysisResultMinMaxAvgMeasures";
			result = getJsonData(sql);
		}
		catch (Exception e) {
			Log.error("DB: Error getting getDataByAnalysisResult: " + e.getMessage());
		}	
		return result;
	}
	
	public String getDataByMat() {
		String result = null;
		try {
			String sql = "SELECT * FROM AnalysisResultByMat NATURAL JOIN MatNoMinMaxAvgMeasures";
			result = getJsonData(sql);
		}
		catch (Exception e) {
			Log.error("DB: Error getting getDataByMat: " + e.getMessage());
		}	
		return result;
	}
	
	public String getDataByMatGrp() {
		String result = null;
		try {
			String sql = "SELECT * FROM ProdGroupOKPercentage";
			result = getJsonData(sql);
		}
		catch (Exception e) {
			Log.error("DB: Error getting getDataByMatGrp: " + e.getMessage());
		}
		return result;
	}
	
	private String getJsonData(String sql) throws SQLException {
		JsonObject result = new JsonObject();
		JsonArray resultArray = new JsonArray();
		
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		ResultSetMetaData metaData = rs.getMetaData();
		while(rs.next()) {
			JsonObject row = buildRow(rs, metaData);
			resultArray.add(row);
		}
		result.add("data", resultArray);
		return result.toString();
	}

	private JsonObject buildRow(ResultSet rs, ResultSetMetaData metaData) throws SQLException {
		//Add columns to json row
		JsonObject row = new JsonObject();
		for (int i = 1; i < metaData.getColumnCount()+1; i++) {
			String columnLabel = metaData.getColumnName(i);
			Object value = rs.getObject(i);
			if (value instanceof Integer) {
				row.addProperty(columnLabel, (Integer) value);
			}
			else if (value instanceof Double) {
				row.addProperty(columnLabel, (Double) value);
			}
			else if (value instanceof String) {
				row.addProperty(columnLabel, (String) value);
			}
			else if (value instanceof Long) {
				row.addProperty(columnLabel, (Long) value);
			}
		}
		return row;
	}
	
	
}
