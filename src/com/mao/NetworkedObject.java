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

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NetworkedObject) {
			NetworkedObject net = (NetworkedObject) obj;
			return net.uniqueID == uniqueID;
		}
		return false;
	}
}
