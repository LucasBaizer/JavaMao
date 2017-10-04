package com.mao;

public enum Face {
	ACE("A"), TWO("2"), THREE("3"), FOUR("4"), FIVE("5"), SIX("6"), SEVEN("7"), EIGHT("8"), NINE("9"), TEN("10"), JACK(
			"J"), QUEEN("Q"), KING("K");

	private String name;

	private Face(String name) {
		this.name = name;
	}

	public String getFaceName() {
		return name;
	}

	public int getValue() {
		return this.ordinal() + 1;
	}

	public boolean isNonNumericalCard() {
		return this == JACK || this == KING || this == QUEEN || this == ACE;
	}

	public String getNonNumericalName() {
		if (!isNonNumericalCard()) {
			return null;
		}
		switch (this) {
		case JACK:
			return "jack";
		case QUEEN:
			return "queen";
		case KING:
			return "king";
		case ACE:
			return "ace";
		default:
			return null;
		}
	}
}
