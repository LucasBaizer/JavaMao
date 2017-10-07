package com.mao.client;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mao.Card;
import com.mao.Game;
import com.mao.Player;
import com.mao.RuleHandler;
import com.mao.lang.Code;
import com.mao.lang.PenalizeCommand;
import com.mao.lang.SayCommand;

import processing.core.PApplet;

public class MainClient {
	public static Player player;

	public static void main(String[] args) throws IOException {
		PApplet.main("com.mao.client.Processing");

		/*
		 * try { Thread.sleep(1000); } catch (InterruptedException e) {
		 * e.printStackTrace(); }
		 * 
		 * player = new Player(); player.initialize("Lucas");
		 * 
		 * Debug.log("Hello, my name is " + player.getUsername() + "!");
		 * 
		 * for (int i = 0; i < 4; i++) {
		 * player.addCard(Game.getGame().getCardFromDeck()); }
		 * 
		 * player.update(); if (Game.getGame().getCurrentPlayerUsername() ==
		 * null) {
		 * Game.getGame().setCurrentPlayerUsername(player.getUsername());
		 * Debug.log("It is now " + player.getUsername() + "'s turn."); }
		 * Game.getGame().update();
		 */
	}

	public static List<String> callEvent(Player player, Card card, String event) {
		ArrayList<String> required = new ArrayList<>();
		for (Code response : RuleHandler.getRuleHandler().fire(event, player, Player.getCurrentTurnPlayer(),
				Player.getNextTurnPlayer(), card)) {
			if (response instanceof PenalizeCommand) {
				PenalizeCommand penalize = (PenalizeCommand) response;

				Card penalty = Game.getGame().getCardFromDeck();
				player.addCard(penalty);

				Processing.getProcessing().notify(penalize.getReason(), penalize.getReason(), Color.RED);
			} else if (response instanceof SayCommand) {
				SayCommand say = (SayCommand) response;
				required.add(say.getPhrase().toLowerCase().replace(".", "").trim());
			}
		}
		return required;
	}
}