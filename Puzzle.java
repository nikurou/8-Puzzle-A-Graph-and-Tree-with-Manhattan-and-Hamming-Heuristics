
import java.util.Random;
import java.util.Arrays;

/**
 * CS 4200.01: Artificial Intelligence
 * 
 *
 * Programming Assignment #1
 *
 * < Description: Puzzle class contains fields puzzle, h1,h2, totalCost, and direction as well as the appropriate getter/setter methods for them
 * 				  Also contains constructors to initialize the fields and check that the values being initailized will be solvable or legal. 
 *                
 *
 * @author Dylan Chung 
 *   
 */


public class Puzzle {
	//Field Variables
	private int [] puzzle; //An integer array representation of the puzzle.
	private int h1; // misplaced tiles (HAMMING)
	private int h2; // sum of distance off (MANHATTAN)
	private int totalCost; //Cost = PathCost - Est Cost to Goal 
	private int direction; // The direction in which the action of moving one index into the blankspace came from.
						   
	
	//Default Constructor 
	public Puzzle(){
		puzzle = new int[]{0,1,2,3,4,5,6,7,8};
		puzzle = shuffle(puzzle,puzzle.length);
		//System.out.println(Arrays.toString(puzzle));
		h1 = Hamming(puzzle);
		h2 = Manhattan(puzzle);
		//totalCost = pathCost + h1; 
		//totalCost = pathCost + h2;
		totalCost = 0; 
		direction = 0;
	}
	
	

	//Constructor for user given initial puzzle state
	public Puzzle(int [] puzzle){
		//Then this configuration is legal and solvable.
		if(isValid(puzzle) == true && isSolvable(puzzle) == true){ 
			this.puzzle = puzzle;
			h1 = Hamming(puzzle);
			h2 = Manhattan(puzzle);
			direction = 0;
		}
		else{
			System.out.println("Sorry, this configuration is not solvable or valid.");
		}
	}
	
	// Prints the current puzzle config in a grid form.
	public void printPuzzle(){
		for(int r = 0; r<=2; r++){ 		// row 
			System.out.print("[ ");
			for(int c = 0; c<=2; c++){   // column
				System.out.print(puzzle[(r*3) + c]+ " ");
			}
			System.out.println("]");
		}
	}
	
	public int[] getPossibleSwappableIndex(){
		int blankSpaceIndex = getBlankSpaceIndex();
		int[] possibleSwappableIndex = new int[4];
		
		int upIndex = blankSpaceIndex -3;
		int downIndex = blankSpaceIndex +3;
		int leftIndex = blankSpaceIndex -1;
		int rightIndex = blankSpaceIndex +1;
		
		// 0 = left, 1 = right, 2 = up, 3 = down.
		if(blankSpaceIndex % 3 != 0 && leftIndex >=0){
			possibleSwappableIndex[0] = leftIndex;
		}
		else possibleSwappableIndex[0] = -1;
		
		if(rightIndex % 3 != 0 && rightIndex <= 8){
			possibleSwappableIndex[1] = rightIndex;
		}
		else possibleSwappableIndex[1] = -1;
		
		if(upIndex < 8 && upIndex >= 0){ 
			possibleSwappableIndex[2] = upIndex;
		}
		else possibleSwappableIndex[2] = -1;
		
		if(downIndex <= 8 && downIndex >= 0){
			possibleSwappableIndex[3] = downIndex;
		}
		else possibleSwappableIndex[3] = -1;
		
		return possibleSwappableIndex;
		
		
	}
	
	// Finds the index of element 0 in the array, which represents the blankspace.
	public int getBlankSpaceIndex() {
		int blankindex=0;
		for(int i = 0; i<puzzle.length; i++){
			if(puzzle[i] == 0)	
				blankindex = i;
		}
	return blankindex;
	}

	// Returns the puzzle array
	public int[] getPuzzleArray(){
		return puzzle;
	}
	
	// Getter method for local field variable h1 
	// This h1 variable represents the heuristic value calculated by Hamming Distance.
	public int getH1(){
		return h1;
	}
	
	// Getter method for local field variable h2
	// h2 variable represents the heuristic value calculated by Manhattan Distance.
	public int getH2(){
		return h2;
	}
	
	// Getter method for returning local field variable totalCost
	public int getTotalCost(){
		return totalCost;
	}
	

	// Getter method for returning local field variable direction
	public int getDirection(){
		return direction;
	}
	
	// Setter method to set local field variable totalCost
	public void setTotalCost(int totalCost){
		this.totalCost = totalCost;
	}
	

	/*
	 *  Setter method to specify the direction in which the action of moving one index into the blankspace came from.
	 *  Used later to differentiate one of the four possible child nodes from eachother. 
	 *  Let 0 represent rootnode or no direction taken.
	 *  1 = left, 2 = right, 3 = up, 4 = down.
	 */
	public void setDirection(int i){
		if( 0 <= i && i <= 4){ //Between 0-4
			direction = i;
		}
	}
	
	// Checks if the current puzzle configuration is equivalent to the goal state. 
	// Returns true if it is, else return false.
	public boolean isGoal(){
		for(int i = 0; i < puzzle.length; i++){
			if(puzzle[i] != i){
				//System.out.println(puzzle[i] + " == " +i );
				return false;
			}
		}
		return true;
	}
	
	// Checks if puzzle input is 9 different numbers and only has 9 numbers or if it's "Grammat
	 public boolean isValid(int[] arr) {
		 boolean isValid = true;
		 
		 if(arr.length == 9){
			 int[] tempArr = arr.clone(); 				//We don't want to sort the original array, and since Java copies by reference, we clone arr.
			 Arrays.sort(tempArr); 						//Sort the array ascending order.
			 for(int i = 0; i < tempArr.length; i++){ //Because 0 - 8 can only appear once each in the puzzle
				 if(tempArr[i] != i){ 				   // We can ensure numbers 0-8 appear and appear only once by sorting it 
					 isValid = false;			       // and comparing it with a loop that iterates through 0-8.
				 }
			 }
		 }
		 //System.out.println("This puzzle is grammatically valid.");
		 return isValid;
	
	}

	// Given an array and it's size, shuffle the indices until 
	// a valid 8 puzzle configuration is found.
	private int[] shuffle(int[] puzzle, int len) {
		Random r = new Random();
		
		for(int i = len-1; i>0; i--){
			//random index from 0 to i
			int j = r.nextInt(i+1); 
			
			//swap 
			int temp = puzzle[i];
			puzzle[i]  = puzzle[j];
			puzzle[j] = temp;
		}
		
		// If under heuristic Hamming it cannot be solved, re-shuffle the array.
		if(isSolvable(puzzle) == false){
			return shuffle(puzzle,len);
		}
		else
			return puzzle;
	}

	/* 
	 * Heuristic using Hamming Distance Heuristic
	 * Returns true if the puzzle can be solved, else false
	 * Also gives us the heuristic value to be used for evaluation and is set to local field variable h1.
	 * Hamming is calculated by the number of misplaced tiles present.
	 */
	public boolean isSolvable(int[] puzzle) {
		int counter = 0;
		
		for(int i = 0; i < puzzle.length-1; i++){ // We don't need to evaluate the last position.
			
			for(int j=i+1; j< puzzle.length; j++){ 
				//Then we found an inversion.
				if(puzzle[i] > puzzle[j] && puzzle[j] != 0 && puzzle[i]!=0 && i!=j){		
					counter++;		
				}
			}
		}
	//	System.out.println("Number of Inversions == " + counter);
		if(counter % 2 == 0){ // then it's even
			return true;
		}
		else	//then it's odd, reject as unsolvable
			return false;
	}
	
	
	//Count misplaced tiles
	private int Hamming(int[] puzzle) {
		int counter = 0;
		for(int i = 0; i < 9;i++){
			if(puzzle[i]!=i){ // Then it's misplaced.
				counter++;
			}
		}
		return counter;
	}
	
	
	// Heuristic using Manhattan 
	// Sets local field variable h2 to the calculated heuristic value. 
	// Manhattan represents the sum of the distances of the tiles from their goal positions.
	private int Manhattan(int[] puzzle) {
		int counter = 0;
		
		for(int i = 0; i < puzzle.length; i++){
			
			//Not the blank space, and not the goal state. 
			if(puzzle[i]!=0 && puzzle[i]!= i) {  
				//System.out.println(i);
				int delta = Math.abs(i-puzzle[i]);
				counter += (delta % 3)+(delta/3); 
			}
		}	
		return counter;
	}

	public int getHeuristicValue(int heuristic) {
		if(heuristic == 1) // Then return Hamming Distance
			return h1;
		else return h2; // else Manhattan.
	}
}
