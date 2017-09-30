package com.mao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NetworkedData {
	private int size;
	private ArrayList<Serializable> data = new ArrayList<>();
	
	public NetworkedData() {
	}

	public NetworkedData(Serializable... data) {
		this.data.addAll(Arrays.asList(data));
		this.size = this.data.size();
	}

	public void write(Serializable obj) {
		data.add(obj);
		size = data.size();
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
		return data;
	}
	
	public int getObjectsCount() {
		return size;
	}
	
	@Override
	public String toString() {
		return data.toString();
	}
}
