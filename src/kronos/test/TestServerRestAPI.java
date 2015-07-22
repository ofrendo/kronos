package kronos.test;

import static org.junit.Assert.fail;
import kronos.server.http.rest.RouteHandler;
import kronos.util.Log;

import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TestServerRestAPI {

	@Test
	public void testGetAnalysisResultByMat() {
		Log.info("______________TEST_________________");
		Log.info("Starting call data/getAnalysisResultByMat...");
		
		RouteHandler routeHandler = new RouteHandler();
		
		String path = "data/getAnalysisResultByMat";
		String result = routeHandler.handleRoute(path);
		
		JsonParser parser = new JsonParser();
		JsonObject testResult = (JsonObject) parser.parse(result);
		JsonArray testResultArray = (JsonArray) testResult.get("data");
		Log.info("TestServerRestAPI data length: " + testResultArray.size());
		if (testResultArray.isJsonArray() == false) {
			fail("jsonElement.data is not a jsonArray");
		}
	}

}
