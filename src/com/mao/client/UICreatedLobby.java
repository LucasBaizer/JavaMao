package com.mao.client;

import com.mao.Debug;
import com.mao.Game;
import com.mao.Network;
import com.mao.NetworkClient;
import com.mao.Player;

public class UICreatedLobby implements UIState {
	@Override
	public void createObjects(Processing g) {
		g.addUIObject(new UIButton(48, "Start", () -> {
			MainClient.lobby.start();

			Network.deinitialize();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Debug.error("Error while disconnecting from lobby server!", e);
			}

			Player player = MainClient.player = new Player();
			player.username = MainClient.username;

			Network.initialize(new NetworkClient(80));

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Debug.error("Error while starting lobby!", e);
			}

			Game.getGame().incrementSetupIndex();
			setup(g, player);
			Game.getGame().onEndedStateChanged(() -> {
				if (!Game.getGame().hasEnded()) {
					setup(g, player);
				}
			});
		}));
	}

	private void setup(Processing g, Player player) {
		g.setGameState(Processing.GAME_STATE_IN_GAME);

		player.initialize(MainClient.username);
		for (int i = 0; i < 4; i++) {
			player.addCard(Game.getGame().getCardFromDeck());
		}

		Game.getGame().setCurrentPlayerUsername(player.getUsername());
		Debug.log("It is now " + player.getUsername() + "'s turn.");

		player.update();
		Game.getGame().update();
	}
}
