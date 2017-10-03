package com.mao;

public enum Suit {
	CLUBS(false), HEARTS(true), SPADES(false), DIAMONDS(true);

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
