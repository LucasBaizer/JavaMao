package com.mao.lang;

public class ExitCommand extends Code {
	@Override
	public ExecutionResult execute() {
		return ExecutionResultBuilder.builder(this).shouldExitScript().successful().build();
	}
}
