package com.mao.lang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.mao.Card;
import com.mao.Face;
import com.mao.Game;
import com.mao.NetworkedData;
import com.mao.Player;
import com.mao.Suit;

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

		addVariable(new Variable("card").setConstant(true));
		for (Face face : Face.values()) {
			addVariable(new Variable(face.name().toUpperCase(), face).setConstant(true));
		}
		for (Suit suit : Suit.values()) {
			addVariable(new Variable(suit.name().toUpperCase(), suit).setConstant(true));
		}

		addVariable(new Variable("player").setConstant(true));
		addVariable(new Variable("actualPlayer").setConstant(true));
		addVariable(new Variable("nextPlayer").setConstant(true));
		addVariable(new Variable("playedCards").setConstant(true));
		addVariable(new Variable("Type::boolean", "Boolean").setConstant(true));
		addVariable(new Variable("Type::string", "String").setConstant(true));
		addVariable(new Variable("Type::integer", "Integer").setConstant(true));
		addVariable(new Variable("Type::card", "Card").setConstant(true));
		addVariable(new Variable("Type::suit", "Suit").setConstant(true));
		addVariable(new Variable("Type::face", "Face").setConstant(true));
		addVariable(new Variable("Type::stack", "Stack").setConstant(true));
		addVariable(new Variable("Type::list", "List").setConstant(true));
		addMethod(new Method("face", 1, (in) -> ((Card) in[0]).getFace()));
		addMethod(new Method("suit", 1, (in) -> ((Card) in[0]).getSuit()));
		addMethod(new Method("below", 1, (in) -> {
			return Game.getGame().getPlayedCards()
					.get((int) Math.max(0, Game.getGame().getPlayedCards().size() - ((double) in[0] + 1)));
		}));
		addMethod(new Method("println", 1, (in) -> {
			System.out.println(in[0]);
			return null;
		}));
		addMethod(new Method("List::pop", 1, (in) -> {
			List<?> list = (ArrayList<?>) in[0];
			return list.remove(list.size() - 1);
		}));
		addMethod(new Method("List::push", 2, (params) -> {
			Card card = (Card) params[0];
			Object par1 = params[1];
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
		addMethod(new Method("Player::cardCount", 1, (in) -> {
			return ((Player) in[0]).getHand().size();
		}));
		addMethod(new Method("Player::username", 1, (in) -> {
			return ((Player) in[0]).getUsername();
		}));
		addMethod(new Method("Math::sum", 2, (in) -> {
			double total = 0;
			for (Object obtainable : in) {
				total += (double) obtainable;
			}
			return total;
		}));
		addMethod(new Method("Math::diff", 2, (in) -> {
			double total = (double) in[0];
			for (int i = 1; i < in.length; i++) {
				total -= (double) in[i];
			}
			return total;
		}));
		addMethod(new Method("Math::mult", 2, (in) -> {
			double total = 1;
			for (Object obtainable : in) {
				total *= (double) obtainable;
			}
			return total;
		}));
		addMethod(new Method("Math::div", 2, (in) -> {
			double total = (double) in[0];
			for (int i = 1; i < in.length; i++) {
				total /= (double) in[i];
			}
			return total;
		}));
		addMethod(new Method("Math::random", 0, (in) -> Math.random()));
		addMethod(new Method("Math::pow", 2, (in) -> Math.pow((double) in[0], (double) in[1])));
		addMethod(new Method("Math::abs", 1, (in) -> Math.abs((double) in[0])));
		addMethod(new Method("Math::max", 2, (in) -> Math.max((double) in[0], (double) in[1])));
		addMethod(new Method("Math::min", 2, (in) -> Math.min((double) in[0], (double) in[1])));
		addMethod(new Method("Math::sin", 1, (in) -> Math.sin((double) in[0])));
		addMethod(new Method("Math::cos", 1, (in) -> Math.cos((double) in[0])));
		addMethod(new Method("Math::tan", 1, (in) -> Math.tan((double) in[0])));
		addMethod(new Method("Math::asin", 1, (in) -> Math.asin((double) in[0])));
		addMethod(new Method("Math::acos", 1, (in) -> Math.acos((double) in[0])));
		addMethod(new Method("Math::atan", 1, (in) -> Math.atan((double) in[0])));
		addMethod(new Method("Math::toDegrees", 1, (in) -> Math.toDegrees((double) in[0])));
		addMethod(new Method("Math::toRadians", 1, (in) -> Math.toRadians((double) in[0])));
		addMethod(new Method("Math::floor", 1, (in) -> Math.floor((double) in[0])));
		addMethod(new Method("Math::ceil", 1, (in) -> Math.ceil((double) in[0])));
		addMethod(new Method("Math::round", 1, (in) -> Math.round((double) in[0])));
		addMethod(new Method("Math::log", 1, (in) -> Math.log((double) in[0])));
		addMethod(new Method("Math::log10", 1, (in) -> Math.log10((double) in[0])));

		addMethod(new Method("String::concat", 2, (in) -> {
			String target = (String) in[0];
			String toConcat = (String) in[1];
			return target.concat(toConcat);
		}));
		addMethod(new Method("String::length", 1, (in) -> ((String) in[0]).length()));

		addMethod(new Method("List::size", 1, (in) -> {
			return (double) ((Collection<?>) in[0]).size();
		}));
		addMethod(new Method("typeof", 1, (in) -> {
			return in[0].getClass().getSimpleName();
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
