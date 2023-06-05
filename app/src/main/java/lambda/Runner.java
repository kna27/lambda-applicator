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
        Expression reduced = reduceOnce(expression);
        while (reduced != null) {
            expression = reduced;
            reduced = reduceOnce(expression);
        }
        return expression;
    }

    /**
     * Reduces the leftmost redex in the given expression once, returning the
     * reduced expression
     *
     * @param expression the expression to reduce
     * @return the reduced expression, or null if no reduction is possible
     */
    private static Expression reduceOnce(Expression expression) {
        if (expression instanceof Application) {
            Application app = (Application) expression;
            if (app.left instanceof Function) {
                Function func = (Function) app.left;
                Variable var = func.variable;
                Expression body = func.expression;
                Expression right = app.right.deepCopy();
                return body.substitute(var, right);
            } else {
                Expression leftReduced = reduceOnce(app.left);
                if (leftReduced != null) {
                    app.left = leftReduced;
                    return app;
                }
                Expression rightReduced = reduceOnce(app.right);
                if (rightReduced != null) {
                    app.right = rightReduced;
                    return app;
                }
            }
        } else if (expression instanceof Function) {
            Function func = (Function) expression;
            Expression bodyReduced = reduceOnce(func.expression);
            if (bodyReduced != null) {
                func.expression = bodyReduced;
                return func;
            }
        }
        return null;
    }
}
