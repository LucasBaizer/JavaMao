package com.mao.client;

import java.awt.Color;

import com.mao.Network;
import com.mao.NetworkedData;
import com.mao.NetworkedObject;

public class NetworkedNotification extends NetworkedObject {
	private String message;
	private Color color;
	
	public NetworkedNotification() {
	}

	public NetworkedNotification(String msg, Color color) {
		this.message = msg;
		this.color = color;
	}

	@Override
	public int getNetworkID() {
		return 4;
	}

	@Override
	public NetworkedData writeNetworkedData() {
		return new NetworkedData(message, color.getRGB());
	}

	@Override
	public void readNetworkedData(NetworkedData data) {
		message = data.read();
		color = new Color(data.read());

		if (Network.isClient()) {
			Processing.getProcessing().addUIObject(new UINotification(message, color));
		}
	}
}
