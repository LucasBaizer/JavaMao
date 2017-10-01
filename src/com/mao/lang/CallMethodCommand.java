package com.mao.lang;

public class CallMethodCommand extends Code {
	private MethodReference reference;

	public CallMethodCommand(MethodReference ref) {
		this.reference = ref;
	}

	@Override
	public ExecutionResult execute() {
		reference.obtain();

		return ExecutionResultBuilder.builder(this).successful().build();
	}
}
