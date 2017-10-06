package com.mao.ui;

import java.util.HashMap;

import com.mao.Card;
import com.mao.Game;

import processing.core.PImage;

public class UIPlayedCards extends UIObject {
	public static final int SORTING_POSITION_START = 1000;

	private HashMap<String, PImage> cardCache = new HashMap<>();

	public UIPlayedCards() {
		super(0, 0, 0, 0);
	}

	@Override
	public void draw(Processing g) {
		width = (int) (Screen.getSize().width / 7.68);
		height = (int) (Screen.getSize().height / 2.97520661f);

		x = (Screen.getSize().width / 2) - (width / 2);
		y = (Screen.getSize().height / 2) - (height / 2);

		int limit = Math.min(Game.getGame().getPlayedCards().size(), 5);
		for (int i = limit; i >= 1 ; i--) {
			String imageName = getImageName(
					Game.getGame().getPlayedCards().get(Game.getGame().getPlayedCards().size() - i));
			PImage image = cardCache.get(imageName);
			if (image == null) {
				image = g.getImage(imageName);
				cardCache.put(imageName, image);
			}
			if (image.width != width || image.height != height) {
				image = g.reloadImage(imageName);
				image.resize(width, height);
				cardCache.put(imageName, image);
			}
			g.image(image, x + ((limit - i) * (width / 3)), y);
		}
	}

	@Override
	public int getSortingPosition() {
		return SORTING_POSITION_START;
	}

	private String getImageName(Card card) {
		return (card.getFace().isNonNumericalCard() ? card.getFace().getNonNumericalName()
				: card.getFace().getFaceName()) + "_of_" + card.getSuit().name().toLowerCase();
	}
}
