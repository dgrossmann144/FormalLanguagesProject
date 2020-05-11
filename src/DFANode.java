import java.util.ArrayList;
import java.util.Hashtable;

public class DFANode {
   private Hashtable<Character, DFANode> transitions = new Hashtable<Character, DFANode>();
   private boolean accepting;
   private ArrayList<Integer> name;
   private boolean startState;

   public DFANode(String alphabet, boolean accepting, ArrayList<Integer> name, boolean startState) {
      this.accepting = accepting;
      for(int i = 0; i < alphabet.length(); i++) {
         transitions.put(alphabet.charAt(i), this);
      }
      this.name = name;
      this.startState = startState;
   }

   public DFANode getTransition(char onChar) {
      return transitions.get(onChar);
   }

   public void setTransition(char onChar, DFANode toNode) {
      transitions.put(onChar, toNode);
   }

   public boolean isAccepting() {
      return accepting;
   }
   
   public boolean isStart() {
      return startState;
   }
   
   public ArrayList<Integer> getName() {
      return name;
   }
}
