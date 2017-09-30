package com.mao.lang;

import java.util.ArrayList;

public class Program {
	private ArrayList<Event> events = new ArrayList<>();

	public void registerEvent(Event event) {
		events.add(event);
	}
}
