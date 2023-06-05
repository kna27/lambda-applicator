package lambda;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Console class is the main class of the program, and runs user input
 * through the lexer and parser.
 * 
 * @author Krish Arora
 */
public class Console {
	/**
	 * The Scanner object that reads user input.
	 */
	private static Scanner in;

	public static void main(String[] args) {
		in = new Scanner(System.in);

		// The lexer and parser are created here, and reused for each input.
		Lexer lexer = new Lexer();
		Parser parser = new Parser(new ArrayList<String>());
		String input = cleanConsoleInput();

		// Exit the REPL when the user types "exit"
		while (!input.equalsIgnoreCase("exit")) {
			// Tokenize the input
			ArrayList<String> tokens = lexer.tokenize(input);
			// If the input is empty, go to the next input
			if (tokens.isEmpty()) {
				input = cleanConsoleInput();
				continue;
			}
			// If the input is an assignment, set the variable to the first token
			String variable = tokens.size() > 2 && tokens.get(1).equals("=") ? tokens.get(0) : null;
			boolean isPopulating = false; // If the input is a populate command
			String output = ""; // Initialize the output string to be empty

			// Checking if it is a populate command
			if (tokens.size() == 3 && tokens.get(0).equals("populate")) {
				// If the 2nd and 3rd tokens are parse-able integers
				int start = parseStrToInt(tokens.get(1));
				int end = parseStrToInt(tokens.get(2));
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
							} catch (ParseException e) {
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
					input = cleanConsoleInput();
					continue;
				}
			}

			// Print the output
			System.out.println(output);

			// Get the next input
			input = cleanConsoleInput();
		}
		// Exit the REPL
		System.out.println("Goodbye!");
	}

	/**
	 * Collects user input and removes BOM character that appears in unicode strings
	 * in Windows, removes comments and anything following them, turned blank
	 * whitespace characters into spaces, and replaces all λs with backslashes
	 * 
	 * @return the cleaned input
	 */
	private static String cleanConsoleInput() {
		System.out.print("> ");
		String input = in.nextLine();
		input = input.replaceAll("\uFEFF", ""); // remove Byte Order Marker
		input = input.replaceAll(";.*", ""); // remove comments
		input = removeWeirdWhitespace(input);
		return input.replaceAll("λ", "\\\\"); // replace lambda symbol with a backslash
	}

	/**
	 * This method was provided with the starter code
	 * 
	 * @param input the string to remove whitespace characters from
	 * @return the string with all whitespace characters removed
	 */
	public static String removeWeirdWhitespace(String input) {
		String whitespace_chars = "" // dummy empty string for homogeneity
				+ "\\u0009" // CHARACTER TABULATION
				+ "\\u000A" // LINE FEED (LF)
				+ "\\u000B" // LINE TABULATION
				+ "\\u000C" // FORM FEED (FF)
				+ "\\u000D" // CARRIAGE RETURN (CR)
				+ "\\u0020" // SPACE
				+ "\\u0085" // NEXT LINE (NEL)
				+ "\\u00A0" // NO-BREAK SPACE
				+ "\\u1680" // OGHAM SPACE MARK
				+ "\\u180E" // MONGOLIAN VOWEL SEPARATOR
				+ "\\u2000" // EN QUAD
				+ "\\u2001" // EM QUAD
				+ "\\u2002" // EN SPACE
				+ "\\u2003" // EM SPACE
				+ "\\u2004" // THREE-PER-EM SPACE
				+ "\\u2005" // FOUR-PER-EM SPACE
				+ "\\u2006" // SIX-PER-EM SPACE
				+ "\\u2007" // FIGURE SPACE
				+ "\\u2008" // PUNCTUATION SPACE
				+ "\\u2009" // THIN SPACE
				+ "\\u200A" // HAIR SPACE
				+ "\\u2028" // LINE SEPARATOR
				+ "\\u2029" // PARAGRAPH SEPARATOR
				+ "\\u202F" // NARROW NO-BREAK SPACE
				+ "\\u205F" // MEDIUM MATHEMATICAL SPACE
				+ "\\u3000"; // IDEOGRAPHIC SPACE
		Pattern whitespace = Pattern.compile(whitespace_chars);
		Matcher matcher = whitespace.matcher(input);
		String result = input;
		if (matcher.find()) {
			result = matcher.replaceAll(" ");
		}
		return result;
	}

	/**
	 * Parses a String to an integer
	 * 
	 * @param str the String to parse
	 * @return the parsed integer, -1 if not parsable
	 */
	public static int parseStrToInt(String str) {
		if (str.matches("\\d+")) {
			return Integer.parseInt(str);
		} else {
			return -1;
		}
	}
}
