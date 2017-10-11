package com.mao.client;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class UITextArea extends UIObject {
	private static final String ACCEPTABLE_CHARACTERS = "abcdefghijjklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUV1234567890,. \n\t:;'\"()[]{}=+-_!@#$%^&*`~<>|\\/";

	private String label;
	private StringBuilder[] text;
	private Point caret = new Point();
	private int textSize;
	private int columns;
	private int rows;
	private int caretTicks;
	private boolean caretVisible = true;
	private boolean selected;

	public UITextArea(int textSize, int rows, int columns) {
		this(null, textSize, rows, columns);
	}

	public UITextArea(String label, int textSize, int rows, int columns) {
		this.label = label;
		this.textSize = textSize;
		this.rows = rows;
		this.columns = columns;

		this.text = new StringBuilder[rows];
		for (int i = 0; i < rows; i++) {
			this.text[i] = new StringBuilder();
		}
	}

	@Override
	public void draw(Processing g) {
		g.textSize(textSize);

		int charWidth = (int) g.textWidth("A");
		int charHeight = (int) g.textAscent();

		width = (columns * charWidth) + charWidth;
		height = (rows * charHeight) + (charHeight / 2);

		g.fill(255);
		g.stroke(0);
		g.strokeWeight(textSize / 8);

		g.rect(x, y, width, height);

		g.fill(0);
		for (int row = 0; row < rows; row++) {
			g.text(text[row].toString(), x + (charWidth / 2), (y * row) + charHeight);
		}

		if (label != null) {
			g.text(label, x - g.textWidth(label), y + g.textAscent());
		}

		if (selected) {
			if (caretVisible) {
				g.noStroke();
				g.rect(x + (caret.x * charWidth + (charWidth / 2)) - textSize / 16, y + (charHeight * caret.y / 8),
						textSize / 16, charHeight * 1.5f); // TODO
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
			int localY = e.getY() - y;
			g.textSize(textSize);

			caretVisible = true;
			caretTicks = 0;

			caret.y = Math.max(0, Math.min(rows, (int) (localY / g.textAscent())));
			caret.x = Math.max(0, Math.min(text[caret.y].length(), (int) (localX / g.textWidth("A"))));

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
				if (caret.x == 0) {
					if (caret.y > 0) {
						caret.y--;
						caret.x = text[caret.y].length();
					}
				} else {
					caret.x--;
				}

				text[caret.y].deleteCharAt(caret.x);
				textChanged();
			} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				caretTicks = 0;
				caretVisible = true;
				if (caret.x == 0) {
					if (caret.y > 0) {
						caret.y--;
						caret.x = text[caret.y].length();
					}
				} else {
					caret.x--;
				}
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				caretTicks = 0;
				caretVisible = true;
				if (caret.x == text[caret.y].length()) {
					if (caret.y < rows) {
						caret.y++;
						caret.x = text[caret.y].length();
					}
				} else {
					caret.x++;
				}
			} else if (ACCEPTABLE_CHARACTERS.contains(Character.toString(e.getKeyChar()))) {
				if (text[caret.y].length() < columns) {
					caretTicks = 0;
					caretVisible = true;
					text[caret.y].insert(Math.min(++caret.x, text[caret.y].length()), e.getKeyChar());
					textChanged();
				}
			}
		}
	}

	public void textChanged() {
	}

	public String getText() {
		return text.toString();
	}

	@Override
	public int getSortingPosition() {
		return 0;
	}
}
