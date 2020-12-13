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

	public static Pair<HashMap<AbstractNode, Integer>, HashMap<AbstractNode, AbstractNode>> dijkstra(IGraph graph, AbstractNode start){
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

	public static Pair<HashMap<AbstractNode, Integer>, HashMap<AbstractNode, AbstractNode>> bellman(IGraph graph, AbstractNode start) throws Exception {
		HashMap<AbstractNode, Integer> costs = new HashMap<>();
		HashMap<AbstractNode, AbstractNode> previous = new HashMap<>();
		if (graph.getClass() == DirectedValuedGraph.class) {
			List<DirectedNode> nodes = ((DirectedValuedGraph) graph).getNodes();
			for (AbstractNode node : nodes) {
				costs.put(node, Integer.MAX_VALUE);
				previous.put(node, null);
			}
		}
		if (graph.getClass() == UndirectedValuedGraph.class){
			List<UndirectedNode> nodes = ((UndirectedValuedGraph) graph).getNodes();
			for (AbstractNode node : nodes) {
				costs.put(node, Integer.MAX_VALUE);
				previous.put(node, null);
			}
		}
		LinkedList<AbstractNode> nodesToConsider = new LinkedList<>();
		costs.put(start, 0);
		nodesToConsider.add(start);
		Integer nbIterations = 0;
		if (graph.getClass() == DirectedValuedGraph.class) {
			nbIterations = ((DirectedValuedGraph) graph).getNbArcs() - 1;
		}
		if (graph.getClass() == UndirectedValuedGraph.class) {
			nbIterations = ((UndirectedValuedGraph) graph).getNbEdges() - 1;
		}
		HashMap<AbstractNode, Integer> savedCosts = new HashMap<>();;
		HashMap<AbstractNode, AbstractNode> savedPrevious = new HashMap<>();;
		// Nous faisons une itération de plus afin de vérifier qu'il n'y a pas de cycle négatif
		for(int i = 0; i<=nbIterations; i++){
			if (i == nbIterations){
				savedCosts = (HashMap<AbstractNode, Integer>) costs.clone();
				savedPrevious = (HashMap<AbstractNode, AbstractNode>) previous.clone();
			}
			if (graph.getClass() == DirectedValuedGraph.class) {
				for (AbstractNode node: nodesToConsider
				) {
					for(Map.Entry<DirectedNode, Integer> succ : ((DirectedNode)node).getSuccs().entrySet()){
						Integer newCost = succ.getValue() + costs.get(node);
						if(newCost < costs.get(succ.getKey())){
							costs.put(succ.getKey(), newCost);
							previous.put(succ.getKey(), node);
						}
					}
				}
			}
			if (graph.getClass() == UndirectedValuedGraph.class) {
				for (AbstractNode node: nodesToConsider
				) {
					for(Map.Entry<UndirectedNode, Integer> succ : ((UndirectedNode)node).getNeighbours().entrySet()){
						Integer newCost = succ.getValue() + costs.get(node);
						if(newCost < costs.get(succ.getKey())){
							costs.put(succ.getKey(), newCost);
							previous.put(succ.getKey(), node);
						}
					}
				}
			}
			if (i == nbIterations){
				if(!savedCosts.equals(costs) || !savedPrevious.equals(previous))
					throw new Exception("Présence d'un cycle négatif");
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
		System.out.println("\nDepth-First-Search");
		depthFirstSearch(al, al.getNodes().get(0));
		System.out.println("\nBreath-First-Search");
		breathFirstSearch(al, al.getNodes().get(0));

		// Djikstra test
		System.out.println("\nDjikstra (Directed Graph):");
		int[][] MatrixDjikstra = GraphTools.generateGraphData(5, 20, true, false, true, 100001);
		DirectedValuedGraph valuedGraph = new DirectedValuedGraph(MatrixDjikstra);
		System.out.println(valuedGraph.toString());
		Pair<HashMap<AbstractNode, Integer>, HashMap<AbstractNode, AbstractNode>> djikstraResult = dijkstra(valuedGraph, valuedGraph.getNodes().get(0));
		System.out.println(djikstraResult.costs);
		System.out.println(djikstraResult.previous);

		System.out.println("\nDjikstra (Undirected Graph):");
		UndirectedValuedGraph undirectedValuedGraph = new UndirectedValuedGraph(MatrixDjikstra);
		System.out.println(undirectedValuedGraph.toString());
		Pair<HashMap<AbstractNode, Integer>, HashMap<AbstractNode, AbstractNode>> djikstraResult2 = dijkstra(undirectedValuedGraph, undirectedValuedGraph.getNodes().get(0));
		System.out.println(djikstraResult2.costs);
		System.out.println(djikstraResult2.previous);

		// Bellman test
		System.out.println("\nBellman (Directed Graph):");
		int[][] MatrixBellman = GraphTools.generateGraphData(4, 20, true, false, true, 100001);
		DirectedValuedGraph valuedGraphBellman = new DirectedValuedGraph(MatrixBellman);
		System.out.println(valuedGraphBellman.toString());
		try {
			Pair<HashMap<AbstractNode, Integer>, HashMap<AbstractNode, AbstractNode>> bellmanResult = bellman(valuedGraphBellman, valuedGraphBellman.getNodes().get(0));
			System.out.println(bellmanResult.costs);
			System.out.println(bellmanResult.previous);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("\nBellman (Undirected Graph):");
		UndirectedValuedGraph undirectedValuedGraphBellman = new UndirectedValuedGraph(MatrixBellman);
		System.out.println(undirectedValuedGraphBellman.toString());
		try {
			Pair<HashMap<AbstractNode, Integer>, HashMap<AbstractNode, AbstractNode>> bellmanResult2 = bellman(undirectedValuedGraphBellman, undirectedValuedGraphBellman.getNodes().get(0));
			System.out.println(bellmanResult2.costs);
			System.out.println(bellmanResult2.previous);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
