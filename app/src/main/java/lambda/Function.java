package lambda;

import java.util.Set;

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
    public Expression substitute(Variable oldVar, Expression newExpr) {
        if (this.variable.equals(oldVar)) {
            return this;
        } else {
            Function newFunction = this.deepCopy();
            if (this.hasConflict(oldVar, newExpr)) {
                String newName;
                do {
                    newName = this.variable.name + "1"; // Create a new variable name
                } while (newExpr.freeVariables().contains(newName));
                newFunction.variable.name = newName;
                newFunction.expression = newFunction.expression.substitute(this.variable, new Variable(newName));
            }
            newFunction.expression = newFunction.expression.substitute(oldVar, newExpr);
            return newFunction;
        }
    }

    /**
     * Finds if there is a conflict between the old variable and the new expression.
     * 
     * @param oldVar  the old variable
     * @param newExpr the new expression
     * @return true if there is a conflict, false otherwise
     */
    public boolean hasConflict(Variable oldVar, Expression newExpr) {
        if (!variable.equals(oldVar) && newExpr.freeVariables().contains(variable.name)) {
            return true;
        }
        return false;
    }

    /**
     * @return a string representation of the function: (λvariable.expression)
     */
    public String toString() {
        return "(λ" + variable + "." + expression + ")";
    }

    /**
     * @return the free variables in the expression
     */
    @Override
    public Set<String> freeVariables() {
        Set<String> freeVariables = expression.freeVariables();
        freeVariables.remove(variable.name); // Remove the function parameter
        return freeVariables;
    }

    /**
     * @return if the expression is an alpha equivalent of the function
     */
    public boolean clashesWith(Set<String> names) {
        return names.contains(variable.name);
    }

    /**
     * Alpha converts the function to a new name.
     * 
     * @param newName the new name of the variable
     * @return the function with the variable renamed
     */
    public Function alphaConvert(String newName) {
        Function newFunction = this.deepCopy();
        newFunction.variable.name = newName;
        newFunction.expression.substitute(this.variable, new Variable(newName));
        return newFunction;
    }
}
