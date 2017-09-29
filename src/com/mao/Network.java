package com.mao;

import java.util.HashMap;
import java.util.List;

public abstract class Network {
	private static Network instance;

	public static void initialize(Network inst) {
		if (instance == null) {
			instance = inst;
			instance.onInitialize();
		}
	}

	public static Network getNetwork() {
		return instance;
	}

	public static NetworkClient getNetworkClient() {
		return (NetworkClient) instance;
	}

	public static NetworkServer getNetworkServer() {
		return (NetworkServer) instance;
	}

	public static boolean isServer() {
		return instance instanceof NetworkServer;
	}

	public static boolean isClient() {
		return instance instanceof NetworkClient;
	}

	private HashMap<Integer, List<NetworkedObject>> networkedObjects = new HashMap<>();

	protected abstract void onInitialize();
	
	public abstract void makeUpdate(NetworkedObject object);

	public void registerObject(NetworkedObject obj) {
		this.networkedObjects.get(obj.getNetworkID()).add(obj);
	}

	public List<NetworkedObject> getObjects(int objectID) {
		return networkedObjects.get(objectID);
	}
}
