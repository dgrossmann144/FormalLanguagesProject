import java.util.ArrayList;
import java.util.Hashtable;

public class DFANode {
   // Hashtable where the key is a character in the NFA's alphabet, and the value
   // is the singular node this node can transition to on that character
   private Hashtable<Character, DFANode> transitions = new Hashtable<Character, DFANode>();
   private boolean accepting;
   // The label is a list of indices from the NFA created during the subset
   // construction of this DFA
   private ArrayList<Integer> label;

   public DFANode(String alphabet, boolean accepting, ArrayList<Integer> label) {
      this.accepting = accepting;
      for(int i = 0; i < alphabet.length(); i++) {
         transitions.put(alphabet.charAt(i), this);
      }
      this.label = label;
   }

   // Returns the node that this node transitions to on the given character
   public DFANode getTransition(char onChar) {
      return transitions.get(onChar);
   }

   // Sets the transition to the given node on the given character
   public void setTransition(char onChar, DFANode toNode) {
      transitions.put(onChar, toNode);
   }

   // Returns true if the node accepts
   public boolean isAccepting() {
      return accepting;
   }

   // Return the label of the node
   public ArrayList<Integer> getLabel() {
      return label;
   }

   // Returns the label of the node as a string used in the DOT language format 
   public String getStringLabel() {
      String result = "";
      for(int i = 0; i < label.size(); i++) {
         if(i == label.size() - 1) {
            result += label.get(i);
         } else {
            result += label.get(i) + ", ";
         }
      }
      return result;
   }
}
