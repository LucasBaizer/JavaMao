package com.mao.lang;

public class VarCommand extends Code {
	private String definition;

	public VarCommand(CodeBlock parent, String definition) {
		setParent(parent);

		this.definition = definition;

		String[] split = definition.split("=");
		String name = split[0].trim();

		Variable existing = getVariable(name);
		if (split.length != 2) {
			if (existing == null) {
				getParent().addVariable(new Variable(name));
			}
		} else {
			Object value = parseObtainable(split[1].trim()).obtain();
			if (existing == null) {
				getParent().addVariable(new Variable(name, value));
			}
		}
	}

	@Override
	public ExecutionResult execute() {
		String[] split = definition.split("=");
		String name = split[0].trim();

		Variable existing = getVariable(name);
		if (split.length != 2) {
			if (existing != null) {
				existing.setValue(null);
			}
		} else {
			Object value = parseObtainable(split[1].trim()).obtain();
			if (existing != null) {
				existing.setValue(value);
			}
		}

		return ExecutionResultBuilder.builder(this).successful().build();
	}
}
