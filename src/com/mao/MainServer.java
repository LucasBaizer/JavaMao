package com.mao;

public class MainServer {
	public static void main(String[] args) {
		Network.initialize(new NetworkServer());

		Game game = Game.initialize();
		for (int i = 0; i < 52; i++) {
			game.addCardToDeck(Card.of(Face.values()[i % 13], Suit.values()[i / 13]));
			game.shuffleDeck(); // we like it shuffled!
		}
		game.addCard(Game.getGame().getCardFromDeck());

		try {
			Network.getNetworkServer().getServer().waitUntilClose();
		} catch (InterruptedException e) {
			Debug.error("Error while waiting for server to close!", e);
			System.exit(1);
		}
	}
}