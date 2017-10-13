package com.mao.lang;

public class CompilerError extends RuntimeException {
	private static final long serialVersionUID = 1123986107673377378L;

	public CompilerError(String message) {
		super(message);
	}
}
