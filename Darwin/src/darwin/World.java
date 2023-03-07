package darwin;

/**
 * This class includes the functions necessary to keep track of the creatures in
 * a two-dimensional world. 
 */

public class World {
	private Matrix<Creature> creatures;
	private int width;
	private int height;
	
	/**
	 * This function creates a new world consisting of width columns and height
	 * rows, each of which is numbered beginning at 0. A newly created world
	 * contains no objects.
	 */
	public World(int w, int h) {
		creatures = new Matrix<Creature>(w, h);
		width = w;
		height = h;
	}

	/**
	 * Returns the height of the world.
	 */
	public int height() {
		return height;
	}

	/**
	 * Returns the width of the world.
	 */
	public int width() {
		return width;
	}

	/**
	 * Returns whether pos is in the world or not.
	 * 
	 * returns true *if* pos is an (x,y) location within the bounds of the board.
	 */
	public boolean inRange(Position pos) {
		return pos.getX() < width && pos.getY() < height && pos.getX() >= 0 && pos.getY() >= 0;
	}

	/**
	 * Set a position on the board to contain e.
	 * 
	 * @throws IllegalArgumentException if pos is not in range
	 */
	public void set(Position pos, Creature e) {
		//Sets creature to the position specified in parameter
		if (inRange(pos)) {
			creatures.set(pos.getX(), pos.getY(), e);
		} else {
			throw new IllegalArgumentException("Position not in range");
		}
	}

	/**
	 * Return the contents of a position on the board.
	 * 
	 * @throws IllegalArgumentException if pos is not in range
	 */
	public Creature get(Position pos) {
		if (inRange(pos)) {
			return creatures.get(pos.getX(), pos.getY());
		} else {
			throw new IllegalArgumentException("Position not in range");
		}
	}

}
