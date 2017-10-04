package com.mao.ui;

import com.mao.Card;

import processing.core.PImage;

public class UICard extends UIObject {
	private Card card;
	private PImage image;
	private int cardIndex;
	private int totalCards;

	public UICard(Card card, int index, int total) {
		super(0, 0, 0, 0);

		this.card = card;
	}

	@Override
	public void initialize(Processing g) {
		image = g.getImage((card.getFace().isNonNumericalCard() ? card.getFace().getNonNumericalName()
				: card.getFace().getFaceName()) + "_of_" + card.getSuit().name().toLowerCase());
		image.resize(250, 363);
	}

	@Override
	public void draw(Processing g) {
		width = Screen.getSize().width / 6;
		height = (int) (Screen.getSize().height / 2.5f);

		g.image(image, x, y);
	}
	
	@Override
	public void mousePressed(Processing g) {
		super.mousePressed(g);
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
}
