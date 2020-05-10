import java.util.ArrayList;
import java.util.Hashtable;

public class Node {
   private int index;
   private Hashtable<Character, ArrayList<Node>> transitions = new Hashtable<Character, ArrayList<Node>>();

   public Node(int index) {
      this.index = index;
   }

   public void addTransition(char onChar, Node toNode) {
      if(transitions.containsKey(onChar)) {
         transitions.get(onChar).add(toNode);
      } else {
         ArrayList<Node> nodes = new ArrayList<Node>();
         nodes.add(toNode);
         transitions.put(onChar, nodes);
      }
   }

   public ArrayList<Node> getTransitions(char onChar) {
      if(transitions.containsKey(onChar)) {
         return transitions.get(onChar);
      } else {
         return new ArrayList<Node>();
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
