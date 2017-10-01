package com.mao;

import java.io.File;

import com.mao.lang.Program;

public class MainServer {
	public static void main(String[] args) {
		Network.initialize(new NetworkServer());

		Game game = Game.initialize();
		for (int i = 0; i < 52; i++) {
			game.addCardToDeck(Card.of(Face.values()[i % 13], Suit.values()[i / 13]));
			game.shuffleDeck(); // we like it shuffled!
		}
		game.playCard(Game.getGame().getCardFromDeck());

		RuleHandler handler = RuleHandler.initialize();
		try {
			handler.addRule(Program.compile(new File("rules/turn.mao")));
			//handler.addRule(Program.compile(new File("rules/placement.mao")));
			handler.addRule(Program.compile(new File("rules/one_card_remaining.mao")));
			handler.addRule(Program.compile(new File("rules/mao_on_win.mao")));
			handler.addRule(Program.compile(new File("rules/all_hail_the_queen_of_mao.mao")));
			handler.addRule(Program.compile(new File("rules/have_a_nice_day.mao")));
		} catch (Throwable e) {
			Debug.error("Error while compiling default rules!", e);
			System.exit(1);
		}

		try {
			Debug.log("Prepared for clients to connect.");
			Network.getNetworkServer().getServer().waitUntilClose();
		} catch (InterruptedException e) {
			Debug.error("Error while waiting for server to close!", e);
			System.exit(1);
		}
	}
}