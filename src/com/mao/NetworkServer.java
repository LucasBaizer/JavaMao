package com.mao;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jnetwork.DataPackage;
import org.jnetwork.TCPConnection;
import org.jnetwork.TCPServer;

public class NetworkServer extends Network {
	private ArrayList<TCPConnection> clients = new ArrayList<>();
	private TCPServer server;

	@SuppressWarnings("unchecked")
	@Override
	protected void onInitialize() {
		Debug.log("Starting server...");
		server = new TCPServer(1337, (data) -> {
			try {
				TCPConnection client = (TCPConnection) data.getConnection();

				Debug.log(client.getRemoteSocketAddress() + " connected.");
				clients.add(client);

				for (List<NetworkedObject> list : getRegisteredObjects()) {
					for (NetworkedObject obj : list) {
						registerObjectOnClient(obj, client);
						makeUpdate(obj, client);
					}
				}

				while (!client.isClosed()) {
					DataPackage pkg = (DataPackage) client.readUnshared();
					if (pkg.getMessage().equals("GAME_DATA")) {
						int netID = (int) pkg.getObjects()[0];
						long uniqueID = (long) pkg.getObjects()[1];
						int netObjects = (int) pkg.getObjects()[2];
						
						NetworkedData netdata = new NetworkedData();
						for (int i = 3; i < 3 + netObjects; i++) {
							netdata.write(pkg.getObjects()[i]);
						}

						for (NetworkedObject obj : getObjects(netID)) {
							if (obj.getUniqueID() == uniqueID) {
								obj.readNetworkedData(netdata);

								if (obj instanceof Player) {
									Debug.log("Updating player with username " + ((Player) obj).getUsername() + ".");
								} else {
									Debug.log("Updating object of type " + obj.getClass().getSimpleName() + ".");
								}
							}
						}

						for (TCPConnection connected : clients) {
							if (!client.getRemoteSocketAddress().toString()
									.equals(connected.getRemoteSocketAddress().toString())) {
								connected.writeUnshared(pkg);
							}
						}
					} else if (pkg.getMessage().equals("REGISTER_OBJECT")) {
						NetworkedObject object = (NetworkedObject) ((Class<? extends NetworkedObject>) pkg
								.getObjects()[0]).newInstance();
						object.setUniqueID((long) pkg.getObjects()[1]);
						registerObject(object);

						for (TCPConnection connected : clients) {
							if (!client.getRemoteSocketAddress().toString()
									.equals(connected.getRemoteSocketAddress().toString())) {
								connected.writeUnshared(pkg);
							}
						}

						Debug.log("Registered object of type " + object.getClass().getSimpleName() + ".");
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

	public void registerObjectOnClient(NetworkedObject obj, TCPConnection client) {
		try {
			client.writeUnshared(new DataPackage(obj.getClass(), obj.getUniqueID()).setMessage("REGISTER_OBJECT"));
		} catch (IOException e) {
			Debug.error("Error while writing client data to server!", e);
			System.exit(1);
		}
	}

	@Override
	public void makeUpdate(NetworkedObject object) {
		for (TCPConnection client : clients) {
			try {
				client.writeUnshared(getUpdatePackage(object));
			} catch (IOException e) {
				Debug.error("Error while writing server data to client!", e);
				System.exit(1);
			}
		}
	}

	private void makeUpdate(NetworkedObject object, TCPConnection client) {
		try {
			client.writeUnshared(getUpdatePackage(object));
		} catch (IOException e) {
			Debug.error("Error while writing server data to client!", e);
			System.exit(1);
		}
	}

	private DataPackage getUpdatePackage(NetworkedObject object) {
		NetworkedData net = object.writeNetworkedData();
		Serializable[] data = new Serializable[net.getObjectsCount() + 3];
		data[0] = object.getNetworkID();
		data[1] = object.getUniqueID();
		data[2] = net.getObjectsCount();
		for (int i = 3; i < 3 + net.getObjectsCount(); i++) {
			data[i] = net.read();
		}
		return new DataPackage(data).setMessage("GAME_DATA");
	}
}
