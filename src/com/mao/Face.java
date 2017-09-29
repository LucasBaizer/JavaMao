package com.mao;

public enum Face {
	TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, KING, QUEEN, ACE;

	public int getValue() {
		return this.ordinal() + 2;
	}
}
