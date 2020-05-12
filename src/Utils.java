import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Utils {
   // String of the only characters that should appear in the regex
   public static final String validChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789&@";

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

   // Given part of a regex string and an opening parenthesis, returns the matching
   // closing parenthesis if it exists
   public static int findMatchingParen(String regex, int startingParen) {
      // Tracks how many open parenthesis the loop has passed
      int parenCount = 1;

      for(int i = startingParen + 1; i < regex.length(); i++) {
         if(regex.charAt(i) == '(') {
            // Increment parenCount if the character is an open parenthesis
            parenCount++;
         } else if(regex.charAt(i) == ')') {
            // Decrement parenCount if the character is a close parenthesis
            parenCount--;
            // If the parenCount has hit zero then the current index is the matching closing paren
            if(parenCount == 0) {
               return i;
            }
         }
      }
      return -1;
   }

   // Removes duplicate integers from an ArrayList
   public static ArrayList<Integer> removeDuplicates(ArrayList<Integer> list) {
      ArrayList<Integer> result = new ArrayList<Integer>();
      for(int i = 0; i < list.size(); i++) {
         if(!result.contains(list.get(i))) {
            result.add(list.get(i));
         }
      }
      return result;
   }

   // Sorts an ArrayList in ascending order
   public static ArrayList<Integer> sort(ArrayList<Integer> list) {
      Object[] items = list.toArray();
      Arrays.sort(items);
      ArrayList<Integer> result = new ArrayList<Integer>();
      for(int i = 0; i < items.length; i++) {
         result.add((Integer) items[i]);
      }
      return result;
   }

   // Goes through the input file and prints the strings that are accepted by the
   // DFA
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

}
