package com.mao;

import java.util.ArrayList;
import java.util.List;

public class Player extends NetworkedObject {
	private String username;
	private ArrayList<Card> hand = new ArrayList<>();

	public void initialize(String username) {
		if (Network.isClient()) {
			this.username = username;
			Network.getNetworkClient().registerObject(this);
			Network.getNetworkClient().makeUpdate(this);
		}
	}

	@Override
	public NetworkedData writeNetworkedData() {
		NetworkedData data = new NetworkedData();
		data.write(hand.size());
		for (Card card : hand) {
			data.write(card);
		}
		data.write(username);
		return data;
	}

	@Override
	public void readNetworkedData(NetworkedData data) {
		hand.clear();

		int handSize = (int) data.read();
		for (int i = 0; i < handSize; i++) {
			hand.add(data.read());
		}
		username = data.read();
	}

	@Override
	public int getNetworkID() {
		return 1;
	}

	public void addCard(Card card) {
		hand.add(card);
	}

	public List<Card> getHand() {
		return hand;
	}

	public String getUsername() {
		return username;
	}
}
