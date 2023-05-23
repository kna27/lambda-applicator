import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

	/*
	 * A lexer (or "tokenizer") converts an input into tokens that
	 * eventually need to be interpreted.
	 * 
	 * Given the input
	 * (\bat .bat flies)cat λg.joy! )
	 * you should output the ArrayList of strings
	 * [(, \, bat, ., bat, flies, ), cat, \, g, ., joy!, )]
	 * Ignore anything after a ; on a line.
	 *
	 */
	public ArrayList<String> tokenize(String input) {
		ArrayList<String> tokens = new ArrayList<String>();
		input = input.replaceAll(";.*", "");
		String regex = "\\(|\\)|\\\\|\\λ|\\.|\\=|\\w+|!";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(input);
		while (m.find()) {
			if (m.group().equals("λ")) {
				tokens.add("\\");
			} else {
				tokens.add(m.group());
			}
		}
		System.out.println(tokens);
		return tokens;
	}

}
