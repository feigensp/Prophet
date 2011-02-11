package test;

import javax.swing.JTextPane;

public class Bla {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int x = 1;
		int z = x++ + x;
		System.out.println("z = " + z);
		// Resultat 3
		x = 1;
		z = x + (x++);
		System.out.println("z = " + z);
		// Resultat 2
	}

}
