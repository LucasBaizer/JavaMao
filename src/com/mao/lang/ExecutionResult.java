package com.mao.lang;

public class ExecutionResult {
	private boolean successful;
	private boolean exitScript;
	private boolean breakLoop;
	private boolean returnMethod;
	private Object returnValue;
	private Code source;

	public boolean isSuccessful() {
		return successful;
	}

	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}

	public boolean shouldExitScript() {
		return exitScript;
	}

	public void setShouldExitScript(boolean exitScript) {
		this.exitScript = exitScript;
	}

	public boolean shouldReturnMethod() {
		return returnMethod;
	}

	public void setShouldReturnMethod(boolean returnMethod) {
		this.returnMethod = returnMethod;
	}

	public Object getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(Object returnValue) {
		this.returnValue = returnValue;
	}

	public Code getSource() {
		return source;
	}

	public void setSource(Code source) {
		this.source = source;
	}

	public boolean shouldBreakLoop() {
		return breakLoop;
	}

	public void setShouldBreakLoop(boolean breakLoop) {
		this.breakLoop = breakLoop;
	}

	@Override
	public String toString() {
		return "ExecutionResult[successful=" + successful + ",exitScript=" + exitScript + ",breakLoop=" + breakLoop
				+ ",returnMethod=" + returnMethod + ",returnValue=" + returnValue + ",source="
				+ source.getClass().getSimpleName() + "]";
	}
}
