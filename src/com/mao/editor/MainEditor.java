package com.mao.editor;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.mao.client.ProgramCallback;

public class MainEditor {
	private static ProgramCallback callback;
	private static Thread thread;

	public static void main(String[] args) {
		mainInternal(null);
	}

	public static void mainInternal(ProgramCallback callback) {
		MainEditor.callback = callback;

		thread = new Thread(() -> {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e) {
				e.printStackTrace();
			}

			JFrame frame = EditorFrame.getFrame();
			frame.setVisible(true);
			frame.pack();
		});
		thread.start();
	}

	public static boolean internallyLaunched() {
		return callback != null;
	}

	public static ProgramCallback getCallback() {
		return callback;
	}
}
