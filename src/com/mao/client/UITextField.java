package com.mao.client;

import java.awt.event.KeyEvent;

public class UITextField extends UIObject {
	private StringBuilder text = new StringBuilder();
	private int caret;
	private int textSize;
	private int columns;
	private int caretTicks;
	private boolean caretVisible = true;
	private boolean selected;

	public UITextField(int x, int y, int textSize, int columns) {
		super(x, y, 0, 0);

		this.textSize = textSize;
		this.columns = columns;
	}

	@Override
	public void draw(Processing g) {
		g.textSize(textSize);

		width = (int) ((columns * g.textWidth("A")) + g.textWidth("A"));
		height = (int) (g.textAscent() + (g.textAscent() / 2));

		g.fill(255);
		g.stroke(0);
		g.strokeWeight(textSize / 8);

		g.rect(x, y, width, height);

		g.fill(0);
		g.text(text.toString(), x + (g.textWidth("A") / 2), y + g.textAscent());

		if (selected) {
			if(caretVisible) {
				g.noStroke();
				g.rect(x + (caret * g.textWidth("A")), y + (height / 8), textSize / 8, height - (height / 8));
				g.stroke(0);
			}
			
			if (++caretTicks == 60) {
				caretTicks = 0;
				caretVisible = !caretVisible;
			}
		}
	}

	@Override
	public boolean mousePressed(Processing g) {
		return selected = super.mousePressed(g);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (selected) {
			if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
				if (caret > 0) {
					text.deleteCharAt(--caret);
				}
			} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				caret = Math.max(caret - 1, 0);
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				caret = Math.min(caret + 1, columns - 1);
			} else {
				if (text.length() < columns) {
					text.insert(caret++, e.getKeyChar());
				}
			}
		}
	}

	@Override
	public int getSortingPosition() {
		return 0;
	}
}
