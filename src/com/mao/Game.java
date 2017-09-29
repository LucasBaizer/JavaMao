package com.mao;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game implements NetworkedObject {
	private static Game game = new Game();
	private static final Random random = new Random();

	public static Random getRandomInstance() {
		return random;
	}

	public static Game getGame() {
		return game;
	}

	private ArrayList<Card> playedCards = new ArrayList<>();

	public Game() {
		Network.getNetwork().registerObject(this);
	}

	@Override
	public int getNetworkedObjectsCount() {
		return 1;
	}

	@Override
	public NetworkedData writeNetworkedData() {
		return new NetworkedData(playedCards);
	}

	@Override
	public void readNetworkedData(NetworkedData data) {
		playedCards = data.read();
	}

	public List<Card> getPlayedCards() {
		return this.playedCards;
	}

	public void setPlayedCards(ArrayList<Card> cards) {
		this.playedCards = cards;
	}

	@Override
	public int getNetworkID() {
		return 0;
	}
	
	public void addCard(Card card) {
		playedCards.add(card);
	}
}
