

import java.util.ArrayList;

public class Selami_Karakas_S018705 {
	public static void main(String[] args) {
		Automaton NFA = Utils.setAutomatonFromTxt(args[0]);
		Automaton DFA = converter(NFA);
		// Utils.printAutomaton(NFA);
		// System.out.println();
		Utils.printAutomaton(DFA);
	}

	public static Automaton converter(Automaton NFA) {
		Automaton DFA = new Automaton();
		DFA.alphabet = NFA.alphabet;
		State start = new State(NFA.startState.name, DFA.alphabet);
		DFA.states.add(start);
		DFA.startState = start;

		Boolean check = true;
		int j = 0;
		while (check) {
			ArrayList<State> newStates = new ArrayList<State>();
			ArrayList<State> states = DFA.states;
			for (int i = j; i < states.size(); i++) {
				State currentState = states.get(i);
				for (Symbol currentSymbol : DFA.alphabet) {
					ArrayList<State> targetStates = new ArrayList<State>();
					// currentState.transitions.put(currentSymbol, new ArrayList<State>());
					for (char b : currentState.name.toCharArray()) {
						for (State state : Utils.findState(NFA.states, "" + b).transitions.get(currentSymbol)) {
							if (!targetStates.contains(state)) {
								targetStates.add(state);
							}
						}
					}
					State newState = new State(targetStates, DFA.alphabet);
					if (Utils.findState(DFA.states, newState.name) == null && !targetStates.isEmpty()) {
						newStates.add(newState);
					}
					if (Utils.findState(currentState.transitions.get(currentSymbol), newState.name) == null) {
						currentState.transitions.get(currentSymbol).add(newState);
					}

				}
			}
			j = DFA.states.size();
			if (newStates.isEmpty()) {
				check = false;
			} else {
				for (State state : newStates) {
					DFA.states.add(state);

					for (State NFAFinal : NFA.finalStates) {
						if (state.name.contains(NFAFinal.name)) {
							DFA.finalStates.add(state);
						}
					}

				}
			}
		}
		return DFA;
	}

}
