package com.mao.editor;

import javax.swing.JFrame;

import com.mao.lang.Program;

public class EditorFrame extends JFrame {
	private static final long serialVersionUID = -6108266143910191681L;

	private static EditorFrame instance = new EditorFrame();

	public static EditorFrame getFrame() {
		return instance;
	}
	
	private Program program = Program.compile("", false);

	public EditorFrame() {
		super("MSL Rule Editor");

		getContentPane().add(EditorPanel.getPanel());
		setJMenuBar(MenuBar.getMenuBar());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		instance = this;
	}

	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}
}
