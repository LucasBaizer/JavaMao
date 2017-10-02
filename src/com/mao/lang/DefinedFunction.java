package com.mao.lang;

import java.util.ArrayList;
import java.util.List;

public class DefinedFunction extends CodeBlock {
	private String name;
	private List<Variable> params = new ArrayList<>();

	public DefinedFunction(CodeBlock parent, String name, String parameters, String block) {
		super(block);
		setParent(parent);

		this.name = name;

		for (String param : parameters.split(",")) {
			Variable var = new Variable(param.trim()).setConstant(true);
			params.add(var);
			addVariable(var);
		}

		parent.addMethod(new Method(name, params.size(), (params) -> {
			return run(params).getReturnValue();
		}));

		parseChildren();
	}

	private ExecutionResult run(Object[] inputParams) {
		if (inputParams.length != params.size()) {
			throw new RuntimeException("Function " + name + " takes " + params.size() + " parameters; "
					+ inputParams.length + " were given");
		}

		for (int i = 0; i < params.size(); i++) {
			params.get(i).setValue(inputParams[i]);
		}

		for (Code child : getChildren()) {
			ExecutionResult result = child.execute();
			if (result.isSuccessful()) {
				if (result.shouldReturnMethod()) {
					for (int i = 0; i < params.size(); i++) {
						params.get(i).setValue(null);
					}
					return result;
				}
			}
		}

		for (int i = 0; i < params.size(); i++) {
			params.get(i).setValue(null);
		}

		return ExecutionResultBuilder.builder(this).successful().breakLoop().shouldReturn().build();
	}

	@Override
	public ExecutionResult execute() {
		return ExecutionResultBuilder.builder(this).successful().build();
	}
}
