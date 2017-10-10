package com.mao;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.jnetwork.DataPackage;
import org.jnetwork.TCPConnection;
import org.jnetwork.TCPServer;

import com.mao.lang.Program;

public class NetworkServer extends Network {
	private ArrayList<TCPConnection> clients = new ArrayList<>();
	private TCPServer server;
	private int port;

	public NetworkServer(int port) {
		this.port = port;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onInitialize() {
		Debug.log("Starting server on port " + port + "...");
		server = new TCPServer(port, (data) -> {
			try {
				TCPConnection client = (TCPConnection) data.getConnection();

				Debug.log(client.getRemoteSocketAddress() + " connected.");
				clients.add(client);

				DataPackage initial = (DataPackage) client.readUnshared();
				String clientLobbyName = (String) initial.getObjects()[0];

				if (clientLobbyName != null) {
					Game game = Game.getGame(clientLobbyName);
					if (game == null) {
						game = Game.initialize(clientLobbyName);

						for (int i = 0; i < 52; i++) {
							game.addCardToDeck(Card.of(Face.values()[i % 13], Suit.values()[i / 13]));
							game.shuffleDeck();
						}
						game.playCard(game.getCardFromDeck());

						RuleHandler handler = RuleHandler.initialize(clientLobbyName);
						try {
							handler.addRule(Program.compile(new File("rules/turn.mao")));
							handler.addRule(Program.compile(new File("rules/placement.mao")));
							handler.addRule(Program.compile(new File("rules/one_card_remaining.mao")));
							handler.addRule(Program.compile(new File("rules/mao_on_win.mao")));
							handler.addRule(Program.compile(new File("rules/all_hail_the_queen_of_mao.mao")));
							handler.addRule(Program.compile(new File("rules/have_a_nice_day.mao")));
						} catch (Throwable e) {
							Debug.error("Error while compiling default rules!", e);
							System.exit(1);
						}
					}
				}

				for (List<NetworkedObject> list : getRegisteredObjects()) {
					for (NetworkedObject obj : list) {
						if (obj instanceof Game) {
							if (obj == Game.getGame(clientLobbyName)) {
								registerObjectOnClient(obj, client);
								makeUpdate(obj, client);
							}
						} else if (obj instanceof RuleHandler) {
							if (obj == RuleHandler.getRuleHandler(clientLobbyName)) {
								registerObjectOnClient(obj, client);
								makeUpdate(obj, client);
							}
						} else {
							registerObjectOnClient(obj, client);
							makeUpdate(obj, client);
						}
					}
				}

				while (!client.isClosed()) {
					DataPackage pkg = (DataPackage) client.readUnshared();
					if (pkg.getMessage().equals("GAME_DATA")) {
						int netID = (int) pkg.getObjects()[0];
						long uniqueID = (long) pkg.getObjects()[1];
						int netObjects = (int) pkg.getObjects()[2];

						NetworkedData netdata = new NetworkedData();
						for (int i = 3; i < 3 + netObjects; i++) {
							netdata.write(pkg.getObjects()[i]);
						}

						for (NetworkedObject obj : getObjects(netID)) {
							if (obj.getUniqueID() == uniqueID) {
								obj.readNetworkedData(netdata);

								if (obj instanceof Player) {
									Debug.log("Updated player with username " + ((Player) obj).getUsername() + ".");
								} else {
									Debug.log("Updated object of type " + obj.getClass().getSimpleName() + ".");
								}
							}
						}

						for (TCPConnection connected : clients) {
							if (!client.getRemoteSocketAddress().toString()
									.equals(connected.getRemoteSocketAddress().toString())) {
								connected.writeUnshared(pkg);
							}
						}
					} else if (pkg.getMessage().equals("REGISTER_OBJECT")) {
						NetworkedObject object = (NetworkedObject) ((Class<? extends NetworkedObject>) pkg
								.getObjects()[0]).newInstance();
						object.setUniqueID((long) pkg.getObjects()[1]);
						registerObject(object);

						for (TCPConnection connected : clients) {
							if (!client.getRemoteSocketAddress().toString()
									.equals(connected.getRemoteSocketAddress().toString())) {
								connected.writeUnshared(pkg);
							}
						}

						Debug.log("Registered object of type " + object.getClass().getSimpleName() + ".");
					}
				}
			} catch (SocketException | EOFException e) {
				return;
			} catch (Exception e) {
				Debug.error("Error in client thread!", e);
				return;
			}
		});
		server.addClientDisconnectionListener((data) -> {
			Debug.log(data.getConnection().getRemoteSocketAddress() + " disconnected.");
			clients.remove(data.getConnection());
		});
		try {
			server.start();
			Debug.log("Server started!");
		} catch (Exception e) {
			Debug.error("Error while launching server!", e);
			System.exit(1);
		}
	}

	public TCPServer getServer() {
		return this.server;
	}

	public void registerObjectOnClient(NetworkedObject obj, TCPConnection client) {
		try {
			client.writeUnshared(new DataPackage(obj.getClass(), obj.getUniqueID()).setMessage("REGISTER_OBJECT"));
		} catch (IOException e) {
			Debug.error("Error while writing client data to server!", e);
			System.exit(1);
		}
	}

	@Override
	public void makeUpdate(NetworkedObject object) {
		for (TCPConnection client : clients) {
			try {
				client.writeUnshared(getUpdatePackage(object));
			} catch (IOException e) {
				Debug.error("Error while writing server data to client!", e);
				System.exit(1);
			}
		}
	}

	private void makeUpdate(NetworkedObject object, TCPConnection client) {
		try {
			client.writeUnshared(getUpdatePackage(object));
		} catch (IOException e) {
			Debug.error("Error while writing server data to client!", e);
			System.exit(1);
		}
	}

	private DataPackage getUpdatePackage(NetworkedObject object) {
		NetworkedData net = object.writeNetworkedData();
		Serializable[] data = new Serializable[net.getObjectsCount() + 3];
		data[0] = object.getNetworkID();
		data[1] = object.getUniqueID();
		data[2] = net.getObjectsCount();
		for (int i = 3; i < 3 + net.getObjectsCount(); i++) {
			data[i] = net.read();
		}
		return new DataPackage(data).setMessage("GAME_DATA");
	}

	@Override
	public void destroy() {
		try {
			server.close();
		} catch (IOException e) {
			Debug.error("Error while closing server!", e);
		}
	}
}
