package com.mao.lang;

public class IfStatement extends CodeBlock {
	private Obtainable leftStatement;
	private Obtainable rightStatement;
	private boolean positive;

	public IfStatement(CodeBlock parent, String condition, String block) {
		super(block);
		super.setParent(parent);
		super.parseChildren();

		positive = condition.contains(" is ");
		String[] split = positive ? condition.split("is") : condition.split("not");
		if (split.length != 2) {
			throw new CompilerError("Expecting conditional in if statement (is/not)");
		}

		leftStatement = parseObtainable(split[0].trim());
		rightStatement = parseObtainable(split[1].trim());
	}

	@Override
	public ExecutionResult execute() {
		boolean shouldBreak = false;
		boolean successful = leftStatement.obtain().equals(rightStatement.obtain()) == positive;
		if (successful) {
			for (Code child : getChildren()) {
				ExecutionResult result = child.execute();
				if (result.isSuccessful()) {
					if (result.shouldExitScript()) {
						return result;
					} else if (result.shouldBreakLoop()) {
						shouldBreak = true;
					}
				}
			}
		}
		return ExecutionResultBuilder.builder(this).successful(successful).breakLoop(shouldBreak).build();
	}
}
