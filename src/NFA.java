import java.util.ArrayList;

public class NFA {
	private String alphabet;
	private ArrayList<Node> nodes = new ArrayList<Node>();
	public NFA(String alphabet) {
		this.alphabet = alphabet;
	}
	
	public int addNode() {
		Node newNode = new Node(nodes.size());
		nodes.add(newNode);
		return newNode.getIndex();
	}
	
	public void insertNode(int index) {
	   Node newNode = new Node(index);
	   nodes.add(index, newNode);
	   for(int i = index + 1; i < nodes.size() ; i++) {
	      nodes.get(i).incrementIndex();
	   }
	}
	
	public void addTransition(Node fromNode, char onChar, Node toNode) {
		fromNode.addTransition(onChar, toNode);
	}
	
	public String getAlphabet() {
		return alphabet;
	}
	
	public ArrayList<Node> getNodesList() {
		return nodes;
	}
	
	public Node getNode(int index) {
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
	   ArrayList<Node> appendingNodes = appending.getNodesList();
	   for(int i = 0; i < appendingNodes.size(); i++) {
	      appendingNodes.get(i).setIndex(i + endingNode + 1);
	      nodes.add(appendingNodes.get(i));
	   }
	   if(endingNode != -1) {
	      addTransition(nodes.get(endingNode), '&', nodes.get(endingNode + 1));
	   }
	}
	
	public String toString() {
		String result = "";
		result += "Alphabet: " + alphabet + "\n";
		for(int i = 0; i < nodes.size(); i++) {
			result += "Node " + i + "\n";
			for(int j = 0; j < alphabet.length(); j++) {
				result += "\t" + alphabet.charAt(j) + ": ";
				ArrayList<Node> transitions = nodes.get(i).getTransitions(alphabet.charAt(j));
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
