public class Grepy {
	private static String dfaFileName = "";
	private static String nfaFileName = "";
	private static String regex = "";
	private static String inputFileName = "";
	public static void main(String[] args) {
		if(args.length == 4) {
			if(args[0].equals("-d")) {
				dfaFileName = args[1];
			} else if(args[0].equals("-n")) {
				nfaFileName = args[1];
			} else {
				System.out.println("Invalid argument, correct usage is \"java Grepy [-d DFA-FILE] [-n NFA-FILE] REGEX FILE\"");
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
				System.out.println("Invalid argument, correct usage is \"java Grepy [-d DFA-FILE] [-n NFA-FILE] REGEX FILE\"");
				return;
			}
		} else if(args.length != 2) {
			System.out.println("Invalid number of arguments, correct usage is \"java Grepy [-d DFA-FILE] [-n NFA-FILE] REGEX FILE\"");
			return;
		}
		regex = args[args.length - 2];
		inputFileName = args[args.length - 1];
		
		System.out.println("DFA File Name: " + dfaFileName);
		System.out.println("NFA File Name: " + nfaFileName);
		System.out.println("Regex: " + regex);
		System.out.println("Input File Name: " + inputFileName);
	}
}
