package com.mao.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import com.mao.editor.Syntax.SyntaxHighlighting;

public class EditorPanel extends JPanel {
	private static final long serialVersionUID = -7323320404155574419L;

	private static EditorPanel instance = new EditorPanel();

	public static EditorPanel getPanel() {
		return instance;
	}

	private GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.NORTHWEST,
			GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0);

	public EditorPanel() {
		super(new GridBagLayout());

		JTextPane pane = new JTextPane();
		pane.setFont(new Font("Courier", Font.PLAIN, 14));
		pane.setBorder(BorderFactory.createLineBorder(Color.black));
		pane.setPreferredSize(new Dimension(500, 500));

		AttributeSet old = pane.getCharacterAttributes().copyAttributes();

		JScrollPane scroll = new JScrollPane(pane);

		StyleContext sc = StyleContext.getDefaultStyleContext();
		StyledDocument doc = pane.getStyledDocument();
		doc.addDocumentListener(new DocumentListener() {
			private boolean ignore;

			@Override
			public void removeUpdate(DocumentEvent e) {
				if (ignore) {
					return;
				}
				SwingUtilities.invokeLater(() -> {
					// pane.setCharacterAttributes(old, true);
				});
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				if (ignore) {
					return;
				}

				Syntax syntax = Syntax.getSyntax();
				String text = pane.getText();

				for (Entry<String, SyntaxHighlighting> keyword : syntax.getSyntaxHighlighting()) {
					Pattern regex = Pattern.compile(Pattern.quote(keyword.getKey()));
					Matcher matcher = regex.matcher(text);
					while (matcher.find()) {
						matcher.group();

						int start = matcher.start();
						int end = matcher.end();

						SwingUtilities.invokeLater(() -> {
							ignore = true;

							AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground,
									keyword.getValue().getColor());
							aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Courier");
							aset = sc.addAttribute(aset, StyleConstants.Bold,
									(keyword.getValue().getStyle() & Font.BOLD) == Font.BOLD);
							aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);
							doc.setCharacterAttributes(start, end, aset, true);

							int oldCaret = pane.getCaretPosition();
							pane.select(start, end);
							pane.replaceSelection(keyword.getKey());
							pane.select(0, 0);
							pane.setCaretPosition(oldCaret);

							pane.setCharacterAttributes(old, true);

							ignore = false;
						});
					}
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
			}
		});

		add(scroll, c);
	}
}
