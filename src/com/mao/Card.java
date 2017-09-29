package com.mao;

import java.io.Serializable;

public class Card implements Serializable {
	private static final long serialVersionUID = 5510774449741346517L;
	private Suit suit;
	private Face face;

	private Card(Face face, Suit suit) {
		this.face = face;
		this.suit = suit;
	}

	public static Card of(Face face, Suit suit) {
		return new Card(face, suit);
	}

	public static Card getRandomCard() {
		return new Card(Face.values()[Game.getRandomInstance().nextInt(Face.values().length)],
				Suit.values()[Game.getRandomInstance().nextInt(Suit.values().length)]);
	}

	public Suit getSuit() {
		return this.suit;
	}

	public Face getFace() {
		return this.face;
	}
}
