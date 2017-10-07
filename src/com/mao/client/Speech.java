package com.mao.client;

import java.awt.Color;

import com.mao.Game;

import voce.SpeechInterface;

public class Speech {
	public static final String GRAMMAR_PATH = "stt";
	public static final String GRAMMAR_NAME = "mao";
	
	private static final Object LOCK = new Object();
	private static boolean stopConsume;

	public static void initialize() {
		SpeechInterface.init("lib", false, true, GRAMMAR_PATH, GRAMMAR_NAME);

		Thread thread = new Thread(() -> {
			while (true) {
				try {
					Thread.sleep(200);

					if (stopConsume) {
						synchronized (LOCK) {
							LOCK.wait();
						}
						stopConsume = false;
					}

					while (SpeechInterface.getRecognizerQueueSize() > 0) {
						String s = SpeechInterface.popRecognizedString();
						if (!s.equals("mao")) {
							MainClient.player.addCard(Game.getGame().getCardFromDeck());
							Processing.getProcessing().notify("Talking.", "Talking.", Color.RED);
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

	public static void consume(boolean should) {
		if (should) {
			stopConsume = false;
			synchronized (LOCK) {
				LOCK.notifyAll();
			}
		} else {
			stopConsume = true;
		}
	}
}
