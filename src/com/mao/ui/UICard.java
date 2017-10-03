package com.mao.ui;

import com.mao.Card;

import processing.core.PImage;

public class UICard extends UIObject {
	private Card card;
	private PImage image;

	public UICard(Card card, int x, int y) {
		super(x, y, Screen.getSize().width / 5, Screen.getSize().height / 5);

		this.card = card;
	}
	
	@Override
	public void initialize(Processing g) {
		image = g.getImage(card.getSuit().name().toLowerCase());
	}

	@Override
	public void draw(Processing g) {
		width = Screen.getSize().width / 6;
		height = (int) (Screen.getSize().height / 2.5f);

		g.fill(255);
		g.strokeWeight(4f);
		g.rect(x, y, width, height, width / 10, width / 10, width / 10, width / 10);
		g.strokeWeight(2f);
		g.rect(x + (width / 6), y + (height / 8), width - (width / 3), height - (height / 4), width / 20, width / 20,
				width / 20, width / 20);

		if (card.getSuit().isRed()) {
			g.fill(255, 0, 0);
		} else {
			g.fill(0);
		}

		String text = card.getFace().getFaceName();

		g.textSize((width / 7) / text.length());
		g.text(text, x + (width / 20), y + (height / 8));
		g.text(text, (x + width - (width / 20)) - g.textWidth(text),
				(y + height) - (height / 8) + (g.textAscent() / 1.5f));
		
		image.resize(width / 7, width / 7);
		g.image(image, x + (width / 20), y + (height / 4));
	}

	@Override
	public long getID() {
		return card.hashCode();
	}
}
