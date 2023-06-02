package lambda;

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
     * @return a string representation of the application: (left right)
     */
    public String toString() {
        return "(" + left + " " + right + ")";
    }
}
