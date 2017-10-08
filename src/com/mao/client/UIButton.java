package com.mao.client;

import java.awt.event.MouseEvent;

public class UIButton extends UIObject {
	private int font;
	private String text;
	private Runnable action;

	public UIButton(int fontSize, String text, Runnable action) {
		this.font = fontSize;
		this.text = text;
		this.action = action;
	}

	@Override
	public void draw(Processing g) {
		g.textSize(font);

		width = (int) (g.textWidth(text) + g.textWidth("A"));
		height = (int) (g.textAscent() + (g.textAscent() / 2));

		g.fill(255);
		g.stroke(0);
		g.strokeWeight(font / 8);
		g.rect(x, y, width, height, width / 20, width / 20, width / 20, width / 20);

		g.fill(0);
		g.text(text, x + (width / 2) - (g.textWidth(text) / 2), y + (height / 2) + (g.textAscent() / 4));
	}

	@Override
	public boolean mousePressed(MouseEvent evt, Processing g) {
		boolean pressed = super.mousePressed(evt, g);
		if (pressed) {
			action.run();
		}
		return pressed;
	}

	@Override
	public int getSortingPosition() {
		return 0;
	}
}
