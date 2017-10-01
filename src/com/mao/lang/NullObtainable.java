package com.mao.lang;

public class NullObtainable implements Obtainable {
	@Override
	public Object obtain() {
		return new NullObject();
	}
}
