import java.util.ArrayList;

public class NFA {
	private String alphabet;
	private ArrayList<Node> nodes = new ArrayList<Node>();
	public NFA(String alphabet) {
		this.alphabet = alphabet;
	}
	
	public void addNode(boolean accepting) {
		Node newNode = new Node(accepting);
		this.nodes.add(newNode);
	}
	
	public void addTransition(int fromNode, char onChar, int toNode) {
		this.nodes.get(fromNode).addTransition(onChar, this.nodes.get(toNode));
	}
}
