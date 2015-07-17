package kronos.server;

import java.net.InetSocketAddress;

import kronos.util.Log;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class WSServer extends WebSocketServer {

	private static final int port = 9003;
	private static InetSocketAddress address = new InetSocketAddress("127.0.0.1", port);

	public WSServer() {
		super(address);
	}
	
	@Override
	public void run() {
		Log.info("WSServer: Starting WS server on " + port + "...");
		super.run();
	}

	@Override
	public void onClose(WebSocket arg0, int arg1, String arg2, boolean arg3) {
		Log.info("WSServer: Connection closed.");
	}

	@Override
	public void onError(WebSocket arg0, Exception arg1) {
		Log.error("WSServer error: " + arg1.getMessage());
	}

	@Override
	public void onMessage(WebSocket arg0, String arg1) {
		System.out.println("Message! " + arg1);
	}

	@Override
	public void onOpen(WebSocket arg0, ClientHandshake arg1) {
		Log.info("WSServer: Connection opened.");
	}
	
	
	
}
