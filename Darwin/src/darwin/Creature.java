package darwin;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * This class represents one creature on the board. Each creature must remember
 * its species, position, direction, and the world in which it is living.
 * In addition, the Creature must remember the next instruction out of its
 * program to execute.
 * The creature is also responsible for making itself appear in the WorldMap. In
 * fact, you should only update the WorldMap from inside the Creature class.
 */
public class Creature {
	private Species species;
	private World world;
	private Position pos;
	private int dir;
	private int step = 1;

	/**
	 * Create a creature of the given species, with the indicated position and
	 * direction. Note that we also pass in the world-- remember this world, so
	 * that you can check what is in front of the creature and update the board
	 * when the creature moves.
	 */
	public Creature(Species species, World world, Position pos, int dir) {
		this.species = species;
		this.world = world;
		this.pos = pos;
		this.dir = dir;

		world.set(pos, this);

		WorldMap.displaySquare(pos, species.getSpeciesChar(), dir, species.getColor());

	}

	/**
	 * Return the species of the creature.
	 */
	public Species species() {
		return species;
	}

	/**
	 * Return the current direction of the creature.
	 */
	public int direction() {
		return dir;
	}

	/**
	 * Sets the current direction of the creature to the given value 
	 */
	public void setDirection(int dir){
		this.dir = dir;
	}

	/**
	 * Return the position of the creature.
	 */
	public Position position() {
		return pos;
	}

	public void wipe(Position p) {
		WorldMap.displaySquare(p, ' ', 0, "");
	}

	public void display() {
		WorldMap.displaySquare(pos, species.getSpeciesChar(), dir, species.getColor());
	}

	/**
	 * Execute steps from the Creature's program
	 *   starting at step #1
	 *   continue until a hop, left, right, or infect instruction is executed.
	 */
	public void takeOneTurn() {
		while (true) {
			Instruction inst = species.programStep(step);

			int opcode = inst.getOpcode();
			int address = inst.getAddress();

			Position front = pos.getAdjacent(dir);

			boolean inRange = world.inRange(front);

			if (opcode == Instruction.HOP) {

				if (inRange && world.get(front) == null) {

					Position old = pos;
					this.pos = front;
					display();
					world.set(pos, this);
					wipe(old);
					world.set(old, null);
				}

				step++;
				return;

			} else if (opcode == Instruction.LEFT) {

				setDirection(leftFrom(dir));
				display();

				step++;
				return;

			} else if (opcode == Instruction.RIGHT) {

				setDirection(rightFrom(dir));
				display();

				step++;
				return;

			} else if (opcode == Instruction.INFECT) {

				Creature fc = world.get(front);

				if (inRange && fc != null && !fc.species().getName().equals(species.getName())); {
					
					fc.species = species;
					fc.step = 1;
					display();

				}

				step++;
				return;

			} else if (opcode == Instruction.IFEMPTY) {

				if (inRange && world.get(front) == null) {
					step = address;
				
				} else { 
					step++;
				}

			} else if (opcode == Instruction.IFWALL) {

				if (!world.inRange(front)) {
					step = address;

				} else {
					step++;
				}

			} else if (opcode == Instruction.IFSAME) {

				if (world.inRange(front) && world.get(front) != null
					&& world.get(front).species().getName().equals(species())) {

					step = address;

					} else {
						step ++;
					}

			} else if (opcode == Instruction.IFENEMY) {

				if (world.inRange(front) && world.get(front) != null
						&& !world.get(front).species().getName().equals(species.getName())) {

						step = address;
					
					} else {
						step++;
					}

			} else if (opcode == Instruction.IFRANDOM) {

				if (Math.random() < .5) {

					step = address;

				} else {
					step++;
				}

			} else if (opcode == Instruction.GO) {

				step = address;

			} 
		}
	}

	/**
	 * Return the compass direction the is 90 degrees left of the one passed in.
	 */
	public static int leftFrom(int direction) {
		return (4 + direction - 1) % 4;
	}

	/**
	 * Return the compass direction the is 90 degrees right of the one passed
	 * in.
	 */
	public static int rightFrom(int direction) {
		return (direction + 1) % 4;
	}
	
public static void main(String[] args) {

	// generates new world with given dimensions
	World world = new World(5, 5);
	WorldMap.createWorldMap(5, 5);
	
	// tries to place one of our new creatures in a small testing world
	try {
		
		// sets creature instruction file
		BufferedReader in = new BufferedReader(new FileReader("./Creatures/Buster.txt"));
		
		// sets creature's species and initial position
		Species s = new Species(in);
		Position pos = new Position(2, 2);
		
		// places creature on grid
		Creature c = new Creature(s, world, pos, 0);
		
		// sets the amount of time in between turns
		while (true) {
			c.takeOneTurn();
			WorldMap.pause(500);
		}
		
	// catches error if the creature is incompatible
	} catch (Exception | Error e) {
		System.out.println("Creature compatibility test failed." + e);
		e.printStackTrace();
	}
}

}
