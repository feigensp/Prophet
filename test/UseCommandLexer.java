package test;

/** Test class to try out the command lexer. */
public class UseCommandLexer {
	public static void main(String args[]) throws Exception {
		CommandLexer command_lexer = new CommandLexer(System.in);
		Yytoken token = null;
		do {
			token = command_lexer.yylex();
			System.out.println("token = " + token);
		} while (token != null);
	}
}