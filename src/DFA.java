import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

public class DFA {
   // Hashtable of nodes in the DFA, the key is a list of indices from the NFA
   // created during the subset construction of this DFA
   // which also serves as the label for the node, the value is the DFANode object
   private Hashtable<ArrayList<Integer>, DFANode> nodes = new Hashtable<ArrayList<Integer>, DFANode>();
   // The alphabet for the DFA
   private String alphabet;
   // The key for the start state of the DFA
   private ArrayList<Integer> startState;

   public DFA(String alphabet) {
      this.alphabet = alphabet;
   }

   // Adds the new node to the DFA
   public DFANode addNode(ArrayList<Integer> label, boolean accepting, boolean startState) {
      nodes.put(label, new DFANode(alphabet, accepting, label));
      if(startState) {
         this.startState = label;
      }
      return nodes.get(label);
   }

   // Returns true if the given label is in the DFA
   public boolean isLabelInNodes(ArrayList<Integer> label) {
      return nodes.containsKey(label);
   }

   // Computes the given string on the DFA
   public boolean computeString(String str) {
      ArrayList<Integer> currentState = startState;

      // For each character in the string traverse one step along the DFA
      for(int i = 0; i < str.length(); i++) {
         if(alphabet.contains(str.charAt(i) + "")) {
            currentState = nodes.get(currentState).getTransition(str.charAt(i)).getLabel();
         } else {
            // If the string contains a character not found in the DFA's alphabet, it does
            // not accept
            return false;
         }
      }

      return nodes.get(currentState).isAccepting();
   }

   // Create the DOT language format file in the given file name
   public void generateDotFile(String dfaFileName) {
      // Build the string before writing it to the file
      String result = "digraph {\n";
      // The list of labels
      Object[] keys = nodes.keySet().toArray();

      // Add the list of nodes
      result += "\t\" \" [shape=none];\n";
      for(int i = 0; i < nodes.size(); i++) {
         DFANode node = nodes.get(keys[i]);
         // Sets the shape of the node appropriately if it accepts or not
         if(node.isAccepting()) {
            result += "\t\"" + node.getStringLabel() + "\" [shape=doublecircle];\n";
         } else {
            result += "\t\"" + node.getStringLabel() + "\" [shape=circle];\n";
         }
      }

      // Add arrow to point to starting node
      result += "\t\" \" -> \"" + nodes.get(startState).getStringLabel() + "\";\n";

      // Add arrows for every node for every transition
      for(int i = 0; i < nodes.size(); i++) {
         DFANode node = nodes.get(keys[i]);
         for(int j = 0; j < alphabet.length(); j++) {
            result += "\t\"" + node.getStringLabel() + "\" -> \"" + node.getTransition(alphabet.charAt(j)).getStringLabel() + "\" [label=\"" + alphabet.charAt(j) + "\"];\n";
         }
      }
      result += "}";

      // Write to the file
      try {
         // Create the file
         File dfaFile = new File(dfaFileName + ".gv");
         if(dfaFile.createNewFile()) {
            // If creating the file was successful, write the formatted string to it
            FileWriter dfaFileWriter = new FileWriter(dfaFileName + ".gv");
            dfaFileWriter.write(result);
            dfaFileWriter.close();
            System.out.println("Successfully created DOT format file for dfa");
         } else {
            System.out.println("Could not create dfa file " + dfaFileName + " because that file already exists");
            return;
         }
      } catch(IOException error) {
         System.out.println("An error occurred while creating dfa file");
      }
   }

   // Returns the alphabet of the DFA   
   public String getAlphabet() {
      return alphabet;
   }

   // Returns the node with the given label
   public DFANode getNode(ArrayList<Integer> label) {
      return nodes.get(label);
   }

   // To string included for debugging purposes
   public String toString() {
      String result = "";
      Object[] keys = nodes.keySet().toArray();

      result += "Alphabet: " + alphabet + "\n";
      for(int i = 0; i < nodes.size(); i++) {
         result += "Node " + keys[i] + (keys[i].equals(startState) ? " start" : "") + "\n";
         for(int j = 0; j < alphabet.length(); j++) {
            result += "\t" + alphabet.charAt(j) + ": " + nodes.get(keys[i]).getTransition(alphabet.charAt(j)).getLabel() + "\n";
         }
      }

      return result;
   }
}
