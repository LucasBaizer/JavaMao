package com.mao.lang;

public class SetVarCommand extends Code {
	private Variable var;
	private Obtainable val;

	public SetVarCommand(Variable var, Obtainable value) {
		this.var = var;
		this.val = value;

		if (var.isConstant()) {
			throw new CompilerError("Cannot assign new value to constant variable " + var.getName());
		}
	}

	@Override
	public ExecutionResult execute() {
		var.setValue(val.obtain());

		return ExecutionResultBuilder.builder(this).build();
	}
}
