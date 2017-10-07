package com.mao.client;

import com.mao.Network;
import com.mao.NetworkClient;

public class UIGame implements UIState {
	@Override
	public void createObjects(Processing g) {
		Network.initialize(new NetworkClient());
		
		g.addUIObject(new UIDeck());
		g.addUIObject(new UIPlayedCards());
	}
}
