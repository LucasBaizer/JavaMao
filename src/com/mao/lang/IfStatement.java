package com.mao.lang;

import java.util.Objects;

public class IfStatement extends CodeBlock {
	private Obtainable leftStatement;
	private Obtainable rightStatement;
	private boolean positive;

	public IfStatement(CodeBlock parent, String condition, String block) {
		super(block);
		super.setParent(parent);
		super.parseChildren();

		positive = condition.contains("==");
		String[] split = positive ? condition.split("==") : condition.split("!=");
		if (split.length != 2) {
			throw new CompilerError("Expecting conditional in if statement (==/!=)");
		}

		leftStatement = parseObtainable(split[0].trim());
		rightStatement = parseObtainable(split[1].trim());
	}

	@Override
	public ExecutionResult execute() {
		boolean shouldBreak = false;

		Object a = leftStatement.obtain();
		Object b = rightStatement.obtain();

		if (a instanceof Number) {
			a = ((Number) a).doubleValue();
		}
		if (b instanceof Number) {
			b = ((Number) b).doubleValue();
		}

		boolean successful = Objects.equals(a, b) == positive;

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
