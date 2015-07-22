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
			routes.add(new Route("data/getAnalysisResultByMat", new Route.Callback() {
				public String onCallback() {
					return db.getAnalysisResultByMat();				
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
