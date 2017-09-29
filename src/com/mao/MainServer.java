package com.mao;

public class MainServer {
	public static void main(String[] args) {
		Network.initialize(new NetworkServer());
		
		
		
		try {
			Network.getNetworkServer().getServer().waitUntilClose();
		} catch (InterruptedException e) {
			Debug.error("Error while waiting for server to close!", e);
			System.exit(1);
		}
	}
}
