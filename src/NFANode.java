import java.util.ArrayList;
import java.util.Hashtable;

public class NFANode {
   private int index;
   private Hashtable<Character, ArrayList<NFANode>> transitions = new Hashtable<Character, ArrayList<NFANode>>();

   public NFANode(int index) {
      this.index = index;
   }

   public void addTransition(char onChar, NFANode toNode) {
      if(transitions.containsKey(onChar)) {
         transitions.get(onChar).add(toNode);
      } else {
         ArrayList<NFANode> nodes = new ArrayList<NFANode>();
         nodes.add(toNode);
         transitions.put(onChar, nodes);
      }
   }

   public ArrayList<NFANode> getTransitions(char onChar) {
      if(transitions.containsKey(onChar)) {
         return transitions.get(onChar);
      } else {
         return new ArrayList<NFANode>();
      }
   }

   public int getIndex() {
      return index;
   }

   public void incrementIndex() {
      index++;
   }

   public void setIndex(int index) {
      this.index = index;
   }
}
