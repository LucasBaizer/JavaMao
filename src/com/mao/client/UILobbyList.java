package com.mao.client;

import java.awt.Color;
import java.util.List;

import com.mao.Debug;
import com.mao.Lobby;
import com.mao.Network;
import com.mao.NetworkClient;
import com.mao.NetworkedObject;

public class UILobbyList implements UIState {
	@Override
	public void createObjects(Processing g) {
		UIObject label = label("Retrieving lobby list...", null);
		g.addUIObject(label);

		Thread thread = new Thread(() -> {
			Network.initialize(new NetworkClient(1338));

			try {
				Thread.sleep(2500);
			} catch (InterruptedException e) {
				Debug.error("Error while retrieving lobby list!", e);
			}
			g.removeUIObject(label);

			UIObject last = null;
			List<NetworkedObject> objects = Network.getNetwork().getObjects(3);
			if (objects == null || objects.size() == 0) {
				g.addUIObject(last = label("No lobbies were found.", null));
			} else {
				for (NetworkedObject object : objects) {
					Lobby lobby = (Lobby) object;
					if (!lobby.hasStarted()) {
						last = button(lobby.getName() + " by " + lobby.getOwner() + " ("
								+ (lobby.getJoinedUsers().size() + 1) + "/6)", () -> {
									if (lobby.getJoinedUsers().size() + 1 < 6) {
										MainClient.lobby = lobby;
										g.setGameState(Processing.GAME_STATE_JOIN_LOBBY);
									} else {
										Processing.getProcessing().notify("Lobby is full.", null, Color.red);
									}
								}, last);
						g.addUIObject(last);
					}
				}
			}

			g.addUIObject(button("Cancel", () -> {
				Network.deinitialize();
				g.setGameState(Processing.GAME_STATE_MAIN_MENU);
			}, last));
		});
		thread.start();
	}

	private UILabel label(String msg, UIObject below) {
		return new UILabel(msg, 72) {
			@Override
			public void draw(Processing g) {
				x = (Screen.getSize().width / 2) - (width / 2);
				y = (below == null ? Screen.getSize().height / 16 : below.getY() + below.getHeight()) + (height / 3);

				super.draw(g);
			}
		};
	}

	private UIButton button(String msg, Runnable action, UIObject below) {
		return new UIButton(48, msg, action) {
			@Override
			public void draw(Processing g) {
				x = (Screen.getSize().width / 2) - (width / 2);
				y = (below == null ? Screen.getSize().height / 16 : below.getY() + below.getHeight()) + (height / 3);

				super.draw(g);
			}
		};
	}
}
