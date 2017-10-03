package com.mao.ui;

import java.awt.Dimension;
import java.util.HashMap;

import com.mao.Debug;
import com.mao.Game;
import com.mao.Network;
import com.mao.NetworkClient;
import com.mao.Player;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

public class Processing extends PApplet {
	private static final long serialVersionUID = -546165304521904394L;

	private HashMap<Long, UIObject> objects = new HashMap<>();
	private HashMap<String, PImage> images = new HashMap<>();

	public Processing() {
		instance = this;
	}

	public void addUIObject(UIObject object) {
		objects.put(object.getID(), object);

		object.initialize(this);
	}

	public void removeUIObject(UIObject object) {
		objects.remove(object.getID());
	}

	public PImage getImage(String name) {
		return images.get(name);
	}

	@Override
	public void setup() {
		frame.setTitle("The Game of Mao");
		frame.setResizable(true);

		images.put("spades", loadImage("assets/images/spades.png"));
		images.put("hearts", loadImage("assets/images/hearts.png"));
		images.put("diamonds", loadImage("assets/images/diamonds.png"));
		images.put("clubs", loadImage("assets/images/clubs.png"));
	}

	@Override
	public void draw() {
		background(255);
		textFont(new PFont(PFont.findFont("Courier"), true));

		Screen.setSize(new Dimension(width, height));

		for (UIObject object : objects.values()) {
			object.draw(this);
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
