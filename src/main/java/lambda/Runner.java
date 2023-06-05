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
        Expression reduced = reduceRedex(expression);
        while (reduced != null) {
            expression = reduced;
            reduced = reduceRedex(expression);
        }
        return expression;
    }

    /**
     * Reduces the leftmost redex in the given expression
     *
     * @param expression the expression to reduce
     * @return the reduced expression, or null if no reduction is possible
     */
    private static Expression reduceRedex(Expression expression) {
        if (expression instanceof Application) {
            Application app = (Application) expression;
            if (app.left instanceof Function) {
                Function func = (Function) app.left;
                Variable var = func.variable;
                Expression body = func.expression;
                Expression right = app.right.deepCopy();
                return body.substitute(var, right);
            } else {
                // Try reducing left and right expressions if possible
                Expression leftReduced = reduceRedex(app.left);
                if (leftReduced != null) {
                    app.left = leftReduced;
                    return app;
                }
                Expression rightReduced = reduceRedex(app.right);
                if (rightReduced != null) {
                    app.right = rightReduced;
                    return app;
                }
            }
        } else if (expression instanceof Function) {
            Function func = (Function) expression;
            Expression bodyReduced = reduceRedex(func.expression);
            if (bodyReduced != null) {
                func.expression = bodyReduced;
                return func;
            }
        }
        // Return null if no reduction is possible (already reduced or a Variable)
        return null;
    }
}
