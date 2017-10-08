package com.mao.client;

import java.awt.event.MouseEvent;

public class UIMenuButton extends UIObject {
	public static final int SORTING_POSITION_START = 0;
	public static final int MENU_BUTTONS = 3;

	private int index;
	private int font;
	private String text;
	private Runnable action;

	public UIMenuButton(int index, int fontSize, String text, Runnable action) {
		this.index = index;
		this.font = fontSize;
		this.text = text;
		this.action = action;
	}

	@Override
	public void draw(Processing g) {
		width = Screen.getSize().width / 6;
		height = Screen.getSize().height / 6;
		x = (Screen.getSize().width / 2) - (width / 2);
		y = (Screen.getSize().height / 2) + ((index - MENU_BUTTONS) * (height + (height / 3)) + (((height + height / 3) / 2) * MENU_BUTTONS));

		g.fill(255);
		g.stroke(0);
		g.strokeWeight(width / 25);
		g.rect(x, y, width, height, width / 20, width / 20, width / 20, width / 20);

		g.fill(0);
		g.textSize(font);
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
		return SORTING_POSITION_START + index;
	}
}
