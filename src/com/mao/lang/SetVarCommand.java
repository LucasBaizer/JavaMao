package com.mao.lang;

public class SetVarCommand extends Code {
	private Variable var;
	private Obtainable val;
	
	public SetVarCommand(Variable var, Obtainable value) {
		this.var = var;
		this.val = value;
	}

	@Override
	public ExecutionResult execute() {
		var.setValue(val.obtain());
		
		return ExecutionResultBuilder.builder(this).build();
	}
}
