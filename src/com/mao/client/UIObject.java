package com.mao.client;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.mao.Game;

public abstract class UIObject implements Comparable<UIObject> {
	private long id;
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	protected boolean selected;
	protected int selectedX = -1;
	protected int selectedY = -1;

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
	
	public boolean mousePressed(MouseEvent e, Processing g) {
		if (g.mouseX > x && g.mouseX < x + width && g.mouseY > y && g.mouseY < y + height) {
			selected = true;
			selectedX = g.mouseX;
			selectedY = g.mouseY;
			return true;
		}
		return false;
	}

	public void mouseReleased(Processing g) {
		selected = false;
		selectedX = -1;
		selectedY = -1;
	}
	
	public void keyPressed(KeyEvent e) {
	}
	
	public void keyReleased(KeyEvent e) {
	}

	public abstract void draw(Processing g);
	
	public abstract int getSortingPosition();
	
	public int compareTo(UIObject other) {
		return this.getSortingPosition() - other.getSortingPosition();
	}

	public long getID() {
		return id;
	}
}
