package com.mao.lang;

public abstract class CodeBlock {
	private String block;
	
	public CodeBlock(String block) {
		this.block = block;
	}

	public String getBlock() {
		return block;
	}

	public void setBlock(String block) {
		this.block = block;
	}
	
	public abstract boolean execute();
}
