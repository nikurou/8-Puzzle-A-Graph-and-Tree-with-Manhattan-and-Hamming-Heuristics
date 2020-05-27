import java.util.Arrays;
import java.util.HashSet;

import java.util.PriorityQueue;

/**
 * CS 4200.01: Artificial Intelligence
 * 
 *
 * Programming Assignment #1
 *
 * < Description: Node Class which holds a puzzle, parent, child, and other fields which are initialized in the constructor. 
 * 				  Various functions exist to help with the operation of the nodes.
 * 			      Also holds both the Tree and Graph Search algorithms.
 *                
 *
 * @author Dylan Chung 
 *   
 */

public class Node implements Comparable<Node>{
	private Puzzle p1;
	private Node parent;
	private Node child;
	
	private int heuristic; //1 for h1, 2 for h2
	private int action; // 1 = left, 2 = right, 3 = up, 4 = down.
	private int pathCost; //Path-Cost 
	private int searchCost;
	
	private long startTime;
	//private int depth; ???????
	
	
	//Constructor for the root node
	public Node(Puzzle p1, int heuristic){
		this.p1 = p1;
		setParent(null);
		this.heuristic = heuristic;
		this.action = 4; // 4 = root, 0 = left, 1 = right, 2 = up, 3 = down INTO the blank space.
		pathCost = p1.getHeuristicValue(heuristic); //There is no pathcost at this point because it's starting node. So jsut add the H value.
		startTime = System.nanoTime();
		
	}
	
	// Constructor for child nodes.
	public Node(Puzzle p1, int heuristic, Node parent, int action){
		child = this;
		this.p1 = p1;
		this.setParent(parent);
		this.heuristic = heuristic;
		this.action = action; // 0 = root, 1 = left, 2 = right, 3 = up, 4 = down INTO the blank space.
		pathCost = getCurrentDepth(this) + p1.getHeuristicValue(heuristic); //Cost = path-cost + heuristicvalue 
		startTime = System.nanoTime();
		
	}
	
	// AStarTreeSearch with intermediate steps printed out
	public Node AStarTreeSearch(Puzzle p1, int heuristic){
		Node root = new Node(p1, heuristic);					  // Original Puzzle Node
		PriorityQueue<Node> frontier = new PriorityQueue<Node>(); // Holds Nodes to do in priority queue based on lowest overall cost.
		frontier.add(root);										  // Add OG puzzle to the frontier	
	
		int iteration = 0;

		while(frontier.isEmpty()== false) { 
			iteration++;
			System.out.println("ITERATION # " + iteration);
		
			Node current = frontier.poll(); //retrieves and removes head of queue. 
			
			if(current.isGoal() == true ){
				return current;  
			}

			int [] possibleActions = current.getPossibleSwappableIndex();
			updateSearchCost(possibleActions);
	
			System.out.println("\n*************************************************************************");
			System.out.println("\nParent\n");
			current.getPuzzle().printPuzzle();
			System.out.println("COST = " + current.getCost());
			
			int depth = getCurrentDepth(current);
			System.out.println("Current Depth == "+ depth);
			
			for(int i = 0; i<4; i++){
				if(possibleActions[i] != -1){ //If it's not an impossible action.
					Puzzle p2 = getNewPuzzle(possibleActions[i], current);
					Node child = new Node(p2, heuristic, current, i); //Create a child node based on that action.
					
					System.out.println("\nChild ");
					child.getPuzzle().printPuzzle();
					System.out.println("COST = " + child.getCost() + " Current Depth = "+ (depth+1) + "	Time: " + child.getElapsedTime() + "ms");
					
					if(frontier.contains(child)==false)
						frontier.add(child);
					}
				}
			}
			System.out.println("*************************************************************************");
			return root;
		}
	
	// AStarTreeSearch with no intermediate steps printed out
	public Node AStarTreeSearchNOPRINT(Puzzle p1, int heuristic){
		Node root = new Node(p1, heuristic);					  // Original Puzzle Node
		PriorityQueue<Node> frontier = new PriorityQueue<Node>(); // Holds Nodes to do in priority queue based on lowest overall cost.
		frontier.add(root);										  // Add OG puzzle to the frontier	
	
		while(frontier.isEmpty()== false) { 
			Node current = frontier.poll(); //retrieves and removes head of queue. 
			
			if(current.isGoal() == true ){
				return current;  
			}
			

			int [] possibleActions = current.getPossibleSwappableIndex();
			updateSearchCost(possibleActions);

			
			for(int i = 0; i<4; i++){
				if(possibleActions[i] != -1){ //If it's not an impossible action.
					Puzzle p2 = getNewPuzzle(possibleActions[i], current);
					Node child = new Node(p2, heuristic, current, i); //Create a child node based on that action.
					
					if(frontier.contains(child)==false)
						frontier.add(child);
					}
				}
			}
			return root;
		}
	
	// AStarGraphSearch, but contains print statements to show intermediate steps
	public Node AStarGraphSearch(Puzzle p1, int heuristic){
		Node root = new Node(p1, heuristic);							  // Original Puzzle Node
		PriorityQueue<Node> frontier = new PriorityQueue<Node>(); // Holds Nodes to do in priority queue based on lowest overall cost.
		frontier.add(root);										  // Add OG puzzle to the frontier	
		HashSet<Node> exploredSet = new HashSet<Node>();          // Explored Puzzles
		int iteration = 0;

		while(frontier.isEmpty()== false) { //frontier.isEmpty()== false
			iteration++;
			System.out.println("ITERATION # " + iteration);
		
			Node current = frontier.poll(); //retrieves and removes head of queue. 
			
			if(current.isGoal() == true ){
				return current;  //TODO: Go up parents to get full solution later.
			}
			
			exploredSet.add(current);
			
			// 0 = left, 1 = right, 2 = up, 3 = down.
			int [] possibleActions = current.getPossibleSwappableIndex();
			updateSearchCost(possibleActions);
			
	
			System.out.println("\n*************************************************************************");
			System.out.println("\nParent\n");
			current.getPuzzle().printPuzzle();
			System.out.println("COST = " + current.getCost());
			
			int depth = getCurrentDepth(current);
			System.out.println("Current Depth == "+ depth);
			
			for(int i = 0; i<4; i++){
				if(possibleActions[i] != -1){ //If it's not an impossible action.
					
					//System.out.println("possibleActions[i] = " + possibleActions[i] + " i : " + i);
					Puzzle p2 = getNewPuzzle(possibleActions[i], current);
					
					Node child = new Node(p2, heuristic, current, i); //Create a child node based on that action.
					
					System.out.println("\nChild ");
					child.getPuzzle().printPuzzle();
					System.out.println("COST = " + child.getCost() + " Current Depth = "+ (depth+1) + "	Time: " + child.getElapsedTime() + "ms");
				
					//If child node is not already in explored or frontier, then insert it into frontier
					if(exploredSet.contains(child) == false){ //|| frontier.contains(child) == false
						frontier.add(child);
					}
				}
			}
			System.out.println("*************************************************************************");
		}
		return root; //It should not reach this return statement, because it should exit from the loop. 
	}
	
	// AStarGraphSearch, but no print statements to show intermediate steps.
	public Node AStarGraphSearchNOPRINT(Puzzle p1, int heuristic){
		Node root = new Node(p1, heuristic);							  // Original Puzzle Node
		PriorityQueue<Node> frontier = new PriorityQueue<Node>(); // Holds Nodes to do in priority queue based on lowest overall cost.
		frontier.add(root);										  // Add OG puzzle to the frontier	
		HashSet<Node> exploredSet = new HashSet<Node>();          // Explored Puzzles

		while(frontier.isEmpty()== false) { //frontier.isEmpty()== false
			Node current = frontier.poll(); //retrieves and removes head of queue. 
			if(current.isGoal() == true ){
				return current;  //TODO: Go up parents to get full solution later.
			}
			
			exploredSet.add(current);
			int [] possibleActions = current.getPossibleSwappableIndex();
			updateSearchCost(possibleActions);
		
			for(int i = 0; i<4; i++){
				if(possibleActions[i] != -1){ //If it's not an impossible action.
					
					Puzzle p2 = getNewPuzzle(possibleActions[i], current);
					Node child = new Node(p2, heuristic, current, i); //Create a child node based on that action.
					//If child node is not already in explored or frontier, then insert it into frontier
					if(exploredSet.contains(child) == false){ 
						frontier.add(child);
					}
				}
			}
		}
		return root; //It should not reach this return statement, because it should exit from the loop. 
	}
	
	
	private void updateSearchCost(int[] possibleActions) {
		for(int i = 0; i<4; i++){
			if(possibleActions[i] != -1)
				searchCost++;
		}
	}
	

	// Returns a new Puzzle Object to use as a child by swapping the contents of the index with the contents of blankspace index.
	// Does not alter the contents of the current node.
	public Puzzle getNewPuzzle(int index, Node current){
		int[] oldPuzzleArray = current.getProblem();
		int blankSpaceIndex = current.getBlankSpaceIndex();
			
		int[] newPuzzleArray = oldPuzzleArray.clone();
				
		//Swap Indexes
		newPuzzleArray[blankSpaceIndex] = oldPuzzleArray[index]; // Moves the content of moveable index into 
		newPuzzleArray[index] = 0; //We moved the blank space here.

		return new Puzzle(newPuzzleArray);
	}
	
	public int getSearchCost(){
		return searchCost;
	}
	
	
	public void addChild(Puzzle p2, int heuristic, Node parent, int action){
		child = new Node(p2, heuristic, parent, action);
	}
	
	public int getCurrentDepth(Node n) {
		int depth = 0;
		while(n.getParent()!= null){ 
			n = n.getParent();
			
			depth++;
		}
		//System.out.println("YOUR CURRENT DEPTH: " + depth);
		return depth;
	}

	// Returns the action done by the node.
	public int getAction() {
		return action;
	}
	
	public long getElapsedTime() {
		long elapsedTime = (System.nanoTime() - startTime);
		return elapsedTime;
	}

	public int[] getPossibleSwappableIndex(){
		return p1.getPossibleSwappableIndex();
	}
	
	//returns parent of the node
	public Node getParent() {
		return parent;
	}
	
	public int[] getProblem() {
		return p1.getPuzzleArray(); 
	}
	
	public boolean isGoal(){
		return p1.isGoal();
	}

	public Puzzle getPuzzle() {
		return p1;
	}

	public int getBlankSpaceIndex() {
		return p1.getBlankSpaceIndex();
	}

	public int getCost() {
		return pathCost;
	}

	// overwrites compareTo so frontier can automatically add nodes by lowest pathcost.
	public int compareTo(Node n) {
		if(this.getCost() < n.getCost()){
			return -1;
		}else if(this.getCost()>n.getCost()){
			return 1;
		} return 0;
	}
	
	
	// overridden method so that hashset can determine if a node is equal or not
	public boolean equals(Object obj){
		if(!(obj instanceof Node))
			return false;
		if(obj == this)
			return true;
		
		return Arrays.equals(this.getPuzzle().getPuzzleArray(), ((Node) obj).getPuzzle().getPuzzleArray()); //If the puzzle is the same, then it's equal. 
	}
	
	
	//generate unique hashcode based on the puzzle's configurration
	public int hashCode(){
		int [] arr = this.getProblem();
		int code = 0;
		for( int i=0; i<9;i++){
			code += arr[i] * i;
		}
		return code;
	}

	
	public void setParent(Node parent) {
		this.parent = parent;
	}

	public void setCost(int i) {
		this.pathCost = i;
		
	}
	
	// Prints all the intermediate nodes that exist between the Goal Node to the Root node. 
	public void printIntermediateNodes(Node goal){
		Node[] arr = new Node[goal.getCurrentDepth(goal)];
		
		System.out.println("\n---------------------------Now printing all intermediate nodes!---------------------------");
		
		int i = 0;
		while(goal.getParent()!=null){
			arr[i] = goal;
			goal = goal.getParent();
			
			i++;
		}
		
		int counter = 0;
		for(int a = arr.length-1; a>=0; a--){
			if(counter % 3 == 0 && counter!=0){
				System.out.println();
			}
			counter++;
			System.out.print(Arrays.toString(arr[a].getProblem()) + " --> ");
		}
		//System.out.println(Arrays.toString(goal.getProblem()));
		System.out.println();
		
		System.out.println("-------------------------------------Complete!--------------------------------------------");
	}


}

	

