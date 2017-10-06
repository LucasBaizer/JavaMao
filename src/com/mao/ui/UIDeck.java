package com.mao.ui;

import com.mao.Card;
import com.mao.Game;
import com.mao.Player;
import com.mao.lang.Event;

import processing.core.PImage;

public class UIDeck extends UIObject {
	public static final int SORTING_POSITION = 100;

	private PImage image;

	@Override
	public void initialize(Processing g) {
		image = g.getImage("card_back");
	}

	public void draw(Processing g) {
		int oldWidth = width;
		int oldHeight = height;
		width = (int) (Screen.getSize().width / 7.68);
		height = (int) (Screen.getSize().height / 2.97520661f);
		if (oldWidth != width || oldHeight != height) {
			image = g.reloadImage("card_back");
			image.resize(width, height);
		}

		x = width;
		y = (Screen.getSize().height / 2) - (height / 2);

		g.image(image, x, y);
	}

	@Override
	public boolean mousePressed(Processing g) {
		boolean pressed = super.mousePressed(g);
		if (pressed) {
			Card card = Game.getGame().getCardFromDeck();
			MainClient.player.addCard(card);

			MainClient.callEvent(MainClient.player, card, Event.CARD_PULLED);

			MainClient.player.update();
			if (MainClient.player == Player.getCurrentTurnPlayer()) {
				Game.getGame().setCurrentPlayerUsername(Player.getNextTurnPlayer().getUsername());
			}
			Game.getGame().update();
		}
		return pressed;
	}

	public int getSortingPosition() {
		return SORTING_POSITION;
	}
}
