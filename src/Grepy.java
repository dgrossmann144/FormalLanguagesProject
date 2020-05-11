import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class Grepy {
   public static final String validChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789&@";

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
         System.out.println("At least one of the characters in the regex is invalid, valid characters are a-z, A-Z, 0-9, (), |, *, &, and @");
         return;
      } else {
//         System.out.println("regex is valid");
      }

//      System.out.println("DFA File Name: " + dfaFileName);
//      System.out.println("NFA File Name: " + nfaFileName);
//      System.out.println("Regex: " + regex);
//      System.out.println("Input File Name: " + inputFileName);

      String alphabet;
      try {
         alphabet = learnAlphabet(inputFileName);
      } catch(FileNotFoundException e) {
         System.out.println("Could not find file " + inputFileName);
         return;
      }
      if(alphabet.equals("")) {
         System.out.println("Input file is empty");
         return;
      }
      alphabet += "&";
//      System.out.println("Alphabet: " + alphabet);
//      System.out.println();

      NFA nfa = createNFA(regex, alphabet);
      if(nfa != null) {
//         System.out.println("nfa");
//         System.out.println(nfa);
      } else {
         System.out.println("Invalid character sequence while parsing regex into NFA");
         return;
      }

      ArrayList<Integer> startingNodes = new ArrayList<Integer>();
      startingNodes.add(0);
      nfa.getEpsilonTransitions(startingNodes);

      DFA dfa = createDFA(nfa);
//      System.out.println();
//      System.out.println("dfa");
//      System.out.println(dfa);
//      System.out.println();

      try {
         computeStrings(dfa, inputFileName);
      } catch(FileNotFoundException e) {
         System.out.println("Could not find file " + inputFileName);
         return;
      }

      if(!dfaFileName.equals("")) {
         dfa.generateDotFile(dfaFileName);
      }

      if(!nfaFileName.equals("")) {
         nfa.generateDotFile(nfaFileName);
      }
   }

   // Takes in the regex string and returns true if every character in the string
   // is in the valid character set
   public static boolean validRegexChars(String regex) {
      for(int i = 0; i < regex.length(); i++) {
         if(!(validChars + "()|*").contains(regex.charAt(i) + "")) {
            return false;
         }
      }
      return true;
   }

   // Takes in the name of the input file and returns a string with one of each
   // character in the file
   public static String learnAlphabet(String inputFileName) throws FileNotFoundException {
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

   public static void computeStrings(DFA dfa, String inputFileName) throws FileNotFoundException {
      Scanner scan = new Scanner(new File(inputFileName));
      while(scan.hasNext()) {
         String line = scan.nextLine();
         if(dfa.computeString(line)) {
            System.out.println(line);
         }
      }
      scan.close();
   }

   public static NFA createNFA(String regex, String alphabet) {
      NFA nfa = new NFA(alphabet);

      int index = 0;
      while(index < regex.length()) {
         char currentChar = regex.charAt(index);
         if(currentChar == '(') {
            int closingParenIndex = Utils.findMatchingParen(regex, index);
            if(closingParenIndex == -1) {
               return null;
            } else {
               NFA parenNFA = createNFA(regex.substring(index + 1, closingParenIndex), alphabet);
               index = closingParenIndex;
               if(index + 1 < regex.length() && regex.charAt(index + 1) == '*') {
                  parenNFA.applyKleeneStar();
                  index++;
               }
               nfa.appendConcatenate(parenNFA);
            }
         } else if(currentChar == '|') {
            if(index == regex.length() - 1) {
               return null;
            }
            NFA choiceNFA = createNFA(regex.substring(index + 1), alphabet);
            if(choiceNFA != null) {
               nfa.appendChoice(choiceNFA);
               index = regex.length();
            } else {
               return null;
            }
         } else if(validChars.contains(currentChar + "")) {
            NFA basicNFA = createBasicNFA(currentChar, alphabet);
            if(index + 1 < regex.length() && regex.charAt(index + 1) == '*') {
               basicNFA.applyKleeneStar();
               index++;
            }
            nfa.appendConcatenate(basicNFA);
         } else {
            return null;
         }
         index++;
      }

      return nfa;
   }

   public static NFA createBasicNFA(char character, String alphabet) {
      NFA nfa = new NFA(alphabet);
      int startIndex = nfa.addNode();
      int endIndex = nfa.addNode();
      switch(character) {
         case '&': {
            nfa.addTransition(nfa.getNode(startIndex), '&', nfa.getNode(endIndex));
            break;
         }
         case '@': {
            break;
         }
         default: {
            nfa.addTransition(nfa.getNode(startIndex), character, nfa.getNode(endIndex));
         }
      }
      return nfa;
   }

   public static DFA createDFA(NFA nfa) {
      DFA dfa = new DFA(nfa.getAlphabet().replaceAll("&", ""));
      Stack<ArrayList<Integer>> statesStack = new Stack<ArrayList<Integer>>();

      ArrayList<Integer> startList = new ArrayList<Integer>();
      startList.add(0);
      startList = nfa.getEpsilonTransitions(startList);
      statesStack.push(startList);
      dfa.addNode(startList, nfa.doesStatesListAccept(startList), true);

      while(!statesStack.empty()) {
         ArrayList<Integer> nfaStates = statesStack.pop();
         for(int j = 0; j < dfa.getAlphabet().length(); j++) {
            ArrayList<Integer> newStates = nfa.getTransitions(nfaStates, dfa.getAlphabet().charAt(j));
            if(!dfa.isNameInNodes(newStates)) {
               dfa.addNode(newStates, nfa.doesStatesListAccept(newStates), false);
               statesStack.push(newStates);
            }
            dfa.getNode(nfaStates).setTransition(dfa.getAlphabet().charAt(j), dfa.getNode(newStates));
         }
      }

      return dfa;
   }
}
