package com.mao;

import java.util.ArrayList;

public class Player implements NetworkedObject {
	private ArrayList<Card> hand = new ArrayList<>();

	public Player() {
		Network.getNetwork().registerObject(this);
	}

	@Override
	public int getNetworkedObjectsCount() {
		return 1;
	}

	@Override
	public NetworkedData writeNetworkedData() {
		return new NetworkedData(hand);
	}

	@Override
	public void readNetworkedData(NetworkedData data) {
		hand = data.read();
	}

	@Override
	public int getNetworkID() {
		return 1;
	}
}
