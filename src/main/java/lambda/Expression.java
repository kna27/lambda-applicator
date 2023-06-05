package lambda;

import java.util.Set;

/**
 * The Expression interface represents a lambda expression.
 * 
 * @author Krish Arora
 */
public interface Expression {
    /**
     * @return a deep copy of the expression
     */
    public Expression deepCopy();

    /**
     * Substitutes a variable with an expression in the expression.
     * 
     * @param v the variable to substitute
     * @param e the expression to substitute the variable with
     * @return the expression with the variable substituted
     */
    public Expression substitute(Variable v, Expression e);

    /**
     * @return the free variables in the expression as a set
     */

    /**
     * @return the free variables in the Expression
     */
    public Set<String> freeVariables();
}
