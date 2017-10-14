package com.mao;

import java.util.ArrayList;
import java.util.List;

public class Lobby extends NetworkedObject {
	private String name;
	private String owner;
	private String password;
	private boolean started;
	private Runnable onUpdate;
	private ArrayList<String> joined = new ArrayList<>();

	public Lobby() {
	}

	public Lobby(String name, String owner, String password) {
		this.name = name;
		this.owner = owner;
		this.password = password;

		Network.getNetwork().registerObject(this);
		Network.getNetwork().makeUpdate(this);
	}

	public Lobby join(String name) {
		joined.add(name);

		Network.getNetwork().makeUpdate(this);

		return this;
	}

	public void leave(String name) {
		joined.remove(name);

		Network.getNetwork().makeUpdate(this);
	}

	public String getName() {
		return this.name;
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
		data.write(password);
		data.write(started);
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
		password = data.read();
		started = data.read();

		int joinedSize = data.read();
		for (int i = 0; i < joinedSize; i++) {
			joined.add(data.read());
		}

		if (onUpdate != null) {
			onUpdate.run();
		}
	}

	public void onUpdate(Runnable runnable) {
		this.onUpdate = runnable;
	}

	public String getOwner() {
		return owner;
	}

	public List<String> getJoinedUsers() {
		return joined;
	}

	public String getPassword() {
		return password;
	}

	public void start() {
		started = true;
		Network.getNetwork().makeUpdate(this);
	}

	public void end() {
		started = false;
		Network.getNetwork().makeUpdate(this);
	}

	public boolean hasStarted() {
		return started;
	}
}
