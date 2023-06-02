package lambda;

/**
 * The Variable class represents a variable in a lambda expression.
 * 
 * @author Krish Arora
 */
public class Variable implements Expression {
	/**
	 * The name of the variable.
	 */
	private String name;

	/**
	 * Constructor for the Variable class.
	 * 
	 * @param name the name of the variable
	 */
	public Variable(String name) {
		this.name = name;
	}

	/**
	 * @return a deep copy of the variable
	 */
	@Override
	public Variable deepCopy() {
		return new Variable(name);
	}

	/**
	 * @return whether the two variable's names are the same
	 */
	public boolean equals(Variable other) {
		return name.equals(other.name);
	}

	/**
	 * @return a string representation of the variable: name
	 */
	public String toString() {
		return name;
	}
}
