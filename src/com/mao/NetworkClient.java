package com.mao;

import java.io.IOException;
import java.io.Serializable;

import org.jnetwork.DataPackage;
import org.jnetwork.TCPConnection;

public class NetworkClient extends Network {
	private TCPConnection client;

	@Override
	protected void onInitialize() {
		Debug.log("Initializing client network connection...");
		try {
			client = new TCPConnection("localhost", 1337);
		} catch (IOException e) {
			Debug.error("Error while opening TCP connection!", e);
			System.exit(1);
		}
		Debug.log("Connected!");

		Thread receiveThread = new Thread(() -> {
			while (!Thread.currentThread().isInterrupted()) {
				try {
					DataPackage in = (DataPackage) client.readUnshared();
					if (in.getMessage().equals("GAME_DATA")) {
						int netID = (int) in.getObjects()[0];
						int netObjects = (int) in.getObjects()[1];

						NetworkedData data = new NetworkedData();
						for (int i = 2; i < 2 + netObjects; i++) {
							data.write(in.getObjects()[i]);
						}
						for (NetworkedObject obj : Network.getNetworkClient().getObjects(netID)) {
							obj.readNetworkedData(data);
						}
					}
				} catch (ClassNotFoundException | IOException e) {
					Debug.error("Error while reading server data!", e);
					System.exit(1);
				}
			}
		});
		receiveThread.start();
	}

	@Override
	public void makeUpdate(NetworkedObject object) {
		NetworkedData net = object.writeNetworkedData();
		Serializable[] data = new Serializable[object.getNetworkedObjectsCount() + 2];
		data[0] = object.getNetworkID();
		data[1] = object.getNetworkedObjectsCount();
		for(int i = 2; i < 2 + object.getNetworkedObjectsCount(); i++) {
			data[i] = net.read();
		}
		try {
			client.writeUnshared(new DataPackage(data).setMessage("GAME_DATA"));
		} catch (IOException e) {
			Debug.error("Error while writing client data to server!", e);
			System.exit(1);
		}
	}
}
