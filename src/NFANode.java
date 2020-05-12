import java.util.ArrayList;
import java.util.Hashtable;

public class NFANode {
   // The index in the ArrayList that the node is in, also serves as the label for
   // the node
   private int index;
   // Hashtable where the key is a character in the NFA's alphabet, and the value
   // is the list of nodes this node can transition to on that character
   private Hashtable<Character, ArrayList<NFANode>> transitions = new Hashtable<Character, ArrayList<NFANode>>();

   public NFANode(int index) {
      this.index = index;
   }

   // Add a transition to this node to the given node on the given character
   public void addTransition(char onChar, NFANode toNode) {
      if(transitions.containsKey(onChar)) {
         // If the transitions table already has the character add the new node to that
         // existing list
         transitions.get(onChar).add(toNode);
      } else {
         // If the transitions table does not have that character, create the list for it
         // with the new node and add it to the table
         ArrayList<NFANode> nodes = new ArrayList<NFANode>();
         nodes.add(toNode);
         transitions.put(onChar, nodes);
      }
   }

   // Returns the list of nodes that this node can transition to on the given character
   public ArrayList<NFANode> getTransitions(char onChar) {
      if(transitions.containsKey(onChar)) {
         return transitions.get(onChar);
      } else {
         // Returns an empty list if the character is not found in the table
         return new ArrayList<NFANode>();
      }
   }

   // Returns the index in the NFA nodes list which is also used as the label for the node
   public int getIndex() {
      return index;
   }

   // Increase the index of the node by one
   public void incrementIndex() {
      index++;
   }

   // Update the index to a new value
   public void setIndex(int index) {
      this.index = index;
   }
}
