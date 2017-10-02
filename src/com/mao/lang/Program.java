package com.mao.lang;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import com.mao.Debug;
import com.mao.NetworkedData;

public class Program {
	private ArrayList<Event> events = new ArrayList<>();
	private ArrayList<String> globals = new ArrayList<>();

	private Program() {
	}

	public void registerEvent(Event event) {
		events.add(event);
	}

	public Event getEvent(String name) {
		for (Event event : events) {
			if (event.getName().equals(name)) {
				return event;
			}
		}
		return null;
	}

	public List<Event> getRegisteredEvents() {
		return events;
	}

	public boolean handlesEvent(String name) {
		for (Event event : events) {
			if (event.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	public void writeToNetworkData(NetworkedData data) {
		data.write(events.size());
		for (int i = 0; i < events.size(); i++) {
			events.get(i).writeToNetworkData(data);
		}
		data.write(globals.size());
		for (int i = 0; i < globals.size(); i++) {
			data.write(globals.get(i));
		}
	}

	public static Program readFromNetworkData(NetworkedData data) {
		Program program = new Program();
		int eventsSize = data.read();
		for (int i = 0; i < eventsSize; i++) {
			program.events.add(Event.readFromNetworkData(data));
		}
		int globalsSize = data.read();
		for (int i = 0; i < globalsSize; i++) {
			program.globals.add(data.read());
		}
		return program;
	}

	public static Program compile(File file) throws IOException {
		Debug.log("Compiling Mao rule {0}...", file.getName());
		return compile(new String(Files.readAllBytes(file.toPath()), "UTF-8"));
	}

	public static Program compile(String program) {
		program = program.replaceAll("\\/\\*(.+?)\\*\\/", "").replace("\t", "").trim();

		Program prog = new Program();

		int currentIndex = -1;
		while ((currentIndex = program.indexOf("global ", currentIndex + 1)) != -1) {
			String global = program.substring(currentIndex, program.indexOf("\n", currentIndex + 1)).trim();

			if (global.endsWith("{")) {
				global = program.substring(currentIndex,
						ParseUtilities.findClosingBracket(program.toCharArray(), program.indexOf("{", currentIndex))
								+ 1)
						.trim();
			}

			String def = global.substring(7);
			prog.globals.add(def);
		}
		currentIndex = -1;

		while ((currentIndex = program.indexOf("Event::", currentIndex + 1)) != -1) {
			int openingIndex = program.indexOf("{", currentIndex + 1);
			int closingIndex = findClosingBracket(program.toCharArray(), openingIndex);

			String name = program.substring(currentIndex + 7, program.indexOf("{", currentIndex)).trim();
			String event = program.substring(program.indexOf("{", currentIndex + 1) + 1, closingIndex).trim();

			for (String global : prog.globals) {
				event = global + "\n" + event;
			}

			Event evt = new Event(name, event);
			prog.registerEvent(evt);
		}

		return prog;
	}

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
}
