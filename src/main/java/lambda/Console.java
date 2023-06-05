package lambda;

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
			// Initialize the output string to be empty
			String output = "";

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

			// Print the output
			System.out.println(output);

			// Get the next input
			input = cleanConsoleInput();
		}
		// Exit the REPL
		System.out.println("Goodbye!");
	}

	/**
	 * This method was provided with the starter code
	 * Collects user input, and ...
	 * ... does a bit of raw string processing to (1) strip away comments,
	 * (2) remove the BOM character that appears in unicode strings in Windows,
	 * (3) turn all weird whitespace characters into spaces,
	 * and (4) replace all λs with backslashes.
	 * 
	 * @return the cleaned input
	 */
	private static String cleanConsoleInput() {
		System.out.print("> ");
		String raw = in.nextLine();
		String deBOMified = raw.replaceAll("\uFEFF", ""); // remove Byte Order Marker from UTF

		String clean = removeWeirdWhitespace(deBOMified);

		return clean.replaceAll("λ", "\\\\");
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
}
