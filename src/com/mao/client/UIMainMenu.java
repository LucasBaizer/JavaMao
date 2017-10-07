package com.mao.client;

public class UIMainMenu implements UIState {
	@Override
	public void createObjects(Processing g) {
		g.addUIObject(new UIMenuButton(0, 36, "Join a Lobby", () -> g.setGameState(Processing.GAME_STATE_LOBBY_MENU)));
		g.addUIObject(new UIMenuButton(1, 36, "Create a Lobby", () -> g.setGameState(Processing.GAME_STATE_CREATE_LOBBY)));
		g.addUIObject(new UIMenuButton(2, 96, "Exit", () -> System.exit(0)));
	}
}
