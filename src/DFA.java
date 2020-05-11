import java.util.ArrayList;
import java.util.Hashtable;

public class DFA {
   private Hashtable<ArrayList<Integer>, DFANode> nodes = new Hashtable<ArrayList<Integer>, DFANode>();
   private String alphabet;

   public DFA(String alphabet) {
      this.alphabet = alphabet;
   }

   public DFANode addNode(ArrayList<Integer> name, boolean accepting, boolean startState) {
      nodes.put(name, new DFANode(alphabet, accepting, name, startState));
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

   public String toString() {
      String result = "";
      Object[] keys = nodes.keySet().toArray();

      result += "Alphabet: " + alphabet + "\n";
      for(int i = 0; i < nodes.size(); i++) {
         result += "Node " + keys[i] + (nodes.get(keys[i]).isStart() ? " start" : "") + "\n";
         for(int j = 0; j < alphabet.length(); j++) {
            result += "\t" + alphabet.charAt(j) + ": " + nodes.get(keys[i]).getTransition(alphabet.charAt(j)).getName()
                  + "\n";
         }
      }

      return result;
   }
}
