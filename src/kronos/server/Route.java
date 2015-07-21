package kronos.server;


public class Route {
	
	private String path;
	private Callback callback;
	
	public Route(String path, Callback callback) {
		this.path = path;
		this.callback = callback;
	}
	
	public String getPath() {
		return path;
	}
	public Callback getCallback() {
		return callback;
	}
	
	public interface Callback {
		public void onCallback(); 
	}
}
