package com.mao.lang;

public class ExecutionResult {
	private boolean successful;
	private boolean exitScript;
	private boolean breakLoop;
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
		return "ExecutionResult[successful=" + successful + ",exitScript=" + exitScript + ",source="
				+ source.getClass().getSimpleName() + "]";
	}
}
