package com.mao;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import com.mao.client.MainClient;
import com.mao.client.Processing;

public class Game extends NetworkedObject {
	private static final Random random = new Random();
	private static HashMap<String, Game> instances = new HashMap<>();;

	public static Random getRandomInstance() {
		return random;
	}

	public static Game getGame(String lobby) {
		return instances.get(lobby);
	}

	public static Game getGame() {
		if (Network.isClient()) {
			return getGame(MainClient.lobby.getName());
		} else if (!Network.isInitialized()) {
			throw new RuntimeException(
					"A getGame() call was attempted, but the network has not been initialized.");
		}
		throw new RuntimeException("getGame() can only be called on a client; use getGame(String) instead");
	}

	private ArrayList<Card> playedCards = new ArrayList<>();
	private Stack<Card> deck = new Stack<>();
	private String currentPlayerUsername;

	public static Game initialize(String lobbyName) {
		Game game = new Game();
		instances.put(lobbyName, game);
		Network.getNetwork().registerObject(game);
		return game;
	}

	public static void setGame(String lobby, Game game) {
		instances.put(lobby, game);
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

		data.write(currentPlayerUsername);

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

		String oldUsername = new String(currentPlayerUsername == null ? "" : currentPlayerUsername);
		currentPlayerUsername = data.read();

		if (currentPlayerUsername != null && !oldUsername.equals(currentPlayerUsername) && Network.isClient()) {
			if (MainClient.player.getUsername().equals(currentPlayerUsername)) {
				Processing.getProcessing().notify("It is your turn.", null, Color.GREEN);
			} else {
				Processing.getProcessing().notify("It is " + currentPlayerUsername + "'s turn.", null, Color.WHITE);
			}
		}

		Debug.log("GAME: Updated game. Top card is now the {0}. Deck now has {1} cards.",
				playedCards.get(playedCards.size() - 1), deck.size());
		if (currentPlayerUsername != null) {
			Debug.log("GAME: It is now {0}'s turn.", currentPlayerUsername);
		} else {
			Debug.log("GAME: Which player's turn it is has not been set yet.");
		}
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
		Card card = deck.pop();
		if (deck.size() == 0) {
			Card top = playedCards.remove(playedCards.size() - 1);
			deck.addAll(playedCards);

			for (int i = 0; i < deck.size(); i++) {
				Collections.shuffle(deck);
			}

			playedCards.clear();
			playedCards.add(top);

			update();
		}
		return card;
	}

	public void shuffleDeck() {
		Collections.shuffle(deck);
	}

	@Override
	public int getNetworkID() {
		return 0;
	}

	public void playCard(Card card) {
		playedCards.add(card);
	}

	public String getCurrentPlayerUsername() {
		return currentPlayerUsername;
	}

	public void setCurrentPlayerUsername(String currentPlayerUsername) {
		this.currentPlayerUsername = currentPlayerUsername;
	}
}
