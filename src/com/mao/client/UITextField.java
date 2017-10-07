package com.mao.client;

public class UITextField extends UIObject {
	private StringBuilder text = new StringBuilder();
	private int caret;
	private int textSize;
	private int columns;
	private int caretTicks;
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
	}

	@Override
	public boolean mousePressed(Processing g) {
		return selected = super.mousePressed(g);
	}

	@Override
	public int getSortingPosition() {
		return 0;
	}
}
