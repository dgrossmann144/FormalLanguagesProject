import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class NFA {
   // The alphabet of the NFA
   private String alphabet;
   // List of nodes in the NFA it is always in a state where the node in the first
   // position is the start state and the node in the last position is the only
   // accepting state
   private ArrayList<NFANode> nodes = new ArrayList<NFANode>();

   public NFA(String alphabet) {
      this.alphabet = alphabet;
   }

   // Adds a new node to the NFA and returns its position
   public int addNode() {
      NFANode newNode = new NFANode(nodes.size());
      nodes.add(newNode);
      return newNode.getIndex();
   }

   // Inserts a new node at the given index and increments the index of all nodes
   // above it
   public void insertNode(int index) {
      NFANode newNode = new NFANode(index);
      nodes.add(index, newNode);
      for(int i = index + 1; i < nodes.size(); i++) {
         nodes.get(i).incrementIndex();
      }
   }

   // Adds a transition from the given node, to the given node, on a given
   // character
   public void addTransition(NFANode fromNode, char onChar, NFANode toNode) {
      fromNode.addTransition(onChar, toNode);
   }

   // Wraps the NFA in a Kleene Star
   public void applyKleeneStar() {
      // Insert node at the start so the front is always the starting node
      insertNode(0);
      // Insert node at the end
      int endNodeIndex = addNode();

      // Add transitions between the new and old nodes as necessary
      addTransition(getNode(0), '&', getNode(1));
      addTransition(getNode(endNodeIndex - 1), '&', getNode(endNodeIndex));
      addTransition(getNode(0), '&', getNode(endNodeIndex));
      addTransition(getNode(endNodeIndex), '&', getNode(0));
   }

   // Concatenates the given NFA to the end of this one
   public void appendConcatenate(NFA appending) {
      // Saves the current end node for adding transitions after the new nodes have
      // been added
      int endingNode = nodes.size() - 1;

      // Add all the nodes from the given NFA onto this one and update their indexes
      ArrayList<NFANode> appendingNodes = appending.getNodesList();
      for(int i = 0; i < appendingNodes.size(); i++) {
         appendingNodes.get(i).setIndex(i + endingNode + 1);
         nodes.add(appendingNodes.get(i));
      }

      // Condition needed if the NFA had no nodes before this point
      if(endingNode != -1) {
         addTransition(nodes.get(endingNode), '&', nodes.get(endingNode + 1));
      }
   }

   // Adds choice to NFA to go to what was there before or the new NFA
   public void appendChoice(NFA appending) {
      // Saves the current end node for adding transitions after the new nodes have
      // been added
      int endingNode = nodes.size() - 1;

      // Add all the nodes from the given NFA onto this one and update their indexes
      ArrayList<NFANode> appendingNodes = appending.getNodesList();
      for(int i = 0; i < appendingNodes.size(); i++) {
         appendingNodes.get(i).setIndex(i + endingNode + 1);
         nodes.add(appendingNodes.get(i));
      }

      // Condition needed if the NFA had no nodes before this point
      if(endingNode != -1) {
         insertNode(0);
         addTransition(nodes.get(0), '&', nodes.get(1));
         addTransition(nodes.get(0), '&', nodes.get(endingNode + 2));
         addNode();
         addTransition(nodes.get(endingNode + 1), '&', nodes.get(nodes.size() - 1));
         addTransition(nodes.get(nodes.size() - 2), '&', nodes.get(nodes.size() - 1));
      }
   }

   // Given a list of nodes, return the list of nodes that can be reached when
   // attempting to transition on the given character, used to generate the DFA
   public ArrayList<Integer> getTransitions(ArrayList<Integer> nodesList, char onChar) {
      ArrayList<Integer> result = new ArrayList<Integer>();

      // From each node, add the list of nodes that it can transition to on the given
      // character
      for(int i = 0; i < nodesList.size(); i++) {
         ArrayList<NFANode> transitions = nodes.get(nodesList.get(i)).getTransitions(onChar);
         for(int j = 0; j < transitions.size(); j++) {
            result.add(transitions.get(j).getIndex());
         }
      }

      // From each node, additionally add any nodes that can be reached on the empty
      // string
      ArrayList<Integer> epsilonTransitions = getEpsilonTransitions(result);
      for(int i = 0; i < epsilonTransitions.size(); i++) {
         result.add(epsilonTransitions.get(i));
      }

      // Remove duplicates and sort the list of nodes
      return Utils.sort(Utils.removeDuplicates(result));
   }

   // Given a list of nodes, return the list of nodes that can be reached on the
   // empty string
   public ArrayList<Integer> getEpsilonTransitions(ArrayList<Integer> nodesList) {
      ArrayList<Integer> result = new ArrayList<Integer>();

      // From each node add the list of nodes that can be reached on the empty string
      for(int i = 0; i < nodesList.size(); i++) {
         result.add(nodes.get(nodesList.get(i)).getIndex());
         NFANode node = nodes.get(nodesList.get(i));
         for(int j = 0; j < node.getTransitions('&').size(); j++) {
            result.add(node.getTransitions('&').get(j).getIndex());
         }
      }
      result = Utils.sort(Utils.removeDuplicates(result));

      // Each new node that is added could have another transition on the empty
      // string, so we need to keep checking until no new nodes are added
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

   // Given a list of node indices, returns true if it includes the last index
   // which means it is an accepting state
   public boolean doesStatesListAccept(ArrayList<Integer> states) {
      return states.contains(nodes.size() - 1);
   }

   // Create the DOT language format file in the given file name
   public void generateDotFile(String nfaFileName) {
      // Build the string before writing it to the file
      String result = "digraph {\n";

      // Add the list of nodes
      result += "\t\" \" [shape=none];\n";
      for(int i = 0; i < nodes.size(); i++) {
         // Last index is the accepting node so its shape is a double circle
         if(i == nodes.size() - 1) {
            result += "\t\"" + i + "\" [shape=doublecircle];\n";
         } else {
            result += "\t\"" + i + "\" [shape=circle];\n";
         }
      }

      // Add arrow to point to starting node
      result += "\t\" \" -> \"0\";\n";

      // Add arrows for every node for every transition
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

      // Write to the file
      try {
         // Create the file
         File nfaFile = new File(nfaFileName + ".gv");
         if(nfaFile.createNewFile()) {
            // If creating the file was successful, write the formatted string to it
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

   // Returns the alphabet of the NFA
   public String getAlphabet() {
      return alphabet;
   }

   // Returns the list of nodes
   public ArrayList<NFANode> getNodesList() {
      return nodes;
   }

   // Returns the node at the given index
   public NFANode getNode(int index) {
      return nodes.get(index);
   }

   // To string included for debugging purposes
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
