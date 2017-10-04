package com.mao.ui;

import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import com.mao.Debug;
import com.mao.Game;
import com.mao.Network;
import com.mao.NetworkClient;
import com.mao.Player;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

public class Processing extends PApplet {
	private static final long serialVersionUID = 4080175460722677681L;

	private HashMap<Long, UIObject> objects = new HashMap<>();
	private HashMap<String, PImage> images = new HashMap<>();
	private ArrayList<UIObject> waitingObjects = new ArrayList<>();
	private Object lock = new Object();
	private boolean shouldLock = true;

	public Processing() {
		instance = this;
	}

	public void addUIObject(UIObject object) {
		synchronized (objects) {
			objects.put(object.getID(), object);
		}

		if (shouldLock) {
			waitingObjects.add(object);
		} else {
			object.initialize(this);
		}
	}

	public void removeUIObject(UIObject object) {
		synchronized (objects) {
			objects.remove(object.getID());
		}
	}

	public UIObject removeUIObject(long id) {
		synchronized (objects) {
			return objects.remove(id);
		}
	}

	public UIObject getObject(long id) {
		return objects.get(id);
	}

	public PImage getImage(String name) {
		return images.get(name);
	}

	@Override
	public void setup() {
		frame.setTitle("The Game of Mao");
		frame.setResizable(true);

		File folder = new File("src/data/assets/images/");
		for (File image : folder.listFiles()) {
			if (image.getName().endsWith("png")) {
				images.put(image.getName().split(Pattern.quote("."))[0], loadImage("assets/images/" + image.getName()));
			}
		}
		Debug.log("Loaded {0} images.", images.size());
		synchronized (lock) {
			lock.notifyAll();
		}

		for (UIObject object : waitingObjects) {
			object.initialize(this);
		}
		Debug.log("Initialized waiting objects.");
		waitingObjects.clear();
		shouldLock = false;
	}

	@Override
	public void draw() {
		background(255);
		textFont(new PFont(PFont.findFont("Courier"), true));

		Screen.setSize(new Dimension(width, height));

		if (shouldLock) {
			synchronized (lock) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		synchronized (objects) {
			for (UIObject object : objects.values()) {
				object.draw(this);
			}
		}
	}

	@Override
	public void mousePressed() {
		synchronized (objects) {
			for (UIObject object : objects.values()) {
				object.mousePressed(this);
			}
		}
	}

	@Override
	public void mouseReleased() {
		synchronized (objects) {
			for (UIObject object : objects.values()) {
				object.mouseReleased(this);
			}
		}
	}

	private static Processing instance;

	public static Processing getProcessing() {
		return instance;
	}

	public static void main(String[] args) {
		Network.initialize(new NetworkClient());

		PApplet.main("com.mao.ui.Processing");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Player player = new Player();
		player.initialize("Lucas");

		Debug.log("Hello, my name is " + player.getUsername() + "!");

		for (int i = 0; i < 4; i++) {
			player.addCard(Game.getGame().getCardFromDeck());
		}

		player.update();
		if (Game.getGame().getCurrentPlayerUsername() == null) {
			Game.getGame().setCurrentPlayerUsername(player.getUsername());
			Debug.log("It is now " + player.getUsername() + "'s turn.");
		}
		Game.getGame().update();
	}
}
