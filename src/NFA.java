import java.util.ArrayList;

public class NFA {
   private String alphabet;
   private ArrayList<NFANode> nodes = new ArrayList<NFANode>();

   public NFA(String alphabet) {
      this.alphabet = alphabet;
   }

   public int addNode() {
      NFANode newNode = new NFANode(nodes.size());
      nodes.add(newNode);
      return newNode.getIndex();
   }

   public void insertNode(int index) {
      NFANode newNode = new NFANode(index);
      nodes.add(index, newNode);
      for(int i = index + 1; i < nodes.size(); i++) {
         nodes.get(i).incrementIndex();
      }
   }

   public void addTransition(NFANode fromNode, char onChar, NFANode toNode) {
      fromNode.addTransition(onChar, toNode);
   }

   public String getAlphabet() {
      return alphabet;
   }

   public ArrayList<NFANode> getNodesList() {
      return nodes;
   }

   public NFANode getNode(int index) {
      return nodes.get(index);
   }

   public void applyKleeneStar() {
      insertNode(0);
      int endNodeIndex = addNode();
      addTransition(getNode(0), '&', getNode(1));
      addTransition(getNode(endNodeIndex - 1), '&', getNode(endNodeIndex));
      addTransition(getNode(0), '&', getNode(endNodeIndex));
      addTransition(getNode(endNodeIndex), '&', getNode(0));
   }

   public void appendConcatenate(NFA appending) {
      int endingNode = nodes.size() - 1;
      ArrayList<NFANode> appendingNodes = appending.getNodesList();
      for(int i = 0; i < appendingNodes.size(); i++) {
         appendingNodes.get(i).setIndex(i + endingNode + 1);
         nodes.add(appendingNodes.get(i));
      }
      if(endingNode != -1) {
         addTransition(nodes.get(endingNode), '&', nodes.get(endingNode + 1));
      }
   }

   public void appendChoice(NFA appending) {
      int endingNode = nodes.size() - 1;
      ArrayList<NFANode> appendingNodes = appending.getNodesList();
      for(int i = 0; i < appendingNodes.size(); i++) {
         appendingNodes.get(i).setIndex(i + endingNode + 1);
         nodes.add(appendingNodes.get(i));
      }
      if(endingNode != -1) {
         insertNode(0);
         addTransition(nodes.get(0), '&', nodes.get(1));
         addTransition(nodes.get(0), '&', nodes.get(endingNode + 2));
         addNode();
         addTransition(nodes.get(endingNode + 1), '&', nodes.get(nodes.size() - 1));
         addTransition(nodes.get(nodes.size() - 2), '&', nodes.get(nodes.size() - 1));
      }
   }

   public String toString() {
      String result = "";
      result += "Alphabet: " + alphabet + "\n";
      for(int i = 0; i < nodes.size(); i++) {
         result += "Node " + i + "\n";
         for(int j = 0; j < alphabet.length(); j++) {
            result += "\t" + alphabet.charAt(j) + ": ";
            ArrayList<NFANode> transitions = nodes.get(i).getTransitions(alphabet.charAt(j));
            for(int k = 0; k < transitions.size(); k++) {
               if(k == transitions.size() - 1) {
                  result += transitions.get(k).getIndex();
               } else {
                  result += transitions.get(k).getIndex() + ", ";
               }
            }
            result += "\n";
         }
      }
      return result;
   }
}
