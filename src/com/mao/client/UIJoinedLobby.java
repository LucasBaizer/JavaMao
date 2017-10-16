package com.mao.client;

import com.mao.Game;
import com.mao.Network;
import com.mao.NetworkClient;
import com.mao.Player;

public class UIJoinedLobby implements UIState {
	@Override
	public void createObjects(Processing g) {
		MainClient.lobby.onUpdate(() -> {
			if (MainClient.lobby.hasStarted()) {
				Network.deinitialize();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				Network.initialize(new NetworkClient(80));

				int index = MainClient.lobby.getJoinedUsers().indexOf(MainClient.username) + 1;
				Game.setOnSetupIndexChangedDefault(() -> {
					if (Game.getGame().getSetupIndex() == index) {
						setup(g);
						Game.getGame().onEndedStateChanged(() -> {
							if (!Game.getGame().hasEnded()) {
								setup(g);
							}
						});
					}
				});
			}
		});
	}

	private void setup(Processing g) {
		g.setGameState(Processing.GAME_STATE_IN_GAME);

		Player player = MainClient.player = new Player().initialize(MainClient.username);
		for (int i = 0; i < 4; i++) {
			player.addCard(Game.getGame().getCardFromDeck());
		}

		player.update();
		Game.getGame().update();
	}
}
