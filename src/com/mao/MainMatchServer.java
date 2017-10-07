package com.mao;

public class MainMatchServer {
	public static void main(String[] args) {
		Network.initialize(new NetworkServer(1338));

		try {
			Debug.log("Prepared for clients to connect.");
			Network.getNetworkServer().getServer().waitUntilClose();
		} catch (InterruptedException e) {
			Debug.error("Error while waiting for server to close!", e);
			System.exit(1);
		}
	}
}