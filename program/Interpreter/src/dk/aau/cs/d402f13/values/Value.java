package dk.aau.cs.d402f13.values;

import dk.aau.cs.d402f13.interpreter.Interpreter;
import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;
import dk.aau.cs.d402f13.utilities.errors.DivideByZeroError;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.errors.TypeError;

public abstract class Value {
  
  public abstract TypeValue getType();
  
  public boolean is(TypeValue type) {
    return getType().isSubtypeOf(type);
  }
  
  public Value as(TypeValue type) {
    return this;
  }
  
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
  public BoolValue lessThan(Value other) throws TypeError {
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
  public BoolValue lessThanEq(Value other) throws TypeError {
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
  public BoolValue greaterThan(Value other) throws TypeError {
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
  public BoolValue greaterThanEq(Value other) throws TypeError {
    throw new TypeError("This value does not support the '>=' operator");
  }
  
  public boolean equals(Object o) {
    if (!(o instanceof Value)) {
      return false;
    }
    if (this == o) {
      return true;
    }
    Value v = (Value)o;
    return v.equalsOp(this) == BoolValue.trueValue();
  }
  
  /**
   * The equality comparison operator '=='.
   * @param other the value to be compared with
   * @return a true BoolValue if the values are equal
   */
  public BoolValue equalsOp(Value other) {
    return BoolValue.falseValue();
  }
  
  /**
   * The negated equality operator '!='.
   * @param other the value to be compared with
   * @return a true BoolValue if the values are not equal
   */
  public final BoolValue notEqual(Value other) {
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
  public Value add(Value other) throws TypeError {
    throw new TypeError("This value does not support the '+' operator");
  }

  /**
   * The subtraction operator. Returns a new Value.
   * @param other the RHS of the subtraction operator
   * @return      a new value as a result of the subtraction operation
   * @throws TypeError if the types don't match
   */
  public Value subtract(Value other) throws TypeError {
    throw new TypeError("This value does not support subtraction");
  }
  
  /**
   * The multiplication operator. Returns a new Value.
   * @param other the RHS of the multiplication operator
   * @return      a new value as a result of the multiplication operation
   * @throws TypeError if the types don't match
   */
  public Value multiply(Value other) throws TypeError {
    throw new TypeError("This value does not support multiplication");
  }
  
  /**
   * The division operator. Returns a new Value.
   * @param other the RHS of the division operator
   * @return      a new value as a result of the division operation
   * @throws TypeError if the types don't match
   * @throws DivideByZeroError if dividing by zero happens at any point
   */
  public Value divide(Value other) throws TypeError, DivideByZeroError {
    throw new TypeError("This value does not support division");
  }
  
  /**
   * The modulo '%' operator. Returns a new Value.
   * @param other the RHS of the modulo operator
   * @return      a new value as a result of the modulo operation
   * @throws TypeError if the types aren't compatible
   */
  public Value mod(Value other) throws TypeError {
    throw new TypeError("This value does not support modulo");
  }
  
  /**
   * Negates the value.
   * @return a new value as result of the negation
   * @throws TypeError
   */
  public Value negate() throws TypeError {
    throw new TypeError("This value does not support negation");
  }
}