import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

public class DFA {
   private Hashtable<ArrayList<Integer>, DFANode> nodes = new Hashtable<ArrayList<Integer>, DFANode>();
   private String alphabet;
   private ArrayList<Integer> startState;

   public DFA(String alphabet) {
      this.alphabet = alphabet;
   }

   public DFANode addNode(ArrayList<Integer> name, boolean accepting, boolean startState) {
      nodes.put(name, new DFANode(alphabet, accepting, name));
      if(startState) {
         this.startState = name;
      }
      return nodes.get(name);
   }

   public DFANode getNode(ArrayList<Integer> name) {
      return nodes.get(name);
   }

   public boolean isNameInNodes(ArrayList<Integer> name) {
      return nodes.containsKey(name);
   }

   public String getAlphabet() {
      return alphabet;
   }
   
   public boolean computeString(String str) {
      ArrayList<Integer> currentState = startState;
      
      for(int i = 0; i < str.length(); i++) {
         if(alphabet.contains(str.charAt(i) + "")) {
            currentState = nodes.get(currentState).getTransition(str.charAt(i)).getName();
         } else {
            return false;
         }
      }
      
      return nodes.get(currentState).isAccepting();
   }
   
   public void generateDotFile(String dfaFileName) {
      String result = "digraph {\n";
      Object[] keys = nodes.keySet().toArray();
      
      result += "\t\"\" [shape=none];\n";
      for(int i = 0; i < nodes.size(); i++) {
         DFANode node = nodes.get(keys[i]);
         if(node.isAccepting()) {
            result += "\t\"" + node.getStringName() + "\" [shape=doublecircle];\n";
         } else {
            result += "\t\"" + node.getStringName() + "\" [shape=circle];\n";
         }
      }
      
      result += "\t\"\" -> \"" + nodes.get(startState).getStringName() + "\";\n";
      
      for(int i = 0; i < nodes.size(); i++) {
         DFANode node = nodes.get(keys[i]);
         for(int j = 0; j < alphabet.length(); j++) {
            result += "\t\"" + node.getStringName() + "\" -> \"" + node.getTransition(alphabet.charAt(j)).getStringName() + "\" [label=\"" + alphabet.charAt(j) + "\"];\n";
         }
      }
      result += "}";
      
      try {
         File dfaFile = new File(dfaFileName + ".gv");
         if(dfaFile.createNewFile()) {
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

   public String toString() {
      String result = "";
      Object[] keys = nodes.keySet().toArray();

      result += "Alphabet: " + alphabet + "\n";
      for(int i = 0; i < nodes.size(); i++) {
         result += "Node " + keys[i] + (keys[i].equals(startState) ? " start" : "") + "\n";
         for(int j = 0; j < alphabet.length(); j++) {
            result += "\t" + alphabet.charAt(j) + ": " + nodes.get(keys[i]).getTransition(alphabet.charAt(j)).getName()
                  + "\n";
         }
      }

      return result;
   }
}
