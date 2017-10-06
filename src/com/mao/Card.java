package com.mao;

import java.io.Serializable;

public class Card implements Serializable, Comparable<Card> {
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

	public static Card of(String name) {
		String[] split = name.split(" of ");
		return new Card(Face.valueOf(split[0].toUpperCase()), Suit.valueOf(split[1].toUpperCase()));
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((face == null) ? 0 : face.hashCode());
		result = prime * result + ((suit == null) ? 0 : suit.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		if (face != other.face)
			return false;
		if (suit != other.suit)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return face.name().toLowerCase() + " of " + suit.name().toLowerCase();
	}
	
	public int getCardNumber() {
		return (this.suit.ordinal() * 13) + this.face.ordinal();
	}

	@Override
	public int compareTo(Card o) {
		return this.face.ordinal() - o.face.ordinal();
	}
}
