package com.mao.lang;

public class ForLoop extends CodeBlock {
	private VarCommand toExecute;
	private Variable counter;
	private Obtainable to;

	public ForLoop(CodeBlock parent, String parameters, String block) {
		super(block);
		setParent(parent);

		String[] split = parameters.split("->");
		if (split.length != 2) {
			throw new CompilerError("Expecting \"to\" statement (->) in for loop");
		}

		String count = split[0].trim();
		if (count.startsWith("const ")) {
			throw new CompilerError("The counter variable in a for loop must be mutable");
		} else if (count.startsWith("var ")) {
			toExecute = new VarCommand(this, count.split(" ", 2)[1], false);
			counter = getVariable(toExecute.getName());
		} else {
			counter = (Variable) parseObtainable(split[0].trim());
			if (counter.isConstant()) {
				throw new CompilerError("The counter variable in a for loop must be mutable");
			}
		}

		to = parseObtainable(split[1].trim());

		parseChildren();
	}

	@Override
	public ExecutionResult execute() {
		boolean ran = true;

		if (toExecute != null) {
			toExecute.execute();
		}

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
