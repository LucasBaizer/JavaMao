package com.mao.lang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mao.Card;
import com.mao.Face;
import com.mao.Game;
import com.mao.NetworkedData;
import com.mao.Player;

public class Event extends CodeBlock {
	public static final String CARD_PLACED = "CardPlaced";
	public static final String CARD_PULLED = "CardPulled";
	public static final String CARD_GIVEN_AS_PENALTY = "CardGivenAsPenalty";
	public static final List<String> EVENT_TYPES = Arrays.asList(CARD_PLACED, CARD_PULLED, CARD_GIVEN_AS_PENALTY);

	private String name;

	@SuppressWarnings("unchecked")
	public Event(String name, String block) {
		super(block);

		if (!EVENT_TYPES.contains(name)) {
			throw new CompilerError("Unknown event type: " + name);
		}

		addVariable(new Variable("card"));
		for (Face face : Face.values()) {
			addVariable(new Variable(face.name().toUpperCase(), face));
		}
		addVariable(new Variable("player"));
		addVariable(new Variable("actualPlayer"));
		addVariable(new Variable("playedCards"));
		addVariable(new Variable("type::boolean", "Boolean"));
		addVariable(new Variable("type::string", "String"));
		addVariable(new Variable("type::integer", "Integer"));
		addVariable(new Variable("type::card", "Card"));
		addMethod(new Method("face", (in) -> ((Card) in[0].obtain()).getFace()));
		addMethod(new Method("suit", (in) -> ((Card) in[0].obtain()).getSuit()));
		addMethod(new Method("below", (in) -> {
			return Game.getGame().getPlayedCards()
					.get(Math.max(0, Game.getGame().getPlayedCards().size() - ((int) in[0].obtain() + 1)));
		}));
		addMethod(new Method("println", (in) -> {
			System.out.println(in[0].obtain());
			return null;
		}));
		addMethod(new Method("pop", (in) -> {
			List<?> list = (ArrayList<?>) in[0].obtain();
			return list.remove(list.size() - 1);
		}));
		addMethod(new Method("push", (params) -> {
			Card card = (Card) params[0].obtain();
			Object par1 = params[1].obtain();
			if (par1 instanceof Player) {
				((Player) par1).addCard(card);
			} else if (params[1] instanceof List<?>) {
				((List<Card>) par1).add(card);
			} else {
				throw new RuntimeException(
						"Method push(Card, Player|List) requires a Player or a List as its second parameter");
			}
			return null;
		}));
		addMethod(new Method("cardCount", (in) -> {
			return ((Player) in[0].obtain()).getHand().size();
		}));
		addMethod(new Method("sum", (in) -> {
			int total = 0;
			for (Obtainable obtainable : in) {
				total += (int) obtainable.obtain();
			}
			return total;
		}));
		addMethod(new Method("diff", (in) -> {
			int total = (int) in[0].obtain();
			for (int i = 1; i < in.length; i++) {
				total -= (int) in[i].obtain();
			}
			return total;
		}));
		addMethod(new Method("mult", (in) -> {
			int total = 1;
			for (Obtainable obtainable : in) {
				total *= (int) obtainable.obtain();
			}
			return total;
		}));
		addMethod(new Method("div", (in) -> {
			int total = (int) in[0].obtain();
			for (int i = 1; i < in.length; i++) {
				total /= (int) in[i].obtain();
			}
			return total;
		}));
		addMethod(new Method("concat", (in) -> {
			String target = (String) in[0].obtain();
			String toConcat = (String) in[1].obtain();
			return target.concat(toConcat);
		}));
		addMethod(new Method("typeof", (in) -> {
			return in[0].obtain().getClass().getSimpleName();
		}));

		parseChildren();

		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public ExecutionResult execute() {
		for (Code child : getChildren()) {
			ExecutionResult result = child.execute();
			if (result.isSuccessful()) {
				if (result.shouldExitScript()) {
					return result;
				}
			}
		}

		return ExecutionResultBuilder.builder(this).successful().build();
	}

	@Override
	public String toString() {
		return "Event::" + name;
	}

	public void writeToNetworkData(NetworkedData data) {
		data.write(name);
		data.write(getBlock());
	}

	public static Event readFromNetworkData(NetworkedData data) {
		return new Event(data.read(), data.read());
	}
}
