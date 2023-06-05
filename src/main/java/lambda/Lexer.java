package lambda;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Lexer class converts an input into tokens that eventually need to be
 * interpreted.
 * 
 * @author Krish Arora
 */
public class Lexer {

	/**
	 * Tokenizes the input.
	 * 
	 * @param input the input to be tokenized
	 * @return an ArrayList of tokens
	 */
	public ArrayList<String> tokenize(String input) {
		ArrayList<String> tokens = new ArrayList<String>();
		// Remove comments and anything after them
		input = input.replaceAll(";.*", "");
		// Regex matching parentheses, backslashes, lambdas, periods, equals signs, and
		// character(s)
		String regex = "\\(|\\)|\\\\|\\λ|\\.|\\=|[^\\s\\(\\)\\\\;\\.\\=\\λ]+";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(input);
		// Add each token to the ArrayList
		while (m.find()) {
			// Add a backslash instead of a lambda
			if (m.group().equals("λ")) {
				tokens.add("\\");
			} else {
				tokens.add(m.group());
			}
		}
		return tokens;
	}
}
