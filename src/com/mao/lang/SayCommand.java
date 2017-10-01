package com.mao.lang;

public class SayCommand extends Code {
	private Obtainable phrase;

	public SayCommand(Obtainable phrase) {
		this.phrase = phrase;
	}

	@Override
	public ExecutionResult execute() {
		return ExecutionResultBuilder.builder(this).shouldExitScript().successful().build();
	}

	public String getPhrase() {
		return phrase.obtain().toString();
	}
}
