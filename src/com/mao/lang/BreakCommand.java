package com.mao.lang;

public class BreakCommand extends Code {
	@Override
	public ExecutionResult execute() {
		return ExecutionResultBuilder.builder(this).successful().breakLoop().build();
	}
}
