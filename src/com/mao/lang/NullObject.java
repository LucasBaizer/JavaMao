package com.mao.lang;

public class NullObject {
	@Override
	public boolean equals(Object obj) {
		return false;
	}

	@Override
	public int hashCode() {
		return -1;
	}

	@Override
	public String toString() {
		return "null";
	}
}
