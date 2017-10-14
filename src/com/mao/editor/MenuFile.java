package com.mao.editor;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

import com.mao.lang.Program;

public class MenuFile extends JMenu {
	private static final long serialVersionUID = 2631640272139849342L;

	private File lastSaveLocation;

	public MenuFile() {
		super("File");

		JMenuItem compile = new JMenuItem(new AbstractAction("Compile") {
			private static final long serialVersionUID = 8475723568476220423L;

			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					EditorFrame.getFrame()
							.setProgram(Program.compile(EditorPanel.getPanel().getTextPane().getText(), true));
					JOptionPane.showMessageDialog(null, "Compiled successfully!");

					Syntax.getSyntax().setSyntax(EditorFrame.getFrame().getProgram());
					TextEditorDocumentListener.getListener().highlightSyntax();
				} catch (RuntimeException e) {
					e.printStackTrace(System.out);
					JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + ": " + e.getMessage());
				}
			}
		});
		compile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK));

		JMenuItem saveAs = new JMenuItem(new AbstractAction("Save As") {
			private static final long serialVersionUID = 8475723568476220423L;

			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					EditorFrame.getFrame()
							.setProgram(Program.compile(EditorPanel.getPanel().getTextPane().getText(), true));
				} catch (RuntimeException e) {
					e.printStackTrace(System.out);
					JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + ": " + e.getMessage()
							+ "\n\nPlease fix this before saving.");
					return;
				}

				JFileChooser chooser = new JFileChooser(
						lastSaveLocation == null ? System.getProperty("user.dir") : lastSaveLocation.getParent());
				chooser.setFileFilter(new FileFilter() {
					@Override
					public String getDescription() {
						return "MSL rule rules (*.mao)";
					}

					@Override
					public boolean accept(File file) {
						return file.isDirectory() || file.getAbsolutePath().endsWith(".mao");
					}
				});

				if (chooser.showSaveDialog(EditorFrame.getFrame()) == JFileChooser.APPROVE_OPTION) {
					try {
						File file = chooser.getSelectedFile();

						String name = file.getName();
						if (!name.endsWith(".mao")) {
							file = new File(file.toString() + ".mao");
						}

						if (!file.exists()) {
							file.createNewFile();
						} else {
							file.delete();
						}

						Files.write(file.toPath(), EditorPanel.getPanel().getTextPane().getText().getBytes("UTF-8"));

						lastSaveLocation = file;
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null, "Failed to save: " + e.getMessage());
					}
				}
			}
		});
		saveAs.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));

		JMenuItem save = new JMenuItem(new AbstractAction("Save") {
			private static final long serialVersionUID = 3821099206378386091L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (lastSaveLocation == null) {
					saveAs.getAction().actionPerformed(null);
				} else {
					try {
						EditorFrame.getFrame()
								.setProgram(Program.compile(EditorPanel.getPanel().getTextPane().getText(), true));
					} catch (RuntimeException e) {
						e.printStackTrace(System.out);
						JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + ": " + e.getMessage()
								+ "\n\nPlease fix this before saving.");
						return;
					}

					try {
						Files.write(lastSaveLocation.toPath(),
								EditorPanel.getPanel().getTextPane().getText().getBytes("UTF-8"));
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null, "Failed to save: " + e.getMessage());
					}
				}
			}
		});
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));

		add(compile);
		add(save);
		add(saveAs);

		if (MainEditor.internallyLaunched()) {
			JMenuItem export = new JMenuItem(new AbstractAction("Export To Game") {
				private static final long serialVersionUID = -7554911227679957133L;

				@Override
				public void actionPerformed(ActionEvent evt) {
					try {
						EditorFrame.getFrame()
								.setProgram(Program.compile(EditorPanel.getPanel().getTextPane().getText(), true));
						JOptionPane.showMessageDialog(null, "Compiled successfully!");

						MainEditor.getCallback().programCompleted(EditorFrame.getFrame().getProgram());

						EditorFrame.getFrame().dispose();
					} catch (RuntimeException e) {
						JOptionPane.showMessageDialog(null, e.getMessage());
					}
				}
			});
			export.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));

			add(export);
		}
	}
}
