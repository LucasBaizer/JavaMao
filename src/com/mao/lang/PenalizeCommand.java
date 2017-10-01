package com.mao.lang;

public class PenalizeCommand extends Code {
	private Obtainable reason;

	public PenalizeCommand(Obtainable reason) {
		this.reason = reason;
	}

	public String getReason() {
		return reason.obtain().toString();
	}

	@Override
	public ExecutionResult execute() {
		return ExecutionResultBuilder.builder(this).successful(true).shouldExitScript().build();
	}
}
