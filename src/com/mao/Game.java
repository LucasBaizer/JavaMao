package com.mao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class Game extends NetworkedObject {
	private static final Random random = new Random();
	private static Game inst;

	public static Random getRandomInstance() {
		return random;
	}

	public static Game getGame() {
		return inst;
	}

	private ArrayList<Card> playedCards = new ArrayList<>();
	private Stack<Card> deck = new Stack<>();

	public static Game initialize() {
		Game game = new Game();
		inst = game;
		Network.getNetwork().registerObject(game);
		return game;
	}
	
	public static void setGame(Game game) {
		inst = game;
	}

	@Override
	public NetworkedData writeNetworkedData() {
		NetworkedData data = new NetworkedData();

		data.write(deck.size());
		for (int i = 0; i < deck.size(); i++) {
			data.write(deck.elementAt(i));
		}

		data.write(playedCards.size());
		for (int i = 0; i < playedCards.size(); i++) {
			data.write(playedCards.get(i));
		}

		return data;
	}

	@Override
	public void readNetworkedData(NetworkedData data) {
		deck.clear();
		playedCards.clear();

		int deckSize = data.read();
		for (int i = 0; i < deckSize; i++) {
			deck.push(data.read());
		}

		int playedCardsSize = data.read();
		for (int i = 0; i < playedCardsSize; i++) {
			playedCards.add(data.read());
		}

		Debug.log("Updated game. Top card is now the {0}. Deck now has {1} cards.",
				playedCards.get(playedCards.size() - 1), deck.size());
	}

	public List<Card> getPlayedCards() {
		return this.playedCards;
	}

	public void setPlayedCards(ArrayList<Card> cards) {
		this.playedCards = cards;
	}

	public void addCardToDeck(Card card) {
		deck.push(card);
	}

	public Card getCardFromDeck() {
		return deck.pop();
	}

	public void shuffleDeck() {
		Collections.shuffle(deck);
	}

	@Override
	public int getNetworkID() {
		return 0;
	}

	public void addCard(Card card) {
		playedCards.add(card);
	}
}
