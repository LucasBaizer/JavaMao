package com.mao;

public enum Suit {
	SPADES(false), CLUBS(false), HEARTS(true), DIAMONDS(true);

	private boolean red;

	private Suit(boolean red) {
		this.red = red;
	}

	public boolean isBlack() {
		return !red;
	}

	public boolean isRed() {
		return red;
	}
}
