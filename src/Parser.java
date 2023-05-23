import java.text.ParseException;
import java.util.ArrayList;
import java.util.Stack;

public class Parser {
	public ArrayList<String> addParensToLambdas(ArrayList<String> tokens) {
		ArrayList<String> newTokens = new ArrayList<>();
		Stack<Integer> lambdaStack = new Stack<>();

		for (String token : tokens) {
			if (token.equals("\\")) {
				lambdaStack.push(newTokens.size());
				newTokens.add(token);
			} else if (token.equals(")") && !lambdaStack.isEmpty()) {
				newTokens.add(token);
				int lambdaStartIndex = lambdaStack.pop();
				if (!(lambdaStartIndex > 0 && newTokens.get(lambdaStartIndex - 1).equals("("))) {
					newTokens.add(")");
					newTokens.add(lambdaStartIndex, "(");
				}
			} else {
				newTokens.add(token);
			}
		}

		while (!lambdaStack.isEmpty()) {
			newTokens.add(")");
			int lambdaStartIndex = lambdaStack.pop();
			newTokens.add(lambdaStartIndex, "(");
		}

		return newTokens;
	}

	public Expression parse(ArrayList<String> tokens) throws ParseException {
		return new Application(new Variable("x"), new Variable("y"));
	}

}
