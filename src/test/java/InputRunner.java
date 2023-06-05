import java.util.ArrayList;

import lambda.Console;
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
			// Tokenize the input
			ArrayList<String> tokens = lexer.tokenize(input);
			// If the input is empty, go to the next input
			if (tokens.isEmpty()) {
				return "";
			}
			// If the input is an assignment, set the variable to the first token
			String variable = tokens.size() > 2 && tokens.get(1).equals("=") ? tokens.get(0) : null;
			boolean isPopulating = false; // If the input is a populate command
			String output = ""; // Initialize the output string to be empty

			// Checking if it is a populate command
			if (tokens.size() == 3 && tokens.get(0).equals("populate")) {
				// If the 2nd and 3rd tokens are parse-able integers
				int start = Console.parseStrToInt(tokens.get(1));
				int end = Console.parseStrToInt(tokens.get(2));
				if (start < end && start >= 0) { // Ensure start is less than end
					isPopulating = true;
					for (int i = start; i <= end; i++) {
						if (i == 0) {
							// Special case if 0
							tokens = lexer.tokenize("0 = (λf.(λx.x))");
						} else { // Otherwise generate the strings to tokenize and run
							String application = "";
							for (int j = 1; j < i; j++) {
								application += "(f ";
							}
							String assignNumber = i + " = (λf.(λx.(f" + application + "x)))";
							for (int j = 1; j < i; j++) {
								assignNumber += ")";
							}
							tokens = lexer.tokenize(assignNumber);
							parser.tokens = tokens;
							try {
								parser.preParse();
								parser.parse();
							} catch (Exception e) {
								System.out.println("Unparsable expression, input was: \"" + input + "\"");
							}
						}
					}
					output = "Populated numbers " + start + " to " + end;
				}
			}
			if (!isPopulating) {
				// Try to parse the input
				try {
					// Set the tokens of the parser to the tokens of the lexer, pre-parse the input,
					// then parse it
					parser.tokens = tokens;
					parser.preParse();
					Expression exp = parser.parse();
					// If the input is an assignment
					if (variable != null) {
						// If the expression is null, the variable is already defined
						if (exp == null) {
							output = tokens.get(0) + " is already defined.";
						} else {
							// Otherwise, add the expression to the definitions
							output = "Added " + exp.toString() + " as " + variable;
						}
					} else {
						// Otherwise, just set the output to the returned expression
						output = exp.toString();
					}
				} catch (Exception e) {
					// If the input is unparsable, print an error message
					System.out.println("Unparsable expression, input was: \"" + input + "\"");
					return "";
				}
			}
			return output;
		}
		return "Goodbye!";
	}
}
