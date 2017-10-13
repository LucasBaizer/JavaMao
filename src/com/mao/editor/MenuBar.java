package com.mao.editor;

import javax.swing.JMenuBar;

public class MenuBar extends JMenuBar {
	private static final long serialVersionUID = 586751392309009784L;
	
	private static final MenuBar menuBar = new MenuBar();
	
	public static MenuBar getMenuBar() {
		return menuBar;
	}

	public MenuBar() {
		add(new MenuFile());
	}
}
