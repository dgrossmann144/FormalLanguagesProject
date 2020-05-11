import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

   public ArrayList<Integer> getTransitions(ArrayList<Integer> nodesList, char onChar) {
      ArrayList<Integer> result = new ArrayList<Integer>();
      
      for(int i = 0; i < nodesList.size(); i++) {
         ArrayList<NFANode> transitions = nodes.get(nodesList.get(i)).getTransitions(onChar);
         for(int j = 0; j < transitions.size(); j++) {
            result.add(transitions.get(j).getIndex());
         }
      }

      ArrayList<Integer> epsilonTransitions = getEpsilonTransitions(result);
      for(int i = 0; i < epsilonTransitions.size(); i++) {
         result.add(epsilonTransitions.get(i));
      }
      
      return Utils.sort(Utils.removeDuplicates(result));
   }

   public ArrayList<Integer> getEpsilonTransitions(ArrayList<Integer> nodesList) {
      ArrayList<Integer> result = new ArrayList<Integer>();

      for(int i = 0; i < nodesList.size(); i++) {
         result.add(nodes.get(nodesList.get(i)).getIndex());
         NFANode node = nodes.get(nodesList.get(i));
         for(int j = 0; j < node.getTransitions('&').size(); j++) {
            result.add(node.getTransitions('&').get(j).getIndex());
         }
      }
      result = Utils.sort(Utils.removeDuplicates(result));
      
      int startingSize;
      do {
         ArrayList<Integer> newElements = new ArrayList<Integer>();
         startingSize = result.size();
         for(int i = 0; i < result.size(); i++) {
            NFANode node = nodes.get(result.get(i));
            for(int j = 0; j < node.getTransitions('&').size(); j++) {
               newElements.add(node.getTransitions('&').get(j).getIndex());
            }
         }
         result.addAll(newElements);
         result = Utils.sort(Utils.removeDuplicates(result));
      } while(result.size() != startingSize);

      return result;
   }
   
   public boolean doesStatesListAccept(ArrayList<Integer> states) {
      return states.contains(nodes.size() - 1);
   }
   
   public void generateDotFile(String nfaFileName) {
      String result = "digraph {\n";
      
      result += "\t\"\" [shape=none];\n";
      for(int i = 0; i < nodes.size(); i++) {
         if(i == nodes.size() - 1) {
            result += "\t\"" + i + "\" [shape=doublecircle];\n";
         } else {
            result += "\t\"" + i + "\" [shape=circle];\n";
         }
      }
      
      result += "\t\"\" -> \"0\";\n";
      
      for(int i = 0; i < nodes.size(); i++) {
         NFANode node = nodes.get(i);
         for(int j = 0; j < alphabet.length(); j++) {
            if(node.getTransitions(alphabet.charAt(j)).size() > 0) {
               for(int k = 0; k < node.getTransitions(alphabet.charAt(j)).size(); k++) {
                  result += "\t\"" + node.getIndex() + "\" -> \"" + node.getTransitions(alphabet.charAt(j)).get(k).getIndex() + "\" [label=\"" + alphabet.charAt(j) + "\"];\n";
               }
            }
         }
      }
      result += "}";
      
      
      try {
          File nfaFile = new File(nfaFileName + ".gv");
          if(nfaFile.createNewFile()) {
             FileWriter nfaFileWriter = new FileWriter(nfaFileName + ".gv");
             nfaFileWriter.write(result);
             nfaFileWriter.close();
             System.out.println("Successfully created DOT format file for nfa");
          } else {
             System.out.println("Could not create nfa file " + nfaFileName + " because that file already exists");
             return;
          }
       } catch(IOException error) {
          System.out.println("An error occurred while creating nfa file");
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
