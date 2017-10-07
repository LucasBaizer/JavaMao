package com.mao;

import java.util.ArrayList;

public class Lobby extends NetworkedObject {
	private String name;
	private String owner;
	private ArrayList<String> joined = new ArrayList<>();
	
	public Lobby(String name, String owner) {
		this.name = name;
		this.owner = owner;
		
		Network.getNetwork().registerObject(this);
	}
	
	public void join(String name) {
		joined.add(name);
	}
	
	public void leave(String name) {
		joined.remove(name);
	}

	@Override
	public int getNetworkID() {
		return 3;
	}

	@Override
	public NetworkedData writeNetworkedData() {
		NetworkedData data = new NetworkedData();
		data.write(name);
		data.write(owner);
		data.write(joined.size());
		for (int i = 0; i < joined.size(); i++) {
			data.write(joined.get(i));
		}
		return data;
	}

	@Override
	public void readNetworkedData(NetworkedData data) {
		joined.clear();

		name = data.read();
		owner = data.read();
		
		int joinedSize = data.read();
		for (int i = 0; i < joinedSize; i++) {
			joined.add(data.read());
		}
	}
}
