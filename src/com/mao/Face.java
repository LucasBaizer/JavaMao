package com.mao;

public enum Face {
	TWO("2"), THREE("3"), FOUR("4"), FIVE("5"), SIX("6"), SEVEN("7"), EIGHT("8"), NINE("9"), TEN("10"), JACK("J"), KING(
			"K"), QUEEN("Q"), ACE("A");

	private String name;

	private Face(String name) {
		this.name = name;
	}

	public String getFaceName() {
		return name;
	}

	public int getValue() {
		return this.ordinal() + 2;
	}
}
