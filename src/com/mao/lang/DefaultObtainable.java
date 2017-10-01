package com.mao.lang;

public class DefaultObtainable implements Obtainable {
	private Object object;

	public DefaultObtainable(Object object) {
		this.object = object;
	}

	@Override
	public Object obtain() {
		return object;
	}
}
