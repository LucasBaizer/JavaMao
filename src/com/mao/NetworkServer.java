package com.mao;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import org.jnetwork.DataPackage;
import org.jnetwork.TCPConnection;
import org.jnetwork.TCPServer;

public class NetworkServer extends Network {
	private ArrayList<TCPConnection> clients = new ArrayList<>();
	private TCPServer server;

	@Override
	protected void onInitialize() {
		Debug.log("Starting server...");
		server = new TCPServer(1337, (data) -> {
			try {
				TCPConnection client = (TCPConnection) data.getConnection();

				Debug.log(client.getRemoteSocketAddress() + " connected.");
				clients.add(client);

				while (!client.isClosed()) {
					DataPackage pkg = (DataPackage) client.readUnshared();
					if (pkg.getMessage().equals("GAME_DATA")) {
						for (TCPConnection connected : clients) {
							if (client != connected) {
								connected.writeUnshared(pkg);
							}
						}
					}
				}
			} catch (Exception e) {
				Debug.error("Error in client thread!", e);
				return;
			}
		});
		server.addClientDisconnectionListener((data) -> {
			Debug.log(data.getConnection().getRemoteSocketAddress() + " disconnected.");
			clients.remove(data.getConnection());
		});
		try {
			server.start();
			Debug.log("Server started!");
		} catch (Exception e) {
			Debug.error("Error while launching server!", e);
			System.exit(1);
		}
	}

	public TCPServer getServer() {
		return this.server;
	}

	@Override
	public void makeUpdate(NetworkedObject object) {
		NetworkedData net = object.writeNetworkedData();
		Serializable[] data = new Serializable[object.getNetworkedObjectsCount() + 2];
		data[0] = object.getNetworkID();
		data[1] = object.getNetworkedObjectsCount();
		for (int i = 2; i < 2 + object.getNetworkedObjectsCount(); i++) {
			data[i] = net.read();
		}
		DataPackage pkg = new DataPackage(data).setMessage("GAME_DATA");
		for (TCPConnection client : clients) {
			try {
				client.writeUnshared(pkg);
			} catch (IOException e) {
				Debug.error("Error while writing client data to server!", e);
				System.exit(1);
			}
		}
	}
}
