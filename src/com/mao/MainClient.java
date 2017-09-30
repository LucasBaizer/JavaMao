package com.mao;

public class MainClient {
	public static void main(String[] args) {
		Network.initialize(new NetworkClient());

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Player player = new Player();
		player.initialize("Negro");

		Debug.log("Hello, my name is " + player.getUsername() + "!");

		for (int i = 0; i < 4; i++) {
			player.addCard(Game.getGame().getCardFromDeck());
		}
		player.update();
		Game.getGame().update();
	}
}