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
     * @return a string representation of the function: (λvariable.expression)
     */
    public String toString() {
        return "(λ" + variable + "." + expression + ")";
    }
}
