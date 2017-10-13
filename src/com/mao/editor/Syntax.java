package com.mao.editor;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.mao.lang.Event;
import com.mao.lang.Method;
import com.mao.lang.Program;
import com.mao.lang.Variable;

import java.util.Set;

public final class Syntax {
	private static final List<String> defaultPurpleKeywords = new ArrayList<>(Arrays.asList("if", "for", "break",
			"return", "exit", "say", "penalize", "var", "const", "global", "function", "true", "false"));
	private HashMap<String, SyntaxHighlighting> keywords = new HashMap<>();

	private static final Syntax syntax = new Syntax();

	public static Syntax getSyntax() {
		return syntax;
	}

	private Syntax() {
		setSyntax(EditorFrame.getFrame().getProgram());
	}

	public void setSyntax(Program program) {
		SyntaxHighlighting blue = new SyntaxHighlighting(Color.BLUE, Font.PLAIN);
		SyntaxHighlighting darkBlue = new SyntaxHighlighting(Color.BLUE, Font.BOLD);
		SyntaxHighlighting purple = new SyntaxHighlighting(new Color(127, 0, 85), Font.BOLD);
		SyntaxHighlighting orange = new SyntaxHighlighting(new Color(255, 120, 0), Font.BOLD);

		for (String event : Event.EVENT_TYPES) {
			keywords.put("Event::" + event, orange);
		}
		for (String str : defaultPurpleKeywords) {
			keywords.put(str, purple);
		}
		keywords.put("#register", new SyntaxHighlighting(new Color(0, 150, 0), Font.BOLD));
		for (Event event : program.getRegisteredEvents()) {
			for (Variable var : event.getVariables()) {
				// if (var.isConstant()) {
				String name = var.getName();
				// if (name.toUpperCase().equals(name)) {
				keywords.put(name, darkBlue);
				// } else {
				// keywords.put(name, blue);
				// }
				// }
			}
			for (Method method : event.getMethods()) {
				keywords.put(method.getName(), blue);
			}
		}
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
