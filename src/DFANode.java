import java.util.Hashtable;

public class DFANode {
   private Hashtable<Character, DFANode> transitions;
   private boolean accepting;
   
   public DFANode(String alphabet, boolean accepting) {
      this.accepting = accepting;
      for(int i = 0; i < alphabet.length(); i++) {
         transitions.put(alphabet.charAt(i), null);
      }
   }
   
   public void setTransition(char onChar, DFANode toNode) {
      transitions.put(onChar, toNode);
   }
   
   public boolean isAccepting() {
      return accepting;
   }
   
   
   

}
