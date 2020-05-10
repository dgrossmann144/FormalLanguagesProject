import java.util.Hashtable;

public class DFA {
   private Hashtable<String, DFANode> nodes = new Hashtable<String, DFANode>();
   private String alphabet;
   
   public DFA(String alphabet) {
      this.alphabet = alphabet;
   }
   
   public void addNode(String name, boolean accepting) {
      nodes.put(name, new DFANode(alphabet, accepting));
   }
   
   public boolean isNameInNodes(String name) {
      return nodes.containsKey(name);
   }
}
