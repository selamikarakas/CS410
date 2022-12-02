

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Utils {
	public static State findState(ArrayList<State> states, String name) {
		for (State state : states) {
			if (state.name.equals(name)) {
				return state;
			}
		}
		return null;
	}

	public static Symbol findSymbol(ArrayList<Symbol> symbols, String value) {
		for (Symbol symbol : symbols) {
			if (symbol.value.equals(value)) {
				return symbol;
			}
		}
		return null;
	}

	public static ArrayList<String> readFile(String filePath) {
		Scanner scanner;
		try {
			scanner = new Scanner(new File(filePath));
			ArrayList<String> lines = new ArrayList<String>();
			while (scanner.hasNextLine()) {
				lines.add(scanner.nextLine());
			}
			scanner.close();
			return lines;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void printAutomaton(Automaton automaton) {
		System.out.println("ALPHABET");
		for (Symbol symbol : automaton.alphabet) {
			System.out.println(symbol.value);
		}
		System.out.println("STATES");
		for (State state : automaton.states) {
			System.out.println(state.name);
		}
		System.out.println("START");
		System.out.println(automaton.startState.name);
		System.out.println("FINAL");
		for (State state : automaton.finalStates) {
			System.out.println(state.name);
		}
		System.out.println("TRANSITIONS");
		for (State state : automaton.states) {
			for (Symbol symbol : automaton.alphabet) {
				for (State targetState : state.transitions.get(symbol)) {
					if (!targetState.name.isEmpty()) {
						System.out.println(state.name + " " + symbol.value + " " + targetState.name);
					}
				}
			}
		}
		System.out.println("END");
	}

	public static Automaton setAutomatonFromTxt(String filePath) {
		String[] headings = new String[] { "ALPHABET", "STATES", "START", "FINAL", "TRANSITIONS", "END" };
		Automaton automaton = new Automaton();
		ArrayList<String> lines = readFile(filePath);
		for (int i = 0; i < lines.size(); i++) {
			String currentLine = lines.get(i);
			if (currentLine.equals("ALPHABET")) {
				i++;
				currentLine = lines.get(i);
				while (!Arrays.stream(headings).anyMatch(currentLine::equals)) {
					automaton.addSymbolToAlphabet(currentLine);
					i++;
					currentLine = lines.get(i);
				}
			} else if (currentLine.equals("STATES")) {
				i++;
				currentLine = lines.get(i);
				while (!Arrays.stream(headings).anyMatch(currentLine::equals)) {
					automaton.addState(currentLine);
					i++;
					currentLine = lines.get(i);
				}
			} else if (currentLine.equals("START")) {
				i++;
				currentLine = lines.get(i);
				while (!Arrays.stream(headings).anyMatch(currentLine::equals)) {
					automaton.setStartState(currentLine);
					i++;
					currentLine = lines.get(i);
				}
			} else if (currentLine.equals("FINAL")) {
				i++;
				currentLine = lines.get(i);
				while (!Arrays.stream(headings).anyMatch(currentLine::equals)) {
					automaton.setFinalState(currentLine);
					i++;
					currentLine = lines.get(i);
				}
			} else if (currentLine.equals("TRANSITIONS")) {
				i++;
				currentLine = lines.get(i);
				while (!Arrays.stream(headings).anyMatch(currentLine::equals)) {
					automaton.addTransition(currentLine);
					i++;
					currentLine = lines.get(i);
				}
			} else if (currentLine.equals("END")) {
				i += 2;
			}
			i--;

		}
		return automaton;
	}

}
