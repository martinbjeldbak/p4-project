package dk.aau.cs.d402f13.values;

import java.util.HashMap;

import dk.aau.cs.d402f13.interpreter.ConstantCallable;
import dk.aau.cs.d402f13.interpreter.Interpreter;
import dk.aau.cs.d402f13.interpreter.Member;
import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;
import dk.aau.cs.d402f13.utilities.errors.DivideByZeroError;
import dk.aau.cs.d402f13.utilities.errors.NameError;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.errors.TypeError;

public abstract class Value {

  /**
   * Returns the type of the Value. All sub-classes need
   * to overrride this.
   * @return                a TypeValue describing the
   *                        abstract type
   * @throws StandardError  if the value doesn't have
   *                        a type
   */
  public abstract TypeValue getType() throws StandardError;

  /**
   * Finds out if the current value is a subtype of the param type.
   * Returns true if it is, else false.
   * @param type the superclass type to check against
   * @return     true if the current type extends the param type
   */
  public boolean is(TypeValue type) throws StandardError {
    return getType().isSubtypeOf(type);
  }

  /**
   * Opposite of {@link #is(TypeValue)} method. Implemented for
   * readability.
   * @param type the superclass type to check against
   * @return     true if the current type is not related to the
   *             param type
   */
  public boolean isNot(TypeValue type) throws StandardError {
    return !is(type);
  }

  /**
   * Attempts to cast the current type to the param type.
   * @param type the value to be casted to
   * @return     the casted value
   * @throws TypeError if casting is not possible
   */
  public Value as(TypeValue type) throws StandardError {
    if (getType() == type) {
      return this;
    }
    throw new TypeError("Invalid cast to type " + type);
  }

  /**
   * Attempts to call the value with the given parameters
   * @param interpreter      an instance of Interpreter, so evaluation
   *                         of expressions is possible in the call
   * @param actualParameters the parameters of the call statement, can
   *                         have an arbitrary length
   * @return                 a new Value as a result of the call
   * @throws StandardError   if the value cannot be called
   */
  public Value call(Interpreter interpreter, Value ... actualParameters) throws StandardError {
    throw new TypeError("This value does not support being called");
  }
  
  /**
   * The 'less than' operator. Returns true or false. The Value argument must
   * also support the '<' operation.
   * @param other the RHS of the less than equality
   * @return      true if current object has a value less than other
   * @throws TypeError if the types do not support the operation
   */
  public BoolValue lessThan(Value other) throws StandardError {
    throw new TypeError("This value does not support the '<' operator");
  }
  
  /**
   * The 'less than or or equal to' operator. Returns true or false. The Value
   *  argument must also support the '<=' operation. 
   * @param other the RHS of the less than or equal to equality
   * @return      true if current object has a value less than or equal to other,
   *              else false
   * @throws TypeError if the argument does not support the operation
   */
  public BoolValue lessThanEq(Value other) throws StandardError {
    throw new TypeError("This value does not support the '<=' operator");
  }
  
  /**
   * The 'greater than' operator. Returns true or false. The Value argument
   *  must also support the '>' operation.
   * @param other  the RHS of the greater than equality
   * @return       true if current object has a value greater than other, else
   *               false
   * @throws TypeError if the argument does not support the operation
   */
  public BoolValue greaterThan(Value other) throws StandardError {
    throw new TypeError("This value does not support the '>' operator");
  }

  /**
   * The 'greater than or or equal to' operator. Returns true or false. The Value
   *  argument must also support the '>=' operation. 
   * @param other the RHS of the greater than or equal to equality
   * @return      true if current object has a value greater than or equal to other,
   *              else false
   * @throws TypeError if the argument does not support the operation
   */
  public BoolValue greaterThanEq(Value other) throws StandardError {
    throw new TypeError("This value does not support the '>=' operator");
  }

  /** {@inheritDoc} */
  public boolean equals(Object o) {
    if (!(o instanceof Value)) {
      return false;
    }
    if (this == o) {
      return true;
    }
    Value v = (Value)o;
    try {
      return v.equalsOp(this) == BoolValue.trueValue();
    }
    catch (StandardError e) {
      return false;
    }
  }
  
  /**
   * The equality comparison operator '=='.
   * @param other the value to be compared with
   * @return a true BoolValue if the values are equal
   */
  public BoolValue equalsOp(Value other) throws StandardError {
    if (this == other) {
      return BoolValue.trueValue();
    }
    return BoolValue.falseValue();
  }
  
  /**
   * The negated equality operator '!='.
   * @param other the value to be compared with
   * @return a true BoolValue if the values are not equal
   */
  public final BoolValue notEqual(Value other) throws StandardError {
    if(this.equalsOp(other) == BoolValue.falseValue())
      return BoolValue.trueValue();
    return BoolValue.falseValue();
  }
  
  /**
   * The addition operator. Returns a new Value.
   * @param other the RHS of the addition operator
   * @return      a new value as a result of the addition operation
   * @throws TypeError if the types don't match
   */
  public Value add(Value other) throws StandardError {
    throw new TypeError("This value does not support the '+' operator");
  }

  /**
   * The subtraction operator. Returns a new Value.
   * @param other the RHS of the subtraction operator
   * @return      a new value as a result of the subtraction operation
   * @throws TypeError if the types don't match
   */
  public Value subtract(Value other) throws StandardError {
    throw new TypeError("This value does not support subtraction");
  }
  
  /**
   * The multiplication operator. Returns a new Value.
   * @param other the RHS of the multiplication operator
   * @return      a new value as a result of the multiplication operation
   * @throws TypeError if the types don't match
   */
  public Value multiply(Value other) throws StandardError {
    throw new TypeError("This value does not support multiplication");
  }
  
  /**
   * The division operator. Returns a new Value.
   * @param other the RHS of the division operator
   * @return      a new value as a result of the division operation
   * @throws TypeError if the types don't match
   * @throws DivideByZeroError if dividing by zero happens at any point
   */
  public Value divide(Value other) throws StandardError {
    throw new TypeError("This value does not support division");
  }
  
  /**
   * The modulo '%' operator. Returns a new Value.
   * @param other the RHS of the modulo operator
   * @return      a new value as a result of the modulo operation
   * @throws TypeError if the types aren't compatible
   */
  public Value mod(Value other) throws StandardError {
    throw new TypeError("This value does not support modulo");
  }
  
  /**
   * Negates the value.
   * @return a new value as result of the negation
   * @throws TypeError
   */
  public Value negate() throws StandardError {
    throw new TypeError("This value does not support negation");
  }

  /**
   * Returns the desired member in the current object.
   * @param member         the unique identifier given
   *                       to the member
   * @return               the member, if it exists
   * @throws StandardError if the member cannot be found or
   *                       doesn't exist
   */
  public Value getMember(String member) throws StandardError {
    Value v = getType().getStaticMember(member);
    if (v != null) {
      return v;
    }
    throw new NameError("Undefined member: " + member);
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    try {
      return getType().getName() + "@" + hashCode();
    }
    catch (StandardError e) {
      return "unknown@" + hashCode();
    }
  }
}