package com.mao.lang;

public class ExecutionResultBuilder {
	private ExecutionResult result = new ExecutionResult();

	private ExecutionResultBuilder(Code source) {
		result.setSource(source);
	}

	public static ExecutionResultBuilder builder(Code source) {
		return new ExecutionResultBuilder(source);
	}

	public ExecutionResultBuilder successful() {
		return successful(true);
	}
	
	public ExecutionResultBuilder successful(boolean success) {
		result.setSuccessful(success);
		return this;
	}
	
	public ExecutionResultBuilder breakLoop() {
		return breakLoop(true);
	}
	
	public ExecutionResultBuilder breakLoop(boolean should) {
		result.setShouldBreakLoop(should);
		return this;
	}

	public ExecutionResultBuilder shouldExitScript() {
		result.setShouldExitScript(true);
		return this;
	}

	public ExecutionResult build() {
		return result;
	}
}
