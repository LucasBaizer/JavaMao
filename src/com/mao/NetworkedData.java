package com.mao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NetworkedData {
	private ArrayList<Serializable> data = new ArrayList<>();
	
	public NetworkedData() {
	}

	public NetworkedData(Serializable... data) {
		this.data.addAll(Arrays.asList(data));
	}

	public void write(Serializable obj) {
		data.add(obj);
	}

	/**
	 * @return An object from the network data in a first-in-first-out manner
	 *         (like a queue).
	 */
	@SuppressWarnings("unchecked")
	public <T extends Serializable> T read() {
		return (T) data.remove(0);
	}

	public List<Serializable> getData() {
		return this.data;
	}
}
