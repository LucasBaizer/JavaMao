package com.mao.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.StyledDocument;

public class EditorPanel extends JPanel {
	private static final long serialVersionUID = -7323320404155574419L;

	private static EditorPanel instance = new EditorPanel();

	public static EditorPanel getPanel() {
		return instance;
	}

	private GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.NORTHWEST,
			GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0);
	private JTextPane pane = new JTextPane();

	public EditorPanel() {
		super(new GridBagLayout());

		pane.setFont(new Font("Courier", Font.PLAIN, 14));
		pane.setBorder(BorderFactory.createLineBorder(Color.black));

		JScrollPane scroll = new JScrollPane(pane);
		scroll.setPreferredSize(new Dimension(500, 500));

		StyledDocument doc = pane.getStyledDocument();
		doc.addDocumentListener(new TextEditorDocumentListener(pane));

		add(scroll, c);
	}

	public JTextPane getTextPane() {
		return pane;
	}
}
