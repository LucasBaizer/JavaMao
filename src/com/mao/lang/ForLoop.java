package com.mao.lang;

public class ForLoop extends CodeBlock {
	private Variable counter;
	private Obtainable to;

	public ForLoop(CodeBlock parent, String parameters, String block) {
		super(block);
		setParent(parent);

		String[] split = parameters.split(" to ");
		if (split.length != 2) {
			throw new CompilerError("Expecting to statement in for loop");
		}

		counter = (Variable) parseObtainable(split[0].trim());
		to = parseObtainable(split[1].trim());
		
		parseChildren();
	}

	@Override
	public ExecutionResult execute() {
		boolean ran = true;

		int start = (int) counter.obtain();
		for (; start < (int) to.obtain(); start++) {
			for (Code child : getChildren()) {
				ExecutionResult result = child.execute();
				if (result.isSuccessful()) {
					if (result.shouldExitScript()) {
						return result;
					} else if (result.shouldBreakLoop()) {
						result.setShouldBreakLoop(false);
						return result;
					}
				}
			}

			counter.setValue((int) counter.getValue() + 1);

			ran = true;
		}

		return ExecutionResultBuilder.builder(this).successful(ran).build();
	}
}
