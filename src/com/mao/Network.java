package com.mao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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

	public static void deinitialize() {
		if (instance != null) {
			instance.destroy();
			instance = null;
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

	public static boolean isInitialized() {
		return instance != null;
	}

	private HashMap<Integer, List<NetworkedObject>> networkedObjects = new HashMap<>();

	protected abstract void onInitialize();

	public abstract void makeUpdate(NetworkedObject object);

	public abstract void destroy();

	public void registerObject(NetworkedObject obj) {
		synchronized (networkedObjects) {
			List<NetworkedObject> list = networkedObjects.get(obj.getNetworkID());
			if (list == null) {
				ArrayList<NetworkedObject> l = new ArrayList<>();
				networkedObjects.put(obj.getNetworkID(), l);
				list = l;
			}
			list.add(obj);
		}
	}

	public void deregisterObject(NetworkedObject obj) {
		synchronized (networkedObjects) {
			networkedObjects.get(obj.getNetworkID()).remove(obj);
		}
	}

	public List<NetworkedObject> getObjects(int objectID) {
		synchronized (networkedObjects) {
			List<NetworkedObject> base = networkedObjects.get(objectID);
			List<NetworkedObject> list = base == null ? new ArrayList<>() : new ArrayList<>(base);
			List<NetworkedObject> objects = Arrays.asList(list.toArray(new NetworkedObject[list.size()]));
			if (objects.size() == 0) {
				Debug.error("Attempted to get objects with ID " + objectID + ", but none were found.");
			}
			return objects;
		}
	}

	public Collection<List<NetworkedObject>> getRegisteredObjects() {
		synchronized (networkedObjects) {
			return networkedObjects.values();
		}
	}
}
