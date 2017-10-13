package com.mao.client;

public class UILabel extends UIObject {
	private String text;
	private int fontSize;

	public UILabel(String text, int fontSize) {
		this.text = text;
		this.fontSize = fontSize;
	}

	@Override
	public void draw(Processing g) {
		g.textSize(fontSize);
		
		width = (int) g.textWidth(text);
		height = (int) g.textAscent();
		
		g.text(text, x, y);
	}
	
	public UILabel setText(String text) {
		this.text = text;
		return this;
	}

	@Override
	public int getSortingPosition() {
		return 0;
	}
}
