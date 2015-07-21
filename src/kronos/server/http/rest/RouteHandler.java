package kronos.server.http.rest;

import java.util.ArrayList;

public class RouteHandler {
	
	private ArrayList<Route> routes;
	
	public RouteHandler() {
		routes = new ArrayList<Route>();
		routes.add(new Route("data/test", new Route.Callback() {
			public void onCallback() {
				
			}
		}));
		
	}
	
	public boolean handleRoute(String path) {
		for (Route route : routes) {
			if (route.getPath().equals(path)) {
				route.getCallback().onCallback();
				return true;
			}
		}
		return false;
	}
}
