package com.mao.editor;

import javax.swing.JFrame;

public class EditorFrame extends JFrame {
	private static final long serialVersionUID = -6108266143910191681L;

	private static EditorFrame instance = new EditorFrame();

	public static EditorFrame getFrame() {
		return instance;
	}

	public EditorFrame() {
		super("MSL Rule Editor");

		getContentPane().add(EditorPanel.getPanel());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		instance = this;
	}
}
