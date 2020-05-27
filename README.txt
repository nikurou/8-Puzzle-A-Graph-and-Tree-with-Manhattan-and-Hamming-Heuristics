
Author:       Dylan Chung
Program:      8 PUZZLE SOLVER
Requirements: JDK 8 or Above

-----------------------------------------------
How to Run with Command Line:
------------------------------------------------
1) Get to the directory the file is located 
	- cd (location)

2)Copy and paste the following commands
	- javac Puzzle.java Node.java Driver.java
	- java Driver

3) I already have compiled the program files and included the .class file for user convenience.


----------------------------------------------
Instructions on using the Program
----------------------------------------------
1) Follow the UI as it prompts you.
2) Valid Puzzle input format follows the format: 
	>1 2 3 4 5 6 7 8 0
	- The program will determine if the input is valid or grammatically correct on its own.

----------------------------------------------
 What this Folder Contains
----------------------------------------------
 
 - README.txt
 - Driver.class Driver.java
 - Node.class Node.java
 - Puzzle.class Puzzle.java
 - Solution1.txt, Solution2.txt, Solution3.txt (3 Sample Solutions with full output)
 - SolutionExtra.txt 
	(3 Extra Solutions that run both heuristics, but only display the path from root node to goal node.)

// I was not sure if the solutions wanted the sequence from root node to goal node only, so I have done both....
// Solution1-3 output shows all the nodes made during a search.
// SolutionExtra only shows the nodes that are related to the goal and root node. 


