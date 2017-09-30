package com.mao;

public abstract class NetworkedObject {
	private long uniqueID;
	
	public abstract int getNetworkID();
	
	public abstract NetworkedData writeNetworkedData();
	
	public abstract void readNetworkedData(NetworkedData data);

	public long getUniqueID() {
		return uniqueID;
	}

	public void setUniqueID(long uniqueID) {
		this.uniqueID = uniqueID;
	}
	
	public void update() {
		Network.getNetwork().makeUpdate(this);
	}
}
