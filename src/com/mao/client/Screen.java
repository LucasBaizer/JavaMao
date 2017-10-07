package com.mao.client;

import java.awt.Dimension;

public class Screen {
	private static Dimension size;
	
	public static Dimension getSize() {
		return size;
	}
	
	public static void setSize(Dimension size) {
		Screen.size = size;
	}
}
