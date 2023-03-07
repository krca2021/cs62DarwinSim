package darwin;

import java.io.*;
import java.util.ArrayList;

/**
 * The individual creatures in the world are all representatives of some
 * species class and share certain common characteristics, such as the species
 * name and the program they execute. Rather than copy this information into
 * each creature, this data can be recorded once as part of the description for
 * a species and then each creature can simply include the appropriate species
 * reference as part of its internal data structure.
 * 
 * Note: The instruction addresses start at one, not zero.
 */
public class Species {
	private String name;
	private String color;
	private char speciesChar; // the first character of Species name
	private ArrayList<Instruction> program;
	private int opcode;

	/**
	 * Create a species for the given fileReader. 
	 */
	public Species(BufferedReader fileReader) {
		try {
			ArrayList<String> species = new ArrayList<String>();
			String line = fileReader.readLine();

			// Loops through the file, adding each line to ArrayList species
			while (!line.equals("")) {
				species.add(line);
				line = fileReader.readLine();
			}

			// initializes class variables based on species file provided
			speciesChar = species.get(0).charAt(0);
			name = species.get(0);
			color = species.get(1);
			program = new ArrayList<Instruction>();

			//loops through instructions in species ArrayList, assigning opcodes
			for(int i = 2; i< species.size();i++) {
				String instruct = species.get(i);
				// Checks for a space in the instruction
				// if no space then instruction with no address
				if (instruct.indexOf(" ") == -1) {
					if (instruct.equals("hop")) {
						opcode = Instruction.HOP;
					} else if (instruct.equals("left")) {
						opcode = Instruction.LEFT;
					} else if (instruct.equals("right")) {
						opcode = Instruction.RIGHT;
					} else if (instruct.equals("infect")) {
						opcode = Instruction.INFECT;
					}

					//Adds instruction to program
					Instruction inst = new Instruction(opcode, 0);
					program.add(inst);
				} else {
					String[] com = species.get(i).split(" ");
					if (com[0].equals("ifempty")) {
						opcode = Instruction.IFEMPTY;
					} else if (com[0].equals("ifrandom")) {
						opcode = Instruction.IFRANDOM;
					} else if (com[0].equals("ifwall")) {
						opcode = Instruction.IFWALL;
					} else if (com[0].equals("ifsame")) {
						opcode = Instruction.IFSAME;
					} else if (com[0].equals("ifenemy")) {
						opcode = Instruction.IFENEMY;
					} else if (com[0].equals("go")) {
						opcode = Instruction.GO;
					}

					Instruction inst = new Instruction(opcode, Integer.parseInt(com[1]));
					program.add(inst);
				}
			}

		} catch (IOException e) {
			System.out.println("Could not read file '" + fileReader + "'");
			System.exit(1);
		}
	}


	/**
	* Return the char for the species
	*/
	public char getSpeciesChar() {
		return speciesChar;
	}

	/**
	 * Return the name of the species.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Return the color of the species.
	 */
	public String getColor() {
		return color;
	}

	/**
	 * Return the number of instructions in the program.
	 */
	public int programSize() {
		return program.size();
	}

	/**
	 * Return an instruction from the program.
	 * @pre 1 <= i <= programSize().
	 * @post returns instruction i of the program.
	 */
	public Instruction programStep(int i) {
		return program.get(i-1);
	}

	/**
	 * Return a String representation of the program.
	 * 
	 * do not change
	 */
	public String programToString() {
		String s = "";
		for (int i = 1; i <= programSize(); i++) {
			s = s + (i) + ": " + programStep(i) + "\n";
		}
		return s;
	}

}
