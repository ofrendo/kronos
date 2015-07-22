package kronos.test;

import kronos.server.http.rest.RouteHandler;
import kronos.util.Log;

import org.junit.Test;

public class TestServerRestAPI {

	@Test
	public void testGetAnalysisResultByMat() {
		Log.info("______________TEST_________________");
		Log.info("Starting call data/getAnalysisResultByMat...");
		
		RouteHandler routeHandler = new RouteHandler();
		
		String path = "data/getAnalysisResultByMat";
		String result = routeHandler.handleRoute(path);
		
		System.out.println(result);
		
	}

}
