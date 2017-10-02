package com.mao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import com.mao.lang.Code;
import com.mao.lang.Event;
import com.mao.lang.PenalizeCommand;
import com.mao.lang.SayCommand;

public class MainClient {
	private static final Scanner in = new Scanner(System.in);

	public static void main(String[] args) {
		Network.initialize(new NetworkClient());

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Player player = new Player();
		player.initialize("Lucas");

		Debug.log("Hello, my name is " + player.getUsername() + "!");

		for (int i = 0; i < 4; i++) {
			player.addCard(Game.getGame().getCardFromDeck());
		}

		player.update();
		if (Game.getGame().getCurrentPlayerUsername() == null) {
			Game.getGame().setCurrentPlayerUsername(player.getUsername());
			Debug.log("It is now " + player.getUsername() + "'s turn.");
		}
		Game.getGame().update();

		while (true) {
			System.out.print("> ");
			String line = in.nextLine().toLowerCase().trim();
			System.out.println();
			if (line.equals("hand") || line.equals("cards") || line.equals("ls")) {
				List<Card> copy = new ArrayList<>(player.getHand());
				Collections.sort(copy);

				System.out.println("You have " + copy.size() + " cards:");
				for (Card card : copy) {
					System.out.println("  " + card);
				}
			} else if (line.startsWith("last")) {
				List<Card> cards = Game.getGame().getPlayedCards();
				if (line.contains(" ")) {
					int depth = Math.min(Integer.parseInt(line.split(" ", 2)[1]), cards.size());
					System.out.println("The last " + depth + " cards were as follows:");

					for (int i = 0; i < depth; i++) {
						System.out.println("  " + cards.get(cards.size() - (i + 1)));
					}
				} else {
					System.out.println("The last card was the " + cards.get(cards.size() - 1) + ".");
				}
			} else if (line.startsWith("play ")) {
				String[] split = line.split(" ", 2);
				Card card = Card.of(split[1].trim());

				player.removeCard(card);
				Game.getGame().playCard(card);

				callEvent(player, card, Event.CARD_PLACED);

				player.update();
				if (player == Player.getCurrentTurnPlayer()) {
					Game.getGame().setCurrentPlayerUsername(Player.getNextTurnPlayer().getUsername());
				}
				Game.getGame().update();
			} else if (line.equals("pull") || line.equals("draw") || line.equals("take")) {
				Card card = Game.getGame().getCardFromDeck();
				player.addCard(card);

				System.out.println("You pulled the " + card + ".");

				callEvent(player, card, Event.CARD_PULLED);

				player.update();
				if (player == Player.getCurrentTurnPlayer()) {
					Game.getGame().setCurrentPlayerUsername(Player.getNextTurnPlayer().getUsername());
				}
				Game.getGame().update();
			} else if (line.equals("turn")) {
				System.out.println(
						Game.getGame().getCurrentPlayerUsername().equals(player.getUsername()) ? "It is your turn."
								: "It is " + Game.getGame().getCurrentPlayerUsername() + "'s turn.");
			} else if (line.equals("exit") || line.equals("quit")) {
				System.exit(0);
			}
			System.out.println();
		}
	}

	private static void callEvent(Player player, Card card, String event) {
		for (Code response : RuleHandler.getRuleHandler().fire(event, player, Player.getCurrentTurnPlayer(),
				Player.getNextTurnPlayer(), card)) {
			if (response instanceof PenalizeCommand) {
				PenalizeCommand penalize = (PenalizeCommand) response;
				System.out.println(penalize.getReason());

				Card penalty = Game.getGame().getCardFromDeck();
				player.addCard(penalty);

				System.out.println("You were given the " + penalty + " as a penalty.");
			} else if (response instanceof SayCommand) {
				SayCommand say = (SayCommand) response;

				System.out.print("Got something to say?: ");
				String userResponse = in.nextLine().toLowerCase().replaceAll(".", "").trim().replaceAll(" +", " ");
				
				String actual = say.getPhrase().toLowerCase().replaceAll(".", "").trim();
				if (!(actual.contains(userResponse) && (Math.abs(actual.length() - userResponse.length()) <= 5))) {
					//System.out.println("Failure to say \"" + say.getPhrase() + "\"");
					System.out.println("Wrong saying.");

					Card penalty = Game.getGame().getCardFromDeck();
					player.addCard(penalty);

					System.out.println("You were given the " + penalty + " as a penalty."); 
				}
			}
		}
	}
}