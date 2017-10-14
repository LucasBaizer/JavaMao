package com.mao.editor;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import com.mao.lang.Program;

public class EditorFrame extends JFrame {
	private static final long serialVersionUID = -6108266143910191681L;

	private static EditorFrame instance = new EditorFrame();

	public static EditorFrame getFrame() {
		return instance;
	}

	private Program program = Program.compile("Event::CardPlaced {}", false);

	public EditorFrame() {
		super("MSL Rule Editor");

		getContentPane().add(EditorPanel.getPanel());
		setJMenuBar(MenuBar.getMenuBar());

		addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}

			@Override
			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowActivated(WindowEvent e) {
			}
		});

		instance = this;
	}

	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}
}
