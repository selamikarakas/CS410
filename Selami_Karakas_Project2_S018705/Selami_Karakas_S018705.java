package cs410HW2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Selami_Karakas_S018705 {
	static char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	static String[] headings = new String[] { "NON-TERMINAL", "TERMINAL", "RULES", "START" };

	static ArrayList<Character> input_N = new ArrayList<Character>();
	static ArrayList<Character> input_T = new ArrayList<Character>();
	static ArrayList<String> input_P = new ArrayList<String>();
	static Character input_S = null;

	public static void main(String[] args) {
		String filePath = args[0];
		ArrayList<String> lines = readFile(filePath);
		for (int i = 0; i < lines.size(); i++) {
			String currentLine = lines.get(i);
			i++;
			if (currentLine.equals("NON-TERMINAL")) {
				currentLine = lines.get(i);
				while (!Arrays.stream(headings).anyMatch(currentLine::equals)) {
					input_N.add(currentLine.toCharArray()[0]);
					i++;
					currentLine = lines.get(i);
				}
			} else if (currentLine.equals("TERMINAL")) {
				currentLine = lines.get(i);
				while (!Arrays.stream(headings).anyMatch(currentLine::equals)) {
					input_T.add(currentLine.toCharArray()[0]);
					i++;
					currentLine = lines.get(i);
				}
			} else if (currentLine.equals("RULES")) {
				currentLine = lines.get(i);
				while (!Arrays.stream(headings).anyMatch(currentLine::equals)) {
					input_P.add(currentLine);
					i++;
					currentLine = lines.get(i);
				}
			} else if (currentLine.equals("START")) {
				currentLine = lines.get(i);
				input_S = currentLine.toCharArray()[0];
			}
			i--;
		}

		if (anyRHSStart()) {
			char next = getAFreeChar();
			input_N.add(next);
			input_P.add(next + ":" + input_S);
			input_S = next;
		}

		while (epsilonIndex() != -1) {
			String epsilonP = input_P.get(epsilonIndex());
			char epsilonT = epsilonP.charAt(0);
			input_P.remove(epsilonIndex());
			for (int i = 0; i < input_P.size(); i++) {
				String currentP = input_P.get(i);
				for (int j = 2; j < currentP.length(); j++) {
					char currentT = currentP.charAt(j);
					if (currentT == epsilonT) {
						if (currentP.length() == 3) {
							String newP = currentP.charAt(0) + ":e";
							input_P.add(newP);
						} else {
							String newP = currentP.charAt(0) + ":" + currentP.substring(2, j)
									+ currentP.substring(j + 1, currentP.length());
							input_P.add(newP);
						}
					}
				}
			}
		}

		while (unitPIndex() != -1) {
			String unitP = input_P.get(unitPIndex());
			char unitT = unitP.charAt(0);
			char targetT = unitP.charAt(2);
			input_P.remove(unitPIndex());
			if (unitT != targetT) {
				for (int i = 0; i < input_P.size(); i++) {
					String currentP = input_P.get(i);
					if (currentP.charAt(0) == targetT && !currentP.equals(targetT + ":" + targetT)) {
						String newP = unitT + ":" + currentP.substring(2);
						input_P.add(newP);
					}
				}
			}
		}

		ArrayList<Character> PTerminal = new ArrayList<Character>();

		while (RHSTWithNIndex() != -1) {
			int RHSTerminalIndex = RHSTWithNIndex();
			String RHSP = input_P.get(RHSTerminalIndex);
			input_P.remove(RHSTerminalIndex);

			for (int i = 0; i < RHSP.length(); i++) {
				char RHST = RHSP.charAt(i);
				if (!Character.isUpperCase(RHST) && RHST != ':') {
					for (int j = 0; j < input_T.size(); j++) {
						if (RHST == input_T.get(j)) {
							try {
								PTerminal.get(j);
							} catch (Exception e) {
								char newNT = getAFreeChar();
								String newP = newNT + ":" + RHST;
								PTerminal.add(j, newNT);
								input_P.add(newP);
								input_N.add(newNT);
							}
							String newP = RHSP.substring(0, i) + PTerminal.get(j) + RHSP.substring(i + 1);
							input_P.add(newP);
						}
					}
					i = RHSP.length();
				}
			}
		}

		while (MultiRHSNIndex() != -1) {
			int MultiRHSNIndex = MultiRHSNIndex();
			String P = input_P.get(MultiRHSNIndex);
			input_P.remove(MultiRHSNIndex);

			char newNT = getAFreeChar();
			input_N.add(newNT);
			String newP1 = newNT + ":" + P.substring(2, 4);
			String newP2 = P.substring(0, 2) + newNT + P.substring(4);
			input_P.add(newP2);
			input_P.add(newP1);
		}

		System.out.println("NON-TERMINAL");
		for (int i = 0; i < input_N.size(); i++) {
			System.out.println(input_N.get(i));
		}

		System.out.println("TERMINAL");
		for (int i = 0; i < input_T.size(); i++) {
			System.out.println(input_T.get(i));
		}

		System.out.println("RULES");
		for (int i = 0; i < input_P.size(); i++) {
			System.out.println(input_P.get(i));
		}

		System.out.println("START");
		System.out.println(input_S);
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
			e.printStackTrace();
		}
		return null;
	}

	private static boolean anyRHSStart() {
		boolean ret = false;
		for (int i = 0; i < input_P.size(); i++) {
			if (input_P.get(i).substring(1).contains(input_S + "")) {
				ret = true;
				i = input_P.size();
			}
		}
		return ret;
	}

	public static char getAFreeChar() {
		char ret = 0;
		for (int i = 0; i < alphabet.length; i++) {
			boolean isFree = true;
			for (int j = 0; j < input_N.size(); j++) {
				if (alphabet[i] == input_N.get(j)) {
					isFree = false;
					j = input_N.size();
				}
			}
			if (isFree) {
				ret = alphabet[i];
				i = alphabet.length;
			}
		}
		return ret;
	}

	public static int epsilonIndex() {
		int ret = -1;
		for (int i = 0; i < input_P.size(); i++) {
			if (input_P.get(i).contains("e") && input_P.get(i).charAt(0) != input_S) {
				ret = i;
				i = input_P.size();
			}
		}
		return ret;

	}

	public static int unitPIndex() {
		int ret = -1;
		for (int i = 0; i < input_P.size(); i++) {
			if (input_P.get(i).length() == 3 && Character.isUpperCase(input_P.get(i).charAt(2))) {
				ret = i;
				i = input_P.size();
			}
		}
		return ret;
	}

	public static int RHSTWithNIndex() {
		int ret = -1;
		for (int i = 0; i < input_P.size(); i++) {
			String tmp = input_P.get(i);
			if (tmp.length() > 3) {
				for (int j = 2; j < tmp.length(); j++) {
					if (!Character.isUpperCase(tmp.charAt(j)) && tmp.charAt(j) != ':') {
						ret = i;
						i = input_P.size();
						j = tmp.length();
					}
				}
			}
		}
		return ret;
	}

	public static int MultiRHSNIndex() {
		int ret = -1;
		for (int i = 0; i < input_P.size(); i++) {
			String tmp = input_P.get(i);
			if (tmp.length() > 4) {
				ret = i;
				i = input_P.size();
			}
		}
		return ret;
	}

}
