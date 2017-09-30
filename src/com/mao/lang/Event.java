package com.mao.lang;

public class Event extends CodeBlock {
	private String name;
	
	public Event(String name, String block) {
		super(block);
		
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}

	@Override
	public boolean execute() {
		return true;
	}
	
	@Override
	public String toString() {
		return "Event::" + name;
	}
}
