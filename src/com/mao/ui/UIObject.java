package com.mao.ui;

import com.mao.Game;

public abstract class UIObject {
	private long id;
	protected int x;
	protected int y;
	protected int width;
	protected int height;

	public UIObject() {
		this(0, 0, 0, 0);
	}

	public UIObject(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		this.id = Game.getRandomInstance().nextLong();
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public void initialize(Processing g) {
	}

	public abstract void draw(Processing g);

	public long getID() {
		return id;
	}
}
