package com.mao.client;

import javax.swing.JOptionPane;

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
				
				MainClient.player = new Player();
				MainClient.player.username = MainClient.username;

				Network.initialize(new NetworkClient(1337));
				
				int index = MainClient.lobby.getJoinedUsers().indexOf(MainClient.username) + 1;
				Game.setOnSetupIndexChangedDefault(() -> {
					if (Game.getGame().getSetupIndex() == index) {
						Game.getGame().incrementSetupIndex();
						setup(g);
						Game.getGame().onEndedStateChanged(() -> {
							if (!Game.getGame().hasEnded()) {
								setup(g);
							}
						});
					}
				});
			} else if (MainClient.lobby.hasEnded()) {
				Network.deinitialize();
				JOptionPane.showMessageDialog(null, "The host ended the lobby.");
				g.setGameState(Processing.GAME_STATE_MAIN_MENU);
			}
		});
	}

	private void setup(Processing g) {
		g.setGameState(Processing.GAME_STATE_IN_GAME);

		Player player = MainClient.player.initialize(MainClient.username);
		for (int i = 0; i < 4; i++) {
			player.addCard(Game.getGame().getCardFromDeck());
		}

		player.update();
		Game.getGame().update();
	}
}
