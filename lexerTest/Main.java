package lexerTest;

/*
 Commented By: Christopher Lopes
 File Name: Main.java
 To Create: After the scanner, lcalc.flex, and the parser, ycalc.cup, have been
 created.
 > javac Main.java
 To Run: > java Main test.txt
 where test.txt is an test input file for the calculator.
 */

/* Import classes needed.  The class we created for the parser, the standard
 runtime class for java, and an io class.*/
import java.io.FileReader;
import java.io.IOException;

class Main {

	static boolean do_debug_parse = false;

	static public void main(String argv[]) {

		/* Start the parser */
		FileReader fr = null;
		try {
			fr = new FileReader("src\\lexerTest\\test.txt");

		} catch (Exception e) {
			System.out.println("Fehler");
		} finally {
		}
		parser p = new parser(new Lexer(fr));
		try {
			Object result = p.parse().value;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}