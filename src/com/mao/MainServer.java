package com.mao;

public class MainServer {
	public static void main(String[] args) {
		Network.initialize(new NetworkServer(80));
	}
}