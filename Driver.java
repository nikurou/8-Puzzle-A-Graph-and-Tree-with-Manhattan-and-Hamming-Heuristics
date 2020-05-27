import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


/**
 * CS 4200.01: Artificial Intelligence
 * 
 *
 * Programming Assignment #1
 *
 * < Description: Driver class which holds the UI and Main() of the program. The program solves an 8 puzzle with specifically the goal state <0-8>,
 *                using A*Graph and TreeSearch implementations. The user may evaluate with Hamming Distance or Manhattan Distance as a heuristic.
 *                The user may either generate a random solvable puzzle or enter one of their own. 
 *
 * @author Dylan Chung 
 *   
 */

public class Driver {
	
	
	// The Main Function of the Driver Class
	// Responsible for calling the UI into action and subsequently the rest of the program.
	public static void main (String[] args){		
		userInterface();
	}
	
	//The first level of the UI
	public static void userInterface(){
		System.out.println(" \nThis program solves 8-puzzles using the A* Tree/Graph Search algorithms.");
		System.out.println("Please select an option from the following menu: ");
		System.out.println("---------------------------------------------------------- ");
		System.out.println("1. Generate random puzzle configurations ");
		System.out.println("2. Enter a custom configuration. ");
		System.out.println("3. Exit ");
		System.out.println("---------------------------------------------------------- ");
		System.out.print(" Which option?: ");
		Scanner kb = new Scanner(System.in);
		
		
		//list1.add(new int[]{goalTree.getCost(), root.getSearchCost(), (int)root.getElapsedTime()});
		

		
		
		
		int choice = kb.nextInt();
		executeChoice(choice ,kb);
		
	}
	
	// Executes whatever option user choose in UserInterface()
	private static void executeChoice(int choice, Scanner kb) {
		if(choice == 1){ //Generate random until solvable.
			int heuristic = promptHeuristic(kb);
			Puzzle rand = new Puzzle();
			
			System.out.println("This is your generated puzzle");
			rand.printPuzzle();
			Node root = new Node(rand, heuristic); //STARTING NODE
			
			int searchType = inputSearchType(kb);
			
			getOutput(rand, root, heuristic, searchType);
			userInterface();
			
		}
		else if(choice == 2){ //Prompt for user config and execute
			int heuristic = promptHeuristic(kb);
			int [] arr = promptPuzzleConfig(kb);
			Puzzle p1 = new Puzzle(arr);
			
			
			if(p1.isSolvable(arr)==false || p1.isValid(arr) == false){
				System.out.println("Please retry.");
				executeChoice(choice,kb);
				return;
			}
			else{
				
				System.out.println("This is your generated puzzle");
				p1.printPuzzle();
				Node root = new Node(p1, heuristic); //STARTING NODE

				int searchType = inputSearchType(kb); //Get Tree or Graph Search Type
				
				if(searchType == 1){ //GraphSearch
					getOutput(p1, root, heuristic, searchType);
				}
				if(searchType == 2) {
					getOutput(p1, root, heuristic, searchType);
				}
				
				userInterface();
			}
			
			
			
		}
		else if(choice == 3){ //Exit the program
			System.exit(0);
			//kb.close();
			return;
		}
		else{
			System.out.println("The option you selected is not valid. Please re-try and ensure you enter a numerical input that corresponds to your choice.");
			userInterface();
			//kb.close();
			return;
		}
		
	}


	// Remprompts the user for input until a valid option is given. 
	private static int inputSearchType(Scanner kb) {
		promptSearchType();
		int searchType = kb.nextInt(); // 1 graph 2 tree
		
		while(searchType != 1 && searchType != 2){
			System.out.println("Not a valid option. Please try again.");
			promptSearchType();
			searchType = kb.nextInt();
		}
		
		return searchType;
	}

	// Calls methods to print out output based on what Node, Puzzle, Heuristic, and searchType is given.
	private static void getOutput(Puzzle rand, Node root, int heuristic, int searchType) {
		if(searchType == 1 && heuristic != 3){ //ONLY GRAPH SEARCH WITH CHOSEN HEURISTIC
			graphSearch(root, rand, heuristic);
			
		}
		if(searchType == 2  && heuristic != 3) { // ONLY TREE SEARCH WITH CHOSEN HEURISTIC
			treeSearch(root, rand, heuristic);
			
		}
		if(searchType == 1 && heuristic == 3){ //GRAPH SEARCH WITH BOTH HEURISTICS
			System.out.println("\n\n---------------------------------------------------------------------------PLEASE READ ------------------------------------------------------------------------------------------------------------------");
			System.out.println(" When running both heuristics, I have decided not to show the results of each iteration like previous options did. If you see this with no output below it yet, it simply means it's still calculating. Thank you!");
			System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
			
			silentGraphSearch(root, rand, 1);
			root = new Node(rand, heuristic); //Re-initialize so we can clear for next run.
			silentGraphSearch(root, rand, 2);
			
		}
		if(searchType == 2 && heuristic == 3) { //TREE SEARCH WITH BOTH HEURISTICS
			
			System.out.println("\n\n---------------------------------------------------------------------------PLEASE READ -------------------------------------------------------------------------------------------------------------------------");
			System.out.println(" When running both heuristics, I have decided not to show the results of each iteration like previous options did. If you see this with no output below it yet, it simply means it's still calculating. Thank you!");
			System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
			
			silentTreeSearch(root, rand, 1);
			root = new Node(rand, heuristic); //Re-initialize so we can clear for next run.
			silentTreeSearch(root, rand, 2);
		}
		
	}
	
	//Calls AStarTreeSearchNOPRINT and prints out the relevant information
	// Note that this does not print the intermediate steps taken.
	private static void silentTreeSearch(Node root, Puzzle rand, int heuristic) {
		System.out.println();
		Node goalTree = root.AStarTreeSearchNOPRINT(rand, heuristic); 
		System.out.println("The goal has been found USING TREESEARCH and H"+ heuristic+  " and it's : ");
		goalTree.getPuzzle().printPuzzle();
		System.out.println("This was generated from the starting state: ");
		root.getPuzzle().printPuzzle();
		
		printFinalStats(root, goalTree);
		System.out.println();
		
	}

	// Calls the silentGraphSearch and prints out the revelant information
	// Note that this does not print the intermediate steps taken.
	private static void silentGraphSearch(Node root, Puzzle rand, int heuristic) {
		System.out.println();
		Node goalGraph =root.AStarGraphSearchNOPRINT(rand,heuristic); //THE RESULTING GOAL NODE
		System.out.println("The goal has been found USING GRAPHSEARCH and H"+ heuristic+  " and it's : ");
		goalGraph.getPuzzle().printPuzzle();
		System.out.println("This was generated from the starting state: ");
		root.getPuzzle().printPuzzle();
		
	
		printFinalStats(root, goalGraph);
		System.out.println();
		
	}

	// Calls treeSearch and prints out the relevant information
	private static void treeSearch(Node root, Puzzle rand, int heuristic) {
		System.out.println();
		Node goalTree = root.AStarTreeSearch(rand, heuristic); 
		System.out.println("The goal has been found USING TREESEARCH and H"+ heuristic+  " and it's : ");
		goalTree.getPuzzle().printPuzzle();
		System.out.println("This was generated from the starting state: ");
		root.getPuzzle().printPuzzle();
		printFinalStats(root, goalTree);
		System.out.println();
		
	}

	// Calls graphSearch and prints out relevant information
	private static void graphSearch(Node root, Puzzle rand, int heuristic) {
		System.out.println();
		Node goalGraph =root.AStarGraphSearch(rand,heuristic); //THE RESULTING GOAL NODE
		System.out.println("The goal has been found USING GRAPHSEARCH and H"+ heuristic+  " and it's : ");
		goalGraph.getPuzzle().printPuzzle();
		System.out.println("This was generated from the starting state: ");
		root.getPuzzle().printPuzzle();
		printFinalStats(root, goalGraph);
		System.out.println();
	}

	private static void printFinalStats(Node root, Node goal) {
		if(goal.getCurrentDepth(goal)== 0){
			System.out.println("Depth : " + goal.getCurrentDepth(goal) + " SearchCost: " + root.getSearchCost() + " Total Elapsed Time: "  + root.getElapsedTime()+ " nanoseconds" ); 
			goal.printIntermediateNodes(goal);
			
		}
		else {
			System.out.println("Depth : " + goal.getCurrentDepth(goal) + " SearchCost: " + root.getSearchCost() +  " AvgSearchCost: " + root.getSearchCost()/goal.getCurrentDepth(goal) + " Total Elapsed Time: "  + root.getElapsedTime() + " nanoseconds");
			goal.printIntermediateNodes(goal);
		}
		
	}

	private static void promptSearchType() {
		System.out.println("What type of search? : ");
		System.out.println("1. Graph Search ");
		System.out.println("2. Tree Search ");
		
		System.out.println("Please enter your option: ");
		
	}

	private static int[] promptPuzzleConfig(Scanner kb) {
		
		System.out.print("Please enter your integer array: ");
		
		String s1 = kb.nextLine();
		String[] puzzleString = s1.split(" ", 9);
		
		if(puzzleString.length!=9){
			System.out.println("The array is not long enough, please try again");
			return promptPuzzleConfig(kb);
		}
		
		int[] arr = new int[9];
		
		for(int i = 0; i<9; i++){
			try {
				arr[i] = Integer.parseInt(puzzleString[i]);
			}
			catch(NumberFormatException nfe) {
				System.out.println("The array you entered contained invalid input or did not meet conditions. Please try again");
				return promptPuzzleConfig(kb);
			}
		}
		return arr;
	}

	public static int promptHeuristic(Scanner kb){
		
	
		System.out.println("\n---------------------------------------------------------- ");
		System.out.println("Which heuristic would you like to evaluate with?: ");
		System.out.println(" 1. Hamming Distance (The number of misplaced tiles)");
		System.out.println(" 2. Manhattan Distance (The sum of distance away from goal state)");
		System.out.println(" 3. Run Both! (Hamming will be first, followed by Manhattan.");
		System.out.println("---------------------------------------------------------- ");
		//System.out.print(" Which option?: ");
		int heuristic;
		
		// Keep reprompting until we get valid input!
		do {
			System.out.print("Which option?: ");
		    while (!kb.hasNextInt()) {
		        System.out.println("That's not a number!");
		        kb.next();
		    }
		   heuristic = kb.nextInt();
		   if(heuristic >3 || heuristic <= 0){
			   System.out.println("The option you selected is not valid. Please re-try and ensure you enter a numerical input that corresponds to your choice.");
		   }
		 //  return heuristic;
		} 
		while(heuristic<=0 || heuristic > 3); 
		kb.nextLine();
		return heuristic;
		
	}
	
	
}



