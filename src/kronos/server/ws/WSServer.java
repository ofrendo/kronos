package kronos.server.ws;

import java.net.InetSocketAddress;
import java.util.ArrayList;

import kronos.model.Product;
import kronos.model.SimData;
import kronos.sim.source.SimSourceType;
import kronos.util.Log;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.google.gson.Gson;

public class WSServer extends WebSocketServer {

	private static final int port = 9003;
	private static InetSocketAddress address = new InetSocketAddress("127.0.0.1", port);
	
	/**
	 * List of currently connected clients
	 */
	private ArrayList<WebSocket> connectedWS;
	
	/**
	 * Used for starting a WebSocket server on port 9003 to send events to a 
	 * frontend client in real time.
	 */
	public WSServer() {
		super(address);
		connectedWS = new ArrayList<WebSocket>();
	}
	
	@Override
	public void run() {
		Log.info("WSServer: Starting WS server on " + port + "...");
		super.run();
	}

	@Override
	public void onClose(WebSocket arg0, int arg1, String arg2, boolean arg3) {
		Log.info("WSServer: Connection closed.");
		connectedWS.remove(arg0);
	}

	@Override
	public void onError(WebSocket arg0, Exception arg1) {
		Log.error("WSServer error: " + arg1.getMessage());
	}

	@Override
	public void onMessage(WebSocket arg0, String arg1) {
	}

	@Override
	public void onOpen(WebSocket arg0, ClientHandshake arg1) {
		Log.info("WSServer: Connection opened.");
		connectedWS.add(arg0);
	}

	/**
	 * Call this when a new event is sent from the simulation. This method
	 * will send the event to all connected clients in the form of a WSMessage.
	 * @param p The product to send, so meta data can be sent with the event
	 * @param simData The event
	 */
	public void onSimData(Product p, SimData simData) {
		SimSourceType type = SimData.getType(simData);
		Gson gson = new Gson();
		WSMessage message = new WSMessage(
				p.erpData.getOrderNumber(), 
				type, 
				p.getState(),
				simData);
		String json = gson.toJson(message);
		for (WebSocket ws : connectedWS) {
			ws.send(json);
		}
	}
	
	
	
}
