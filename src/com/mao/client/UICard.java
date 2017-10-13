package com.mao.client;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import com.mao.Card;
import com.mao.Game;
import com.mao.Player;
import com.mao.lang.Event;

import processing.core.PImage;
import voce.SpeechInterface;

public class UICard extends UIObject {
	public static final int SORTING_POSITION_START = 0;

	public Card card;
	private PImage image;
	private int cardIndex;
	private int totalCards;

	public UICard(Card card, int index, int total) {
		super(0, 0, 0, 0);

		this.cardIndex = index;
		this.totalCards = total;
		this.card = card;
	}

	@Override
	public void initialize(Processing g) {
		image = g.getImage(getImageName());
	}

	@Override
	public void draw(Processing g) {
		int oldWidth = width;
		int oldHeight = height;
		width = (int) (Screen.getSize().width / 7.68);
		height = (int) (Screen.getSize().height / 2.97520661f);
		if (oldWidth != width || oldHeight != height) {
			image = g.reloadImage(getImageName());
			image.resize(width, height);
		}

		x = (int) (cardIndex * (width / 2f));
		y = (int) (Screen.getSize().height - (height / 1.5f));

		g.image(image, x, y);
	}

	@Override
	public boolean mousePressed(MouseEvent evt, Processing g) {
		boolean pressed = super.mousePressed(evt, g);
		if (pressed && !MainClient.lobby.hasUserWon()) {
			Game.getGame().playCard(card);
			MainClient.player.removeCard(card);

			List<String> responses = MainClient.callEvent(MainClient.player, card, Event.CARD_PLACED);
			System.out.println("Responses: " + responses);

			Thread waitThread = new Thread(() -> {
				UILabel label = new UILabel("Turn ends in 5 seconds...", 48) {
					@Override
					public void draw(Processing g) {
						x = (int) (Screen.getSize().width - (width * 1.1f));
						y = Screen.getSize().height / 16;

						super.draw(g);
					}
				};
				g.addUIObject(label);
				for (int i = 5; i > 0; i--) {
					label.setText("Turn ends in " + i + " seconds...");

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				g.removeUIObject(label);

				if (responses.size() == 0) {
					MainClient.player.update();
					if (MainClient.player == Player.getCurrentTurnPlayer()) {
						Game.getGame().setCurrentPlayerUsername(Player.getNextTurnPlayer().getUsername());
					}
					Game.getGame().update();
				}
			});
			waitThread.start();

			if (responses.size() > 0) {
				Thread thread = new Thread(() -> {
					Speech.consume(false);

					try {
						waitThread.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					List<String> saids = new ArrayList<>();
					while (SpeechInterface.getRecognizerQueueSize() > 0) {
						String str = SpeechInterface.popRecognizedString();
						if (str.equals("mao")) {
							if (responses.contains("mao")) {
								saids.add(str);
							}
						} else {
							saids.add(str);
						}
					}

					List<String> saidsCopy = new ArrayList<>(saids);
					for (String said : saids) {
						if (responses.contains(said)) {
							System.out.println("Player said: " + said);
							responses.remove(said);
							saidsCopy.remove(said);
						}
					}

					if (responses.size() > 0) {
						while (saidsCopy.size() > 0) {
							String said = saidsCopy.remove(0);
							System.out.println("Given for wrong saying: " + said);
							MainClient.player.addCard(Game.getGame().getCardFromDeck());
							Processing.getProcessing().notify("Wrong saying.", "Wrong saying.\n(said \"" + said + "\")",
									Color.RED);
							responses.remove(0);
						}
					}

					for (String left : responses) {
						System.out.println("Did not say: " + left);
						MainClient.player.addCard(Game.getGame().getCardFromDeck());
						Processing.getProcessing().notify("Failure to say:\n" + left, "Failure to say:\n" + left,
								Color.RED);
					}

					Speech.consume(true);

					if (MainClient.player.getHand().size() == 0) {
						Processing.getProcessing().notify("You won!", "User won!", Color.GREEN);
					}

					MainClient.player.update();
					if (MainClient.player == Player.getCurrentTurnPlayer()) {
						Game.getGame().setCurrentPlayerUsername(Player.getNextTurnPlayer().getUsername());
					}
					Game.getGame().update();
				});
				thread.start();
			}
		}
		return pressed;
	}

	@Override
	public long getID() {
		return card.hashCode();
	}

	public int getCardIndex() {
		return cardIndex;
	}

	public void setCardIndex(int cardIndex) {
		this.cardIndex = cardIndex;
	}

	public int getTotalCards() {
		return totalCards;
	}

	public void setTotalCards(int totalCards) {
		this.totalCards = totalCards;
	}

	private String getImageName() {
		return (card.getFace().isNonNumericalCard() ? card.getFace().getNonNumericalName()
				: card.getFace().getFaceName()) + "_of_" + card.getSuit().name().toLowerCase();
	}

	@Override
	public int getSortingPosition() {
		return SORTING_POSITION_START + cardIndex;
	}
}
