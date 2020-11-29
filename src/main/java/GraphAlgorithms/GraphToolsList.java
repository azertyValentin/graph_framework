package GraphAlgorithms;

import AdjacencyList.DirectedGraph;
import Nodes.AbstractNode;
import Nodes.DirectedNode;
import Nodes.UndirectedNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class GraphToolsList  extends GraphTools {

	private static int _DEBBUG =0;

	private static int[] visite;
	private static int[] debut;
	private static int[] fin;
	private static List<Integer> order_CC;
	private static int cpt=0;

	//--------------------------------------------------
	// 				Constructors
	//--------------------------------------------------

	public GraphToolsList(){
		super();
	}

	// ------------------------------------------
	// 				Accessors
	// ------------------------------------------



	// ------------------------------------------
	// 				Methods
	// ------------------------------------------

	// A completer
	public static void depthFirstSearch(DirectedGraph graph, AbstractNode start) {
		HashMap<AbstractNode, Boolean> mark = new HashMap<>();
		for (AbstractNode node : graph.getNodes()) {
			mark.put(node, false);
		}
		Stack<AbstractNode> toVisit = new Stack<>();
		toVisit.push(start);
		while (!toVisit.isEmpty()) {
			System.out.println(mark);
			AbstractNode actualNode = toVisit.pop();
			mark.put(actualNode, true);
			Map<DirectedNode, Integer> successors = ((DirectedNode) actualNode).getSuccs();
			for (Map.Entry<DirectedNode, Integer> successor : successors.entrySet()) {
				if (!mark.get(successor.getKey())) {
					toVisit.push(successor.getKey());
				}
			}
		}

	}

	public static void breathFirstSearch(DirectedGraph graph, AbstractNode start) {

		HashMap<AbstractNode, Boolean> mark = new HashMap<>();
		for (AbstractNode node : graph.getNodes()) {
			mark.put(node, false);
		}
		mark.put(start, true);
		Stack<AbstractNode> toVisit = new Stack<>();
		toVisit.push(start);
		while (!toVisit.isEmpty()) {
			System.out.println(mark);
			AbstractNode actualNode = toVisit.pop();
			if (actualNode.getClass() == UndirectedNode.class) {
				Map<UndirectedNode, Integer> neighbours = ((UndirectedNode) actualNode).getNeighbours();
				for (Map.Entry<UndirectedNode, Integer> neighbour: neighbours.entrySet()) {
					if (!mark.get(neighbour.getKey())) {
						mark.put(neighbour.getKey(), true);
						toVisit.push(neighbour.getKey());
					}
				}
			} else if (actualNode.getClass() == DirectedNode.class) {
				Map<DirectedNode, Integer> successors = ((DirectedNode) actualNode).getSuccs();
				for (Map.Entry<DirectedNode, Integer> successor: successors.entrySet()) {
					if (!mark.get(successor.getKey())) {
						mark.put(successor.getKey(), true);
						toVisit.push(successor.getKey());
					}
				}
			}
		}
	}


	public static void main(String[] args) {
		int[][] Matrix = GraphTools.generateGraphData(10, 20, false, false, true, 100001);
		GraphTools.afficherMatrix(Matrix);
		DirectedGraph al = new DirectedGraph(Matrix);
		System.out.println(al);

		// A completer
		System.out.println("Depth-First-Search");
		depthFirstSearch(al, al.getNodes().get(0));
		System.out.println("Breath-First-Search");
		breathFirstSearch(al, al.getNodes().get(0));
	}
}
