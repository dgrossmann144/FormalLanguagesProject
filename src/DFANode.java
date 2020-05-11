import java.util.ArrayList;
import java.util.Hashtable;

public class DFANode {
   private Hashtable<Character, DFANode> transitions = new Hashtable<Character, DFANode>();
   private boolean accepting;
   private ArrayList<Integer> name;

   public DFANode(String alphabet, boolean accepting, ArrayList<Integer> name) {
      this.accepting = accepting;
      for(int i = 0; i < alphabet.length(); i++) {
         transitions.put(alphabet.charAt(i), this);
      }
      this.name = name;
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
   
   public ArrayList<Integer> getName() {
      return name;
   }
   
   public String getStringName() {
      String result = "";
      for(int i = 0; i < name.size(); i++) {
         if(i == name.size() - 1) {
            result += name.get(i);
         } else {
            result += name.get(i) + ", ";
         }
      }
      return result;
   }
}
