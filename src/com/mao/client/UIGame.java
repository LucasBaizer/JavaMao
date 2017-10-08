package com.mao.client;

public class UIGame implements UIState {
	@Override
	public void createObjects(Processing g) {
		g.addUIObject(new UIDeck());
		g.addUIObject(new UIPlayedCards());
	}
}
