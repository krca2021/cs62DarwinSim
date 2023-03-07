package darwin;

import java.util.ArrayList;
import java.util.Random;
import java.io.*;

/**
 * This class controls the simulation. The design is entirely up to you. You
 * should include a main method that takes the array of species file names
 * passed in and populates a world with species of each type. You class should
 * be able to support anywhere between 1 to 4 species.
 * 
 * Be sure to call the WorldMap.pause() method every time through the main
 * simulation loop or else the simulation will be too fast. For example:
 * 
 * 
 * public void simulate() { 
 * 	for (int rounds = 0; rounds < numRounds; rounds++) {
 * 		giveEachCreatureOneTurn(); 
 * 		WorldMap.pause(500); 
 * 	} 
 * }
 * 
 */
class Darwin {
	final private int numRounds = 10000;
	String[] speciesList;
	ArrayList<Creature> creatures = new ArrayList<Creature>();
	
	

	public Darwin(String[] speciesFilenames) {
		speciesList = speciesFilenames;
	}

	/**
	 * The array passed into main will include the arguments that appeared on the
	 * command line. For example, running "java Darwin Hop.txt Rover.txt" will call
	 * the main method with s being an array of two strings: "Hop.txt" and
	 * "Rover.txt".
	 * 
	 * The autograder will always call the full path to the creature files, for
	 * example "java Darwin /home/user/Desktop/Assignment02/Creatures/Hop.txt" So
	 * please keep all your creates in the Creatures in the supplied
	 * Darwin/Creatures folder.
	 *
	 * To run your code you can either: supply command line arguments through
	 * Eclipse ("Run Configurations -> Arguments") or by creating a temporary array
	 * with the filenames and passing it to the Darwin constructor. If you choose
	 * the latter options, make sure to change the code back to: Darwin d = new
	 * Darwin(s); before submitting. If you want to use relative filenames for the
	 * creatures they should be of the form "./Creatures/Hop.txt".
	 */
	public static void main(String s[]) {
		Darwin d = new Darwin(s);
		d.simulate();
	}
	
	public void giveEachCreatureOneTurn() {
		for(int i = 0; i<creatures.size();i++) {
			creatures.get(i).takeOneTurn();
		}
	}

	public void simulate() {
		
		// sets number of columns and rows for world generation
		int rows = 20;
		int cols = 20;
		
		// generates world grid according to given parameters
		World world = new World(rows,cols);
		WorldMap.createWorldMap(rows,cols);
		
		// constructs creatures with given type and amount
		for(String species : speciesList) {
			
			/*constructs one creature per pass of the for loop.
			places creatures randomly on grid*/
			for(int i = 0; i<20; i++) {
				try {
					BufferedReader in = new BufferedReader(new FileReader("./Creatures/" + species));
					Species s = new Species(in);
					Random rand = new Random();
					int x = rand.nextInt(rows);
					int y = rand.nextInt(cols);
					Position pos = new Position(x,y);
					
					/* checks if spot is occupied. If so, generates new random
				 	location until it finds a location that is null (empty)*/
					while(world.get(pos)!=null) {
						
						x = rand.nextInt(rows);
						y = rand.nextInt(cols);
						pos = new Position(x,y);
					}
					
					// assigns creature random starting direction
					int dir = rand.nextInt(4);
					Creature c = new Creature(s,world,pos,dir);
					
					// adds completed creature to creatures
					creatures.add(c);
					
					// catches error if creature is incompatible	
				} catch (Exception | Error e) {
					System.out.println("Creature compatibility test failed." + e);
					e.printStackTrace();
				}
			}
		}
		
		for(int i = 0; i<numRounds;i++) {
			  	giveEachCreatureOneTurn(); 
			  	WorldMap.pause(10); 
			  } 
		
		
	}
}
