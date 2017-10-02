package com.mao.lang;

public class ReturnCommand extends Code {
	private Obtainable toReturn;

	public ReturnCommand(CodeBlock parent, String toReturn) {
		setParent(parent);

		if (toReturn != null) {
			this.toReturn = parseObtainable(toReturn);
		}
	}

	@Override
	public ExecutionResult execute() {
		return ExecutionResultBuilder.builder(this).successful().breakLoop().shouldReturn()
				.returnValue(toReturn == null ? null : toReturn.obtain()).build();
	}
}
