package com.mao;

import java.util.ArrayList;
import java.util.List;

import com.mao.ui.Processing;
import com.mao.ui.UICard;

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

		if (Network.isClient()) {
			Processing.getProcessing().addUIObject(new UICard(card, hand.size() - 1, hand.size()));
		}
	}

	public void removeCard(Card card) {
		hand.remove(card);

		if (Network.isClient()) {
			Processing.getProcessing().removeUIObject(card.hashCode());
		}
	}

	public List<Card> getHand() {
		return hand;
	}

	public String getUsername() {
		return username;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Player)) {
			return false;
		}
		Player other = (Player) obj;
		if (username == null) {
			if (other.username != null) {
				return false;
			}
		} else if (!username.equals(other.username)) {
			return false;
		}
		return true;
	}

	public static Player getCurrentTurnPlayer() {
		if (Game.getGame().getCurrentPlayerUsername() != null) {
			for (NetworkedObject obj : Network.getNetwork().getObjects(1)) {
				Player player = (Player) obj;
				if (player.username.equals(Game.getGame().getCurrentPlayerUsername())) {
					return player;
				}
			}
		}
		return null;
	}

	public static Player getNextTurnPlayer() {
		if (Game.getGame().getCurrentPlayerUsername() != null) {
			List<NetworkedObject> objects = Network.getNetwork().getObjects(1);
			for (int i = 0; i < objects.size(); i++) {
				Player player = (Player) objects.get(i);
				if (player.username.equals(Game.getGame().getCurrentPlayerUsername())) {
					if (i + 1 == objects.size()) {
						return (Player) objects.get(0);
					} else {
						return (Player) objects.get(i + 1);
					}
				}
			}
		}
		return null;
	}
}
