package com.mao;

public interface NetworkedObject {
	public int getNetworkID();
	
	public int getNetworkedObjectsCount();
	
	public NetworkedData writeNetworkedData();
	
	public void readNetworkedData(NetworkedData data);
}
