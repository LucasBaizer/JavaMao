package com.mao;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mao.client.Speech;
import com.mao.lang.Code;
import com.mao.lang.Event;
import com.mao.lang.PenalizeCommand;
import com.mao.lang.Program;
import com.mao.lang.SayCommand;

import voce.SpeechInterface;

public class RuleHandler extends NetworkedObject {
	private static RuleHandler inst;

	public static RuleHandler getRuleHandler() {
		return inst;
	}

	public static void setRuleHandler(RuleHandler object) {
		inst = object;
	}

	public static RuleHandler initialize() {
		RuleHandler game = new RuleHandler();
		inst = game;
		Network.getNetwork().registerObject(game);
		return game;
	}

	private ArrayList<Program> rules = new ArrayList<>();

	public void addRule(Program rule) {
		rules.add(rule);
	}

	public List<Code> fire(String name, Player placer, Player shouldPlaced, Player next, Card card) {
		List<Code> responses = new ArrayList<>();
		for (Program rule : rules) {
			if (rule.handlesEvent(name)) {
				Event event = rule.getEvent(name);
				setupEvent(event, placer, shouldPlaced, next, card);

				Code result = event.execute().getSource();
				if (result instanceof SayCommand || result instanceof PenalizeCommand) {
					responses.add(result);
				}
			}
		}
		return responses;
	}

	private void setupEvent(Event event, Player placer, Player shouldPlaced, Player next, Card card) {
		event.getVariable("card").setValue(card);
		event.getVariable("player").setValue(placer);
		event.getVariable("actualPlayer").setValue(shouldPlaced);
		event.getVariable("nextPlayer").setValue(next);
		event.getVariable("playedCards").setValue(Game.getGame().getPlayedCards());
	}

	@Override
	public int getNetworkID() {
		return 2;
	}

	@Override
	public NetworkedData writeNetworkedData() {
		NetworkedData data = new NetworkedData();
		data.write(rules.size());
		for (int i = 0; i < rules.size(); i++) {
			rules.get(i).writeToNetworkData(data);
		}
		return data;
	}

	@Override
	public void readNetworkedData(NetworkedData data) {
		rules.clear();
		
		SpeechInterface.destroy();

		try {
			File grammar = new File(Speech.GRAMMAR_PATH + File.separator + Speech.GRAMMAR_NAME + ".gram");
			grammar.delete();
			grammar.createNewFile();

			FileWriter out = new FileWriter(grammar, true);
			out.write("#JSGF V1.0;" + System.lineSeparator());
			out.write("grammar mao;" + System.lineSeparator());
			out.write("public <mao_gramar> = ");

			int rulesSize = data.read();
			for (int i = 0; i < rulesSize; i++) {
				Program program = Program.readFromNetworkData(data);
				for (String saying : program.getRegisteredSayings()) {
					out.write(saying.toLowerCase().replace(".", "").replace(",", "").replace("?", "").replace("!", "")
							.trim()
							+ (i == rulesSize - 1 && program.getRegisteredSayings()
									.indexOf(saying) == program.getRegisteredSayings().size() - 1 ? ";" : " | "));
				}
				addRule(program);
			}

			out.flush();
			out.close();
		} catch (IOException e) {
			Debug.error("Error while updating rules!", e);
		}

		Debug.log("Rules updated. There are now " + rules.size() + " rules in effect.");
		
		Speech.initialize();
	}
}
