package lambda;

/**
 * The Expression interface represents a lambda expression.
 * 
 * @author Krish Arora
 */
public interface Expression {
    /**
     * @return a deep copy of the expression
     */
    Expression deepCopy();
}
