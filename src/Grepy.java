import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;

public class Grepy {
	public static void main(String[] args) {
		String dfaFileName = "";
		String nfaFileName = "";
		
		// Parse command line arguments based on number of arguments
		if(args.length == 4) {
			if(args[0].equals("-d")) {
				dfaFileName = args[1];
			} else if(args[0].equals("-n")) {
				nfaFileName = args[1];
			} else {
				System.out.println("Invalid argument, correct usage is: java Grepy [-d DFA-FILE] [-n NFA-FILE] \"REGEX\" FILE");
				return;
			}
		} else if(args.length == 6) {
			if(args[0].equals("-d") && args[2].equals("-n")) {
				dfaFileName = args[1];
				nfaFileName = args[3];
			} else if(args[0].equals("-n") && args[2].equals("-d")) {
				dfaFileName = args[3];
				nfaFileName = args[1];
			} else {
				System.out.println("Invalid argument, correct usage is: java Grepy [-d DFA-FILE] [-n NFA-FILE] \"REGEX\" FILE");
				return;
			}
		} else if(args.length != 2) {
			System.out.println("Invalid number of arguments, correct usage is: java Grepy [-d DFA-FILE] [-n NFA-FILE] \"REGEX\" FILE");
			return;
		}
		String regex = args[args.length - 2];
		String inputFileName = args[args.length - 1];
		
		// Check if input file is a text file
		if(!inputFileName.endsWith(".txt")) {
			System.out.println("Input file must be a text file");
			return;
		}
		
		// Check if the regex has valid characters
		if(!validRegexChars(regex)) {
			System.out.println("At least one of the characters in the regex is invalid, valid characters are a-z, A-Z, 0-9, (), *, &, and @");
			return;
		} else {
			System.out.println("regex is valid");
		}
		
		System.out.println("DFA File Name: " + dfaFileName);
		System.out.println("NFA File Name: " + nfaFileName);
		System.out.println("Regex: " + regex);
		System.out.println("Input File Name: " + inputFileName);
		
		String alphabet;
		try {
			alphabet = learnAlphabet(inputFileName);
		} catch (FileNotFoundException e) {
			System.out.println("Could not find file " + inputFileName);
			return;
		}
		if(alphabet.equals("")) {
			System.out.println("Input file is empty");
			return;
		}
		System.out.println("Alphabet: " + alphabet);
		createNFA(regex, alphabet);
	}
	
	// Takes in the regex string and returns true if every character in the string is in the valid character set
	public static boolean validRegexChars(String regex) {
		String validChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789()*&@";
		for(int i = 0; i < regex.length(); i++) {
			if(!validChars.contains(regex.charAt(i) + "")) {
				return false;
			}
		}
		return true;
	}
	
	// Takes in the name of the input file and returns a string with one of each character in the file
	public static String learnAlphabet(String inputFileName) throws FileNotFoundException{
		String alphabet = "";
		Scanner scan = new Scanner(new File(inputFileName));
		while(scan.hasNext()) {
			String line = scan.nextLine();
			for(int i = 0; i < line.length(); i++) {
				if(!alphabet.contains(line.charAt(i) + "")) {
					alphabet = alphabet + line.charAt(i);
				}
			}
		}
		scan.close();
		return alphabet;
	}
	
	public static void createNFA(String regex, String alphabet) {
		NFA nfa = new NFA(alphabet);
		nfa.addNode(false);
		nfa.addNode(true);
		
		Stack<Character> stack = new Stack<Character>();
		for(int i = 0; i < regex.length(); i++) {
			if(stack.peek() == '+' && alphabet.contains(regex.charAt(i) + "")) {
				// 
			}
		}
		
	}
}
