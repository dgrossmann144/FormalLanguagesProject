import java.util.ArrayList;
import java.util.Hashtable;

public class Node {
	private boolean accepting;
	private Hashtable<Character, ArrayList<Node>> transitions = new Hashtable<Character, ArrayList<Node>>();
	public Node(boolean accepting) {
		this.accepting = accepting;
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
			return null;
		}
	}
	
	public boolean isAccepting() {
		return this.accepting;
	}
	
	public void setAccepting(boolean accepting) {
		this.accepting = accepting;
	}
}
