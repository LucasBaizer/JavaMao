package com.mao.editor;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public final class Syntax {
	private static final List<String> defaultPurpleKeywords = new ArrayList<>(Arrays.asList("if", "for", "break",
			"return", "exit", "say", "penalize", "var", "const", "global", "function", "true", "false"));
	private static final HashMap<String, SyntaxHighlighting> keywords = new HashMap<>();

	private static final Syntax syntax = new Syntax();

	static {
		SyntaxHighlighting purple = new SyntaxHighlighting(new Color(127, 0, 85), Font.BOLD);
		for (String str : defaultPurpleKeywords) {
			keywords.put(str, purple);
		}
	}

	public static Syntax getSyntax() {
		return syntax;
	}

	private Syntax() {
	}

	public Set<Entry<String, SyntaxHighlighting>> getSyntaxHighlighting() {
		return keywords.entrySet();
	}

	public Set<String> getKeywords() {
		return keywords.keySet();
	}

	public SyntaxHighlighting getSyntaxHighlighting(String keyword) {
		return keywords.get(keyword);
	}

	public void addSyntaxHighlighting(String keyword, SyntaxHighlighting highlight) {
		keywords.put(keyword, highlight);
	}

	public static class SyntaxHighlighting {
		private Color color;
		private int style;

		public SyntaxHighlighting(Color color, int style) {
			this.color = color;
			this.style = style;
		}

		public Color getColor() {
			return color;
		}

		public void setColor(Color color) {
			this.color = color;
		}

		public int getStyle() {
			return style;
		}

		public void setStyle(int style) {
			this.style = style;
		}
	}
}
