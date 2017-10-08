package com.mao.client;

import java.awt.Color;
import java.awt.event.MouseEvent;

public class UINotification extends UIObject {
	public static final int SORTING_POSITION_START = 10000;
	private static int notifications = 0;

	private String message;
	private Color color;
	private int index;

	public UINotification(String msg, Color color) {
		this.message = msg;
		this.color = color;

		index = notifications++;
	}

	@Override
	public void draw(Processing g) {
		width = Screen.getSize().width / 5;
		height = Screen.getSize().height / 10;

		x = (Screen.getSize().width / 2) - (width / 2);
		y = index * height;

		g.fill(color.getRed(), color.getGreen(), color.getBlue());
		g.rect(x, y, width, height);

		g.fill(color.equals(Color.WHITE) ? 0 : 255);
		g.textSize((width / 12f) / message.split("\r\n|\r|\n").length);
		g.text(message, (Screen.getSize().width / 2) - (g.textWidth(message) / 2), y + (height / 2));
	}

	@Override
	public boolean mousePressed(MouseEvent e, Processing g) {
		boolean pressed = super.mousePressed(e, g);
		if (pressed) {
			notifications--;
			g.removeUIObject(this);
		}
		return pressed;
	}

	@Override
	public int getSortingPosition() {
		return SORTING_POSITION_START + notifications;
	}
}
