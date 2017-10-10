package com.mao.client;

import java.awt.Color;

import com.mao.Network;
import com.mao.NetworkClient;

public class UILobbyPrompt implements UIState {
	@Override
	public void createObjects(Processing g) {
		NetworkClient.initialize(new NetworkClient(443));

		UITextField lobby = new UITextField("Lobby password: ", 48, 10) {
			@Override
			public void draw(Processing g) {
				x = (Screen.getSize().width / 2);
				y = (Screen.getSize().height / 2) - (height / 2) - height;

				super.draw(g);
			}
		};
		UIButton button = new UIButton(48, "Join", () -> {
			if (lobby.getText().equals(MainClient.lobby.getPassword())) {
				MainClient.lobby.join(MainClient.username);
				g.setGameState(Processing.GAME_STATE_JOINED_LOBBY);
			} else {
				Processing.getProcessing().notify("Incorrect password.", null, Color.RED);
			}
		}) {
			@Override
			public void draw(Processing g) {
				x = lobby.getX() + lobby.getWidth() - width;
				y = lobby.getY() + lobby.getHeight() + (height / 3);

				super.draw(g);
			}
		};
		UIButton cancel = new UIButton(48, "Cancel", () -> {
			Network.deinitialize();
			MainClient.lobby = null;
			g.setGameState(Processing.GAME_STATE_LOBBY_MENU);
		}) {
			@Override
			public void draw(Processing g) {
				x = lobby.x + lobby.width - lobby.getEntireWidth(g);
				y = lobby.getY() + lobby.getHeight() + (height / 3);

				super.draw(g);
			}
		};
		g.addUIObject(lobby);
		g.addUIObject(button);
		g.addUIObject(cancel);
	}
}
