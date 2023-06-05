import java.util.ArrayList;

import lambda.Expression;
import lambda.Lexer;
import lambda.Parser;

/**
 * Simulates entering input into the Console.
 * 
 * @author Krish Arora
 */
public class InputRunner {
	Lexer lexer = new Lexer();
	Parser parser = new Parser(new ArrayList<String>());

	public String run(String input) {
		while (!input.equalsIgnoreCase("exit")) {
			ArrayList<String> tokens = lexer.tokenize(input);
			if (tokens.isEmpty()) {
				return "";
			}

			boolean isAssignment = tokens.size() > 2 && tokens.get(1).equals("=");
			String variable = isAssignment ? tokens.get(0) : null;
			String output = "";

			try {
				parser.tokens = tokens;
				parser.preParse();
				Expression exp = parser.parse();
				if (isAssignment) {
					if (exp == null) {
						output = tokens.get(0) + " is already defined.";
					} else {
						output = "Added " + exp.toString() + " as " + variable;
					}
				} else {
					output = exp.toString();
				}
			} catch (Exception e) {
				System.out.println("Unparsable expression, input was: \"" + input + "\"");
				return "";
			}

			return output;
		}
		return "Goodbye!";
	}
}
