package lambda;

import java.util.HashSet;
import java.util.Set;

/**
 * The Application class represents the application of an expression to
 * another expression.
 * 
 * @author Krish Arora
 */
public class Application implements Expression {
    /**
     * The left and right expressions of the application.
     */
    public Expression left;
    public Expression right;

    /**
     * Constructor for the Application class.
     * 
     * @param left  the left expression of the application
     * @param right the right expression of the application
     */
    public Application(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * @return a deep copy of the application
     */
    @Override
    public Application deepCopy() {
        return new Application(left.deepCopy(), right.deepCopy());
    }

    /**
     * @return the free variables in the expression
     */
    @Override
    public Set<String> freeVariables() {
        Set<String> freeVariables = new HashSet<>();
        freeVariables.addAll(left.freeVariables());
        freeVariables.addAll(right.freeVariables());
        return freeVariables;
    }

    /**
     * @return the expression with the variable substituted
     */
    @Override
    public Expression substitute(Variable v, Expression e) {
        // Substitute in both the left and the right expressions
        return new Application(this.left.substitute(v, e), this.right.substitute(v, e));
    }

    /**
     * @return a string representation of the application: (left right)
     */
    public String toString() {
        return "(" + left + " " + right + ")";
    }
}
