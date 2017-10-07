package com.mao.client;

import java.awt.Color;

import com.mao.Game;
import com.mao.Network;
import com.mao.NetworkedData;
import com.mao.NetworkedObject;

public class NetworkedNotification extends NetworkedObject {
	private String message;
	private Color color;

	// constructor gets called on remote registration
	public NetworkedNotification() {
		if (Network.isClient()) {
			Processing.getProcessing().addUIObject(new UINotification(message, color));
		}
	}

	public NetworkedNotification(String msg, Color color) {
		this.message = msg;
		this.color = color;
	}

	@Override
	public int getNetworkID() {
		return Game.getRandomInstance().nextInt();
	}

	@Override
	public NetworkedData writeNetworkedData() {
		return new NetworkedData(message, color);
	}

	@Override
	public void readNetworkedData(NetworkedData data) {
		message = data.read();
		color = data.read();
	}
}
