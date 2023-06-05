package lambda;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * The Parser class converts tokens from the parser into an expression tree that
 * can be evaluated.
 * 
 * @author Krish Arora
 */
public class Parser {
    // The tokens to be parsed from the lexer
    public ArrayList<String> tokens;
    // Variables and their corresponding expression definitions
    public Hashtable<String, Expression> definitions = new Hashtable<String, Expression>();

    /**
     * Constructor for the Parser class.
     * 
     * @param tokens the tokens to be parsed
     */
    public Parser(ArrayList<String> tokens) {
        this.tokens = tokens;
    }

    /**
     * Surrounds an expression with a pair of parentheses
     * 
     * @param start the index of the first token of the expression to wrap
     * @param end   the index of the last token of the expression to wrap
     */
    private void wrapExpressionWithParens(int start, int end) {
        tokens.add(start, "(");
        tokens.add(++end, ")");
    }

    /**
     * Adds parentheses around a lambda function if it does not already have
     * parentheses around it.
     * 
     * @param lambdaPos the position of the lambda function in the tokens array
     * @throws ParseException if there is an unexpected closing parenthesis that
     *                        does not match an opening one
     */
    private void addParensToLambda(int lambdaPos) throws ParseException {
        int start, i;
        start = i = lambdaPos;
        int openParens = 0;

        while (i < tokens.size()) {
            if (tokens.get(i).equals("(")) {
                openParens++;
            } else if (tokens.get(i).equals(")")) {
                openParens--;
                if (openParens < -1) {
                    throw new ParseException("Unexpected closing parenthesis", i);
                }
                // Wrap lambda expression with parentheses
                else if (openParens == -1) {
                    wrapExpressionWithParens(start, i);
                    return;
                }
            }
            i++;
        }

        if (openParens == 0) {
            wrapExpressionWithParens(start, i);
        } else if (openParens > 0) {
            throw new ParseException("Unclosed opening parenthesis", i);
        } else {
            throw new ParseException("Unexpected closing parenthesis", i);
        }
    }

    /**
     * Pre-parses the input, adding parentheses around lambda functions if they do
     * not already have parentheses around them.
     * 
     * @throws ParseException if there is an unexpected closing parenthesis that
     *                        does not match an opening one
     */
    public void preParse() throws ParseException {
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).equals("\\")) {
                // if it is the first token or the token before it is not an opening
                if (i == 0 || !tokens.get(i - 1).equals("(")) {
                    addParensToLambda(i);
                }
            }
        }
    }

    /**
     * Parses an application from a range of tokens.
     * 
     * @param expressionStart the index of the first token of the application
     * @param length          the length of the application to parse
     * @return the parsed application
     */
    private Expression parseApplication(int expressionStart, int length) {
        int openParens, rightExpressionIndex, fullLength;
        openParens = 0;
        rightExpressionIndex = fullLength = length;
        boolean lengthWasChanged = false;

        // find the right expression of the application
        for (int i = fullLength; i > expressionStart - 1; i--) {
            if (tokens.get(i).equals(")")) {
                if (openParens == 0) {
                    lengthWasChanged = true;
                    length = i;
                }
                openParens++;
            } else if (tokens.get(i).equals("(")) {
                openParens--;
                if (openParens == 0) {
                    rightExpressionIndex = i;
                    break;
                }
            } else if (openParens == 0) {
                rightExpressionIndex = i;
                break;
            }
        }

        // If the application was one expression
        if (rightExpressionIndex == expressionStart) {
            if (length == fullLength && lengthWasChanged) {
                return parseExpression(++expressionStart, length);
            }
        }

        // If the right expression is a variable
        if (rightExpressionIndex == length) {
            return new Application(parseExpression(expressionStart, rightExpressionIndex),
                    this.definitions.getOrDefault(tokens.get(length), new Variable(tokens.get(length))));
        }

        return new Application(parseExpression(expressionStart, rightExpressionIndex),
                parseExpression(++rightExpressionIndex, length));
    }

    /**
     * Parses an expression from a range of tokens.
     * 
     * @param expressionStart the index of the first token of the expression
     * @param length          the length of the expression to parse
     * @return the parsed expression
     */
    private Expression parseExpression(int expressionStart, int length) {
        length--;
        String firstToken = tokens.get(expressionStart);

        // If it is a function
        if (firstToken.equals("\\")) {
            return new Function(new Variable(tokens.get(expressionStart + 1)),
                    parseExpression(expressionStart + 3, ++length));
        }

        // If it is a variable
        if (expressionStart == length) {
            return this.definitions.getOrDefault(firstToken, new Variable(firstToken));
        }

        return parseApplication(expressionStart, length); // Otherwise it is an application
    }

    /**
     * Parses the tokens into an expression tree.
     * 
     * @return the expression tree
     * @throws ParseException if there is an unexpected closing parenthesis that
     *                        does not match an opening one
     */
    public Expression parse() throws ParseException {
        // Return null if there are no tokens
        if (tokens.size() == 0) {
            return null;
        }
        // If it is an assignment
        if (tokens.size() > 2 && tokens.get(1).equals("=")) {
            String variableName = tokens.get(0);
            // Return null if the variable is already defined
            if (this.definitions.containsKey(variableName)) {
                return null;
            } else {
                // Remove the variable name and the equals sign
                tokens.remove(0);
                tokens.remove(0);
                Expression expression;
                // If it is a run command
                if (tokens.get(0).equals("run")) {
                    tokens.remove(0); // Remove the run keyword
                    expression = parseExpression(0, tokens.size());
                    // Run the expression
                    expression = Runner.run(expression);
                    if (getKeyFromValue(expression) != null) {
                        return new Variable(getKeyFromValue(expression));
                    }
                } else {
                    expression = parseExpression(0, tokens.size());
                }
                // Add the variable and its definition to the definitions Hashtable
                this.definitions.put(variableName, expression);
                return expression;
            }
        }
        preParse();
        Expression expression;
        // If it is a run command
        if (tokens.get(0).equals("run")) {
            tokens.remove(0);
            expression = Runner.run(parseExpression(0, tokens.size()));
            if (getKeyFromValue(expression) != null) {
                return new Variable(getKeyFromValue(expression));
            }
        } else {
            expression = parseExpression(0, tokens.size());
        }
        return expression;
    }

    /**
     * Gets the key from a value in the definitions Hashtable.
     * 
     * @param value the value to get the key from
     * @return the key corresponding to the value
     */
    private String getKeyFromValue(Expression value) {
        String key = null;
        Iterator<String> itr = this.definitions.keySet().iterator();
        String currentKey = null;

        while (itr.hasNext()) {
            currentKey = itr.next();
            if (this.definitions.get(currentKey).toString().equals(value.toString())) {
                return currentKey;
            }
        }
        return key;
    }
}
