package com.mao;

import java.io.EOFException;
import java.io.IOException;
import java.io.Serializable;
import java.net.SocketException;

import org.jnetwork.DataPackage;
import org.jnetwork.TCPConnection;

public class NetworkClient extends Network {
	private TCPConnection client;

	@SuppressWarnings("unchecked")
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

		Game.getGame();

		Thread receiveThread = new Thread(() -> {
			while (!Thread.currentThread().isInterrupted()) {
				try {
					DataPackage in = (DataPackage) client.readUnshared();
					if (in.getMessage().equals("GAME_DATA")) {
						int netID = (int) in.getObjects()[0];
						long uniqueID = (long) in.getObjects()[1];
						int netObjects = (int) in.getObjects()[2];

						NetworkedData data = new NetworkedData();
						for (int i = 3; i < 3 + netObjects; i++) {
							data.write(in.getObjects()[i]);
						}
						
						for (NetworkedObject obj : Network.getNetworkClient().getObjects(netID)) {
							if (obj.getUniqueID() == uniqueID) {
								Debug.log("Updating incoming object of type " + obj.getClass().getSimpleName() + ".");
								obj.readNetworkedData(data);
								if (obj instanceof Player) {
									Debug.log("Username: " + ((Player) obj).getUsername());
								}
							}
						}
					} else if (in.getMessage().equals("REGISTER_OBJECT")) {
						NetworkedObject object = (NetworkedObject) ((Class<? extends NetworkedObject>) in
								.getObjects()[0]).newInstance();
						if(object instanceof Game) {
							Game.setGame((Game) object);
						}
						object.setUniqueID((long) in.getObjects()[1]);
						registerUnsharedObject(object);
						Debug.log("Registered incoming object of type " + object.getClass().getSimpleName() + ".");
					}
				} catch(SocketException | EOFException e) {
					Debug.error("Server forcefully closed connection, exiting!");
					System.exit(-1);
				} catch (Exception e) {
					Debug.error("Error while reading server data!", e);
					System.exit(1);
				}
			}
		});
		receiveThread.start();
	}

	@Override
	public void registerObject(NetworkedObject obj) {
		obj.setUniqueID(Game.getRandomInstance().nextLong());

		super.registerObject(obj);

		try {
			client.writeUnshared(new DataPackage(obj.getClass(), obj.getUniqueID()).setMessage("REGISTER_OBJECT"));
		} catch (IOException e) {
			Debug.error("Error while writing client data to server!", e);
			System.exit(1);
		}
	}

	public void registerUnsharedObject(NetworkedObject obj) {
		super.registerObject(obj);
	}

	@Override
	public void makeUpdate(NetworkedObject object) {
		NetworkedData net = object.writeNetworkedData();
		Serializable[] data = new Serializable[net.getObjectsCount() + 3];
		data[0] = object.getNetworkID();
		data[1] = object.getUniqueID();
		data[2] = net.getObjectsCount();
		for (int i = 3; i < 3 + net.getObjectsCount(); i++) {
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