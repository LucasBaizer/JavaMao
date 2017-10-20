package com.mao.client;

import com.mao.Lobby;
import com.mao.Network;
import com.mao.NetworkClient;

public class UICreateLobby implements UIState {
	@Override
	public void createObjects(Processing g) {
		NetworkClient.initialize(new NetworkClient(1338));
		
		UITextField lobby = new UITextField("Lobby name: ", 48, 10) {
			@Override
			public void draw(Processing g) {
				x = (Screen.getSize().width / 2);
				y = (Screen.getSize().height / 2) - (height / 2) - height;

				super.draw(g);
			}
		};
		UITextField password = new UITextField("Password: ", 48, 10) {
			@Override
			public void draw(Processing g) {
				x = (Screen.getSize().width / 2);
				y = (Screen.getSize().height / 2) - (height / 3);

				super.draw(g);
			}
		};
		UIButton button = new UIButton(48, "Create", () -> {
			g.setGameState(Processing.GAME_STATE_CREATED_LOBBY);
			MainClient.lobby = new Lobby(lobby.getText().trim(), MainClient.username, password.getText());
		}) {
			@Override
			public void draw(Processing g) {
				x = password.getX() + password.getWidth() - width;
				y = password.getY() + password.getHeight() + (height / 3);

				super.draw(g);
			}
		};
		UIButton cancel = new UIButton(48, "Cancel", () -> {
			Network.deinitialize();
			g.setGameState(Processing.GAME_STATE_MAIN_MENU);
		}) {
			@Override
			public void draw(Processing g) {
				x = password.x + password.width - password.getEntireWidth(g);
				y = password.getY() + password.getHeight() + (height / 3);

				super.draw(g);
			}
		};
		g.addUIObject(lobby);
		g.addUIObject(password);
		g.addUIObject(button);
		g.addUIObject(cancel);
	}
}
