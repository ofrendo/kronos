package kronos.test;

import static org.junit.Assert.fail;
import kronos.server.http.rest.RouteHandler;
import kronos.util.Log;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TestServerRestAPI {
	
	RouteHandler routeHandler;
	
	@Before
	public void setup() {
		Log.info("________________TEST_________________");
		routeHandler = new RouteHandler();
	}
	
	@Test
	public void testGetDataByMat() {
		String path = "data/getDataByMat";
		testCall(path);
	}
	
	@Test
	public void testGetDataByMatGrp() {
		String path = "data/getDataByMatGrp";
		testCall(path);
	}
	
	@Test
	public void testGetProductData() {
		String path = "data/getDataByAnalysisResult";
		testCall(path);
	}
	
	public synchronized void testCall(String path) {
		Log.info("Starting call " + path + "...");
		String result = routeHandler.handleRoute(path);
		
		if (getTestResult(result) == false) {
			fail("jsonElement.data is not a jsonArray");
		}
	}
	
	public boolean getTestResult(String result) {
		JsonParser parser = new JsonParser();
		JsonObject testResult = (JsonObject) parser.parse(result);
		JsonArray testResultArray = (JsonArray) testResult.get("data");
		Log.info("TestServerRestAPI data length: " + testResultArray.size());
		if (testResultArray.size() == 0) {
			fail("Empty json array");
		}
		System.out.println(testResultArray.toString());
		return testResultArray.isJsonArray();
	}
	
}
