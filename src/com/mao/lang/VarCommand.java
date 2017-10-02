package com.mao.lang;

public class VarCommand extends Code {
	private String name;
	private String definition;

	public VarCommand(CodeBlock parent, String definition, boolean constant) {
		setParent(parent);

		this.definition = definition;

		String[] split = definition.split("=");
		String name = split[0].trim();
		
		this.name = name;

		Variable existing = getVariable(name);
		if (split.length != 2) {
			if (existing == null) {
				if (constant) {
					throw new CompilerError("Constant variabled must be assigned a value at compile-time");
				}
				getParent().addVariable(new Variable(name));
			}
		} else {
			Object value = parseObtainable(split[1].trim()).obtain();
			if (existing == null) {
				getParent().addVariable(new Variable(name, value).setConstant(constant));
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
