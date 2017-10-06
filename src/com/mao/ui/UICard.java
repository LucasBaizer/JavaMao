package com.mao.ui;

import com.mao.Card;
import com.mao.Game;
import com.mao.Player;
import com.mao.lang.Event;

import processing.core.PImage;

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
	public boolean mousePressed(Processing g) {
		boolean pressed = super.mousePressed(g);
		if (pressed) {
			System.out.println("Playing the " + card + ".");
			Game.getGame().playCard(card);
			MainClient.player.removeCard(card);
			
			MainClient.callEvent(MainClient.player, card, Event.CARD_PLACED);

			MainClient.player.update();
			if (MainClient.player == Player.getCurrentTurnPlayer()) {
				Game.getGame().setCurrentPlayerUsername(Player.getNextTurnPlayer().getUsername());
			}
			Game.getGame().update();
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
