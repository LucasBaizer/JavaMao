package com.mao.client;

public class UICreateLobby implements UIState {
	@Override
	public void createObjects(Processing g) {
		g.addUIObject(new UITextField(100, 100, 48, 10));
	}
}
