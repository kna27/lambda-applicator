package lambda;

/**
 * The Runner class runs Applications and handles reduction of lambda
 * expressions
 * 
 * @author Krish Arora
 */
public class Runner {
    /**
     * Runs the given expression, reducing it until it cannot be reduced any
     * further.
     * 
     * @param expression the expression to run
     * @return the reduced expression
     */
    public static Expression run(Expression expression) {
        while (true) {
            Expression reduced = reduceOnce(expression);
            if (reduced == null) {
                break; // No more reductions are possible
            } else {
                expression = reduced;
            }
        }
        return expression;
    }

    /**
     * Finds the leftmost redex in the given expression
     * 
     * @param expression
     * @return the leftmost redex in the given expression, or null if none found
     */
    private static Expression findRedex(Expression expression) {
        if (expression instanceof Application) {
            Application app = (Application) expression;
            if (app.left instanceof Function) {
                return app; // This is a redex
            } else {
                // Recurse into the left and right expressions
                Expression leftRedex = findRedex(app.left);
                if (leftRedex != null) {
                    return leftRedex;
                }
                return findRedex(app.right);
            }
        } else if (expression instanceof Function) {
            // Recurse into the function body
            return findRedex(((Function) expression).expression);
        } else {
            // Variables cannot be redexes
            return null;
        }
    }

    /**
     * Reduces the given expression once, returning the reduced expression
     * 
     * @param expression the expression to reduce
     * @return the reduced expression, or null if no reduction is possible
     */
    private static Expression reduceOnce(Expression expression) {
        Expression redex = findRedex(expression);
        if (redex != null) {
            Function func = (Function) ((Application) redex).left;
            return func.expression.substitute(func.variable, ((Application) redex).right);
        } else {
            return null;
        }
    }
}
