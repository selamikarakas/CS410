

import java.util.ArrayList;
import java.util.HashMap;

public class State {
	String name;
	HashMap<Symbol, ArrayList<State>> transitions = new HashMap<>();

	public State(String name, ArrayList<Symbol> alphabet) {
		this.name = name;
		initializeTransitions(alphabet);
	}

	public State(ArrayList<State> states, ArrayList<Symbol> alphabet) {
		String abc = "";
		for (State state2 : states) {
			String abd = state2.name;
			abc += abd;
		}
		this.name = abc;
		initializeTransitions(alphabet);
	}

	public void initializeTransitions(ArrayList<Symbol> alphabet) {
		for (Symbol symbol : alphabet) {
			transitions.put(symbol, new ArrayList<>());
		}
	}

}
