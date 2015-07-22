package kronos.server.http.rest;

import java.util.ArrayList;

import kronos.db.DB;
import kronos.util.Log;

public class RouteHandler {
	
	private ArrayList<Route> routes;
	private DB db;
	
	public RouteHandler() {
		try {
			db = DB.getDB();
			
			routes = new ArrayList<Route>();
			routes.add(new Route("data/getDataByMat", new Route.Callback() {
				public String onCallback() {
					return db.getDataByMat();				
				}
			}));
			routes.add(new Route("data/getDataByMatGrp", new Route.Callback() {
				public String onCallback() {
					return db.getDataByMatGrp();
				}
			}));
			routes.add(new Route("data/getDataByAnalysisResult", new Route.Callback() {
				public String onCallback() {
					return db.getDataByAnalysisResult();
				}
			}));
			
			
		}
		catch (Exception e) {
			Log.error("RouteHandler: Error getting DB");
		}
	}
	
	public String handleRoute(String path) {
		for (Route route : routes) {
			if (route.getPath().equals(path)) {
				return route.getCallback().onCallback();
			}
		}
		return null;
	}
}
