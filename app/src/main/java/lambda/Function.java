package lambda;

/**
 * The Function class represents a lambda function.
 * 
 * @author Krish Arora
 */
public class Function implements Expression {
    /**
     * The variable and expression of the function.
     */
    public Variable variable;
    public Expression expression;

    /**
     * Constructor for the Function class.
     * 
     * @param variable   the variable of the function
     * @param expression the expression of the function
     */
    public Function(Variable variable, Expression expression) {
        this.variable = variable;
        this.expression = expression;
    }

    /**
     * @return a deep copy of the function
     */
    @Override
    public Function deepCopy() {
        return new Function(variable.deepCopy(), expression.deepCopy());
    }

    /**
     * @return the expression with the variable substituted
     */
    @Override
    public Expression substitute(Variable v, Expression e) {
        // If this function's variable is the one we're substituting, don't substitute
        // in the function body, otherwise, substitute in the function body
        return this.variable.equals(v) ? this : new Function(this.variable, this.expression.substitute(v, e));
    }

    /**
     * @return a string representation of the function: (λvariable.expression)
     */
    public String toString() {
        return "(λ" + variable + "." + expression + ")";
    }
}
