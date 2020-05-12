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
            System.out.println("Invalid argument, correct usage is: java Grepy [-d \"DFA-FILE\"] [-n \"NFA-FILE\"] \"REGEX\" \"FILE\"");
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
      if(!Utils.validRegexChars(regex)) {
         System.out.println("At least one of the characters in the regex is invalid, valid characters are a-z, A-Z, 0-9, (), |, *, &, and @");
         return;
      }

      // Learn the alphabet from the input file
      String alphabet;
      try {
         alphabet = Utils.learnAlphabet(inputFileName);
         if(alphabet.equals("")) {
            System.out.println("Input file is empty");
            return;
         }
         // Empty string is always included in an NFA alphabet
         alphabet += "&";
      } catch(FileNotFoundException e) {
         System.out.println("Could not find file " + inputFileName);
         return;
      }

      // Create the NFA
      NFA nfa = createNFA(regex, alphabet);
      if(nfa == null) {
         System.out.println("Invalid character sequence while parsing regex into NFA");
         return;
      }

      // Create the DFA
      DFA dfa = createDFA(nfa);

      // Output the strings from the input file if they accept on the DFA
      try {
         Utils.computeStrings(dfa, inputFileName);
      } catch(FileNotFoundException e) {
         System.out.println("Could not find file " + inputFileName);
         return;
      }

      // If the NFA file name exists create the dot file for it
      if(!nfaFileName.equals("")) {
         nfa.generateDotFile(nfaFileName);
      }

      // If the DFA file name exists create the dot file for it
      if(!dfaFileName.equals("")) {
         dfa.generateDotFile(dfaFileName);
      }
   }

   // Creates NFA based on the given regex and alphabet
   public static NFA createNFA(String regex, String alphabet) {
      NFA nfa = new NFA(alphabet);

      // Tracks the current character in the regex that is being processed
      int index = 0;

      while(index < regex.length()) {
         // Get the current character and based on what it is add different things to the
         // NFA
         char currentChar = regex.charAt(index);
         if(currentChar == '(') {
            int closingParenIndex = Utils.findMatchingParen(regex, index);
            if(closingParenIndex == -1) {
               return null;
            } else {
               // Recursively call createNFA on the part of the regex in parenthesis
               NFA parenNFA = createNFA(regex.substring(index + 1, closingParenIndex), alphabet);
               // Move the index to the closing parenthesis
               index = closingParenIndex;
               // Check to see if a star follows the parenthesis
               if(index + 1 < regex.length() && regex.charAt(index + 1) == '*') {
                  parenNFA.applyKleeneStar();
                  index++;
               }
               // Append the part of the regex in parenthesis to the end of the existing NFA
               nfa.appendConcatenate(parenNFA);
            }
         } else if(currentChar == '|') {
            // If | is the last character the regex is invalid
            if(index == regex.length() - 1) {
               return null;
            }
            // Recursively call createNFA on the right half of the choice symbol and then
            // append it to the existing NFA
            NFA choiceNFA = createNFA(regex.substring(index + 1), alphabet);
            if(choiceNFA != null) {
               nfa.appendChoice(choiceNFA);
               index = regex.length();
            } else {
               return null;
            }
         } else if(validChars.contains(currentChar + "")) {
            // If the character is a part of the alphabet create a simple NFA for it
            NFA basicNFA = createBasicNFA(currentChar, alphabet);
            // Check if it is followed by a star,
            if(index + 1 < regex.length() && regex.charAt(index + 1) == '*') {
               basicNFA.applyKleeneStar();
               index++;
            }
            // Append the basic NFA to the end of the existing NFA
            nfa.appendConcatenate(basicNFA);
         } else {
            // Character is invalid
            return null;
         }
         // Move to the next character
         index++;
      }

      return nfa;
   }

   // Creates a simple NFA with one transition
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

   // Creates a DFA from the given NFA
   public static DFA createDFA(NFA nfa) {
      // DFA alphabet does not include empty string
      DFA dfa = new DFA(nfa.getAlphabet().replaceAll("&", ""));
      // Stack keeps track of the sets of states that still need to be processed
      Stack<ArrayList<Integer>> statesStack = new Stack<ArrayList<Integer>>();

      // Add the first state to the stack
      ArrayList<Integer> startList = new ArrayList<Integer>();
      startList.add(0);
      // Includes any epsilon transitions from the starting node
      startList = nfa.getEpsilonTransitions(startList);
      statesStack.push(startList);
      dfa.addNode(startList, nfa.doesStatesListAccept(startList), true);

      // Continue while there are more sets of states that need to be processed
      while(!statesStack.empty()) {
         ArrayList<Integer> nfaStates = statesStack.pop();
         // For each character in the DFAs alphabet
         for(int j = 0; j < dfa.getAlphabet().length(); j++) {
            // Get the
            ArrayList<Integer> newStates = nfa.getTransitions(nfaStates, dfa.getAlphabet().charAt(j));
            // If the set of states is already in the DFA then it doesn't need to be added
            // again
            if(!dfa.isLabelInNodes(newStates)) {
               dfa.addNode(newStates, nfa.doesStatesListAccept(newStates), false);
               statesStack.push(newStates);
            }
            // Add a transition from the current state to the new state
            dfa.getNode(nfaStates).setTransition(dfa.getAlphabet().charAt(j), dfa.getNode(newStates));
         }
      }

      return dfa;
   }
}
