package com.mao.lang;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.mao.Debug;

public class Parser {
	public static void main(String[] args) {
		try {
			parse(new String(Files.readAllBytes(Paths.get("rules/all_hail_the_queen_of_mao.mao")), "UTF-8"));
		} catch (IOException e) {
			Debug.error("Error while parsing!", e);
		}
	}

	public static Program parse(String program) {
		program = program.replaceAll("\\/\\/(.+?)\n", "").replace("\t", "").trim();

		Program prog = new Program();

		int currentIndex = -1;
		while ((currentIndex = program.indexOf("Event::", currentIndex + 1)) != -1) {
			int opening = 1;
			int closing = 0;

			int openingIndex = program.indexOf("{", currentIndex + 1);
			int closingIndex = 0;

			while (opening > closing) {
				openingIndex = program.indexOf("{", openingIndex + 1);
				if (openingIndex != -1) {
					opening++;
				}
				closingIndex = program.indexOf("}", closingIndex + 1);
				if (closingIndex != -1) {
					closing++;
				}
			}

			String event = program.substring(program.indexOf("{", currentIndex + 1) + 1, closingIndex).trim();
			
			Event evt = new Event(program.substring(currentIndex + 7, program.indexOf("{", currentIndex)).trim(), event);
			prog.registerEvent(evt);
		}

		return prog;
	}
}