package com.mao.lang;

public class ParseUtilities {
	// thanks to @Bill the Lizard on StackOverflow.
	public static int findClosingBracket(char[] text, int openPos) {
		int closePos = openPos;
		int counter = 1;
		while (counter > 0) {
			char c = text[++closePos];
			if (c == '{') {
				counter++;
			} else if (c == '}') {
				counter--;
			}
		}
		return closePos;
	}

	public static int findClosingParenthesis(char[] text, int openPos) {
		int closePos = openPos;
		int counter = 1;
		while (counter > 0) {
			char c = text[++closePos];
			if (c == '(') {
				counter++;
			} else if (c == ')') {
				counter--;
			}
		}
		return closePos;
	}
}
