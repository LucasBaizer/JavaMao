package com.mao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mao.client.Processing;
import com.mao.client.UICard;

public class Player extends NetworkedObject {
	public String username;
	private ArrayList<Card> hand = new ArrayList<>();

	public Player initialize(String username) {
		if (Network.isClient()) {
			this.username = username;
			Network.getNetworkClient().registerObject(this);
			Network.getNetworkClient().makeUpdate(this);
		}
		return this;
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
		Collections.sort(hand);

		if (Network.isClient()) {
			if (Processing.getProcessing() != null) {
				Processing.getProcessing().addUIObject(new UICard(card, hand.indexOf(card), hand.size()));

				for (Card each : hand) {
					UICard uiCard = (UICard) Processing.getProcessing().getObject(each.hashCode());
					uiCard.setCardIndex(hand.indexOf(uiCard.card));
				}

				Processing.getProcessing().sortObjects();
			}
		}
	}

	public void removeCard(Card card) {
		hand.remove(card);
		Collections.sort(hand);

		if (Network.isClient()) {
			if (Processing.getProcessing() != null) {
				UICard returned = (UICard) Processing.getProcessing().removeUIObject(card.hashCode());

				for (Card each : hand) {
					UICard ui = (UICard) Processing.getProcessing().getObject(each.hashCode());
					if (ui.getCardIndex() > returned.getCardIndex()) {
						ui.setCardIndex(ui.getCardIndex() - 1);
					}
					ui.setTotalCards(hand.size());
				}
			}
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
