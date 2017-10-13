package com.mao.editor;

import java.awt.Color;
import java.awt.Font;
import java.util.Map.Entry;

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

public class TextEditorDocumentListener implements DocumentListener {
	private static TextEditorDocumentListener listener;
	
	public static TextEditorDocumentListener getListener() {
		return listener;
	}
	
	private StyleContext sc;
	private JTextPane pane;
	private AttributeSet emptyAttributeSet;
	private StyledDocument doc;
	private boolean ignore;

	public TextEditorDocumentListener(JTextPane pane) {
		this.pane = pane;
		this.doc = pane.getStyledDocument();
		this.emptyAttributeSet = pane.getCharacterAttributes().copyAttributes();
		this.sc = StyleContext.getDefaultStyleContext();
		
		listener = this;
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		highlightSyntax();
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		highlightSyntax();
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
	}

	public boolean highlightSyntax() {
		if (ignore) {
			return false;
		}

		Syntax syntax = Syntax.getSyntax();
		String text = pane.getText();

		SwingUtilities.invokeLater(() -> {
			doc.setCharacterAttributes(0, doc.getLength(), emptyAttributeSet, true);
		});

		for (Entry<String, SyntaxHighlighting> keyword : syntax.getSyntaxHighlighting()) {
			int currentIndex = -1;
			while ((currentIndex = text.indexOf(keyword.getKey(), currentIndex + 1)) != -1) {
				int start = currentIndex;
				int end = currentIndex + keyword.getKey().length();

				if (start - 1 >= 0) {
					if (Character.isAlphabetic(text.charAt(start - 1))) {
						continue;
					}
				}
				if (end < text.length()) {
					if (Character.isAlphabetic(text.charAt(end))) {
						continue;
					}
				}

				SwingUtilities.invokeLater(() -> {
					ignore = true;

					AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground,
							keyword.getValue().getColor());
					aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Courier");
					aset = sc.addAttribute(aset, StyleConstants.Bold,
							(keyword.getValue().getStyle() & Font.BOLD) == Font.BOLD);
					aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

					String txt = pane.getText().substring(0, start);
					int count = txt.length() - txt.replace("\n", "").length();

					int pos = pane.getCaretPosition();
					pane.select(start - count, end - count);
					pane.setCharacterAttributes(aset, true);
					pane.select(0, 0);
					pane.setCaretPosition(pos);
					pane.setCharacterAttributes(emptyAttributeSet, true);

					ignore = false;
				});
			}
		}

		SwingUtilities.invokeLater(() -> {
			int quote = -1;
			while ((quote = text.indexOf('"', quote + 1)) != -1) {
				AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground,
						new Color(0, 150, 0));
				aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Courier");
				aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

				String txt = pane.getText().substring(0, quote);
				int count = txt.length() - txt.replace("\n", "").length();

				int end = text.indexOf('"', quote + 1);

				if (end == -1) {
					end = text.length();
				}

				int pos = pane.getCaretPosition();
				pane.select(quote - count, end - count + 1);
				pane.setCharacterAttributes(aset, true);
				pane.select(0, 0);
				pane.setCaretPosition(pos);
				pane.setCharacterAttributes(emptyAttributeSet, true);

				quote = end;
			}
		});

		return true;
	}
}
