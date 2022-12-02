

import java.util.ArrayList;

public class Automaton {
	ArrayList<State> states;
	State startState;
	ArrayList<State> finalStates;
	ArrayList<Symbol> alphabet;

	public Automaton() {
		states = new ArrayList<>();
		finalStates = new ArrayList<>();
		alphabet = new ArrayList<>();
	}

	void addSymbolToAlphabet(String name) {
		Symbol symbol = new Symbol(name);
		alphabet.add(symbol);
	}

	void addState(String name) {
		State state = new State(name,alphabet);
		states.add(state);
	}

	void setStartState(String name) {
		startState = Utils.findState(states, name);
	}

	void setFinalState(String name) {
		finalStates.add(Utils.findState(states, name));
	}

	void addTransition(String transition) {
		String[] words = transition.split(" ");
		//System.out.println(words[0] + words[1] + words[2]);
		//Utils.findState(states, words[0]).transitions.put(Utils.findSymbol(alphabet, words[1]), new ArrayList<>());

		Utils.findState(states, words[0]).transitions.get(Utils.findSymbol(alphabet, words[1]))
				.add(Utils.findState(states, words[2]));

	}
}
