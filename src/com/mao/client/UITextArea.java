package com.mao.client;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class UITextArea extends UIObject {
	private static final String ACCEPTABLE_CHARACTERS = "abcdefghijjklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUV1234567890,. \t;'\"()[]{}=+-_!@#$%^&*`~<>|\\/";

	private String label;
	private StringBuilder[] text;
	private Point caret;
	private int textSize;
	private int columns;
	private int rows;
	private int caretTicks;
	private boolean caretVisible = true;
	private boolean selected;

	public UITextArea(int textSize, int columns) {
		this(null, textSize, columns);
	}

	public UITextArea(String label, int textSize, int columns) {
		this.label = label;
		this.textSize = textSize;
		this.columns = columns;
		text = new StringBuilder[columns];
		// TODO
	}

	@Override
	public void draw(Processing g) {
		g.textSize(textSize);

		int charWidth = (int) g.textWidth("A");

		width = (columns * charWidth) + charWidth;
		height = (int) (g.textAscent() + (g.textAscent() / 2));

		g.fill(255);
		g.stroke(0);
		g.strokeWeight(textSize / 8);

		g.rect(x, y, width, height);

		g.fill(0);
		g.text(text.toString(), x + (charWidth / 2), y + g.textAscent());

		if (label != null) {
			g.text(label, x - g.textWidth(label), y + g.textAscent());
		}

		if (selected) {
			if (caretVisible) {
				g.noStroke();
				g.rect(x + (caret * charWidth + (charWidth / 2)) - textSize / 16, y + (height / 8), textSize / 16,
						height - (height / 4));
				g.stroke(0);
			}

			if (++caretTicks == 30) {
				caretTicks = 0;
				caretVisible = !caretVisible;
			}
		}
	}

	public int getEntireWidth(Processing g) {
		g.textSize(textSize);

		int left = x;
		if (label != null) {
			left -= g.textWidth(label);
		}

		return (x + width) - left;
	}

	@Override
	public boolean mousePressed(MouseEvent e, Processing g) {
		if (selected = super.mousePressed(e, g)) {
			int localX = e.getX() - x;
			g.textSize(textSize);

			caretVisible = true;
			caretTicks = 0;
			caret = Math.max(0, Math.min(text.length(), (int) (localX / g.textWidth("A"))));

			return true;
		} else {
			caretVisible = false;
			caretTicks = 0;
		}
		return false;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (selected) {
			if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
				if (caret > 0) {
					text.deleteCharAt(--caret);
					textChanged();
				}
			} else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
				if (caret < text.length()) {
					text.deleteCharAt(caret);
					textChanged();
				}
			} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				caretTicks = 0;
				caretVisible = true;
				caret = Math.max(caret - 1, 0);
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				caretTicks = 0;
				caretVisible = true;
				caret = Math.min(caret + 1, text.length());
			} else if (ACCEPTABLE_CHARACTERS.contains(Character.toString(e.getKeyChar()))) {
				if (text.length() < columns) {
					caretTicks = 0;
					caretVisible = true;
					text.insert(caret++, e.getKeyChar());
					textChanged();
				}
			}
		}
	}

	public void textChanged() {
	}

	public UITextArea setText(String text) {
		this.text = new StringBuilder(text);
		return this;
	}

	public String getText() {
		return text.toString();
	}

	@Override
	public int getSortingPosition() {
		return 0;
	}
}
