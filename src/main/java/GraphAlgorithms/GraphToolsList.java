package GraphAlgorithms;

import Abstraction.IGraph;
import AdjacencyList.DirectedGraph;
import AdjacencyList.DirectedValuedGraph;
import AdjacencyList.UndirectedValuedGraph;
import Nodes.AbstractNode;
import Nodes.DirectedNode;
import Nodes.UndirectedNode;

import java.util.*;

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

	public static Pair<HashMap<AbstractNode, Integer>, HashMap<AbstractNode, AbstractNode>> djikstra(IGraph graph, AbstractNode start){
		HashMap<AbstractNode, Integer> costs = new HashMap<>();
		HashMap<AbstractNode, AbstractNode> previous = new HashMap<>();
		PriorityQueue<Map.Entry<AbstractNode, Integer>> toVisit = new PriorityQueue<>(Map.Entry.comparingByValue());
		if (graph.getClass() == DirectedValuedGraph.class) {
			List<DirectedNode> nodes = ((DirectedValuedGraph) graph).getNodes();
			for (AbstractNode node : nodes) {
				costs.put(node, Integer.MAX_VALUE);
				previous.put(node, null);
				toVisit.add(new AbstractMap.SimpleEntry(node, Integer.MAX_VALUE));
			}
		}
		if (graph.getClass() == UndirectedValuedGraph.class){
			List<UndirectedNode> nodes = ((UndirectedValuedGraph) graph).getNodes();
			for (AbstractNode node : nodes) {
				costs.put(node, Integer.MAX_VALUE);
				previous.put(node, null);
				toVisit.add(new AbstractMap.SimpleEntry(node, Integer.MAX_VALUE));
			}
		}
		costs.put(start, 0);
		while(!toVisit.isEmpty()){
			AbstractNode aNode = toVisit.poll().getKey();
			if (aNode.getClass() == DirectedNode.class) {
				for (Map.Entry<DirectedNode, Integer> successor: ((DirectedNode) aNode).getSuccs().entrySet()){
					Integer alt = costs.get(aNode) + successor.getValue();
					if(alt < costs.get(successor.getKey())){
						costs.put(successor.getKey(), successor.getValue());
						previous.put(successor.getKey(), aNode);
					}
				}
			}
			if(aNode.getClass() == UndirectedNode.class) {
				for (Map.Entry<UndirectedNode, Integer> successor: ((UndirectedNode) aNode).getNeighbours().entrySet()){
					Integer alt = costs.get(aNode) + successor.getValue();
					if(alt < costs.get(successor.getKey())){
						costs.put(successor.getKey(), successor.getValue());
						previous.put(successor.getKey(), aNode);
					}
				}
			}
		}
		return new Pair<HashMap<AbstractNode, Integer>, HashMap<AbstractNode, AbstractNode>>(costs, previous);
	}

	public static class Pair<T, U> {
		public final T costs;
		public final U previous;

		public Pair(T t, U u) {
			this.costs= t;
			this.previous= u;
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

		// Djikstra test
		System.out.println("Djikstra:");
		int[][] MatrixDjikstra = GraphTools.generateGraphData(5, 20, true, false, true, 100001);
		DirectedValuedGraph valuedGraph = new DirectedValuedGraph(MatrixDjikstra);
		System.out.println(valuedGraph.toString());
		Pair<HashMap<AbstractNode, Integer>, HashMap<AbstractNode, AbstractNode>> djikstraResult = djikstra(valuedGraph, valuedGraph.getNodes().get(0));
		System.out.println(djikstraResult.costs);
		System.out.println(djikstraResult.previous);
	}
}
