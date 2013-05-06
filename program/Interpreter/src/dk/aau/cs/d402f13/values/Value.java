package dk.aau.cs.d402f13.values;

import dk.aau.cs.d402f13.interpreter.Interpreter;
import dk.aau.cs.d402f13.interpreter.Member;
import dk.aau.cs.d402f13.utilities.errors.*;
import dk.aau.cs.d402f13.utilities.errors.InternalError;


  /**
   * Returns the type of the Value. All sub-classes need
   * to overrride this.
   * @return                a TypeValue describing the
   *                        abstract type
   * @throws StandardError  if the value doesn't have
   *                        a type
   */
public abstract class Value implements Cloneable {
  

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
    Member m = getType().getTypeMember(member);
    if (m != null) {
      return new MemberValue(m);
    }
    throw new NameError("Undefined member: " + member);
  }
  
  public Value getMember(String name, TypeValue type) throws StandardError {
    Value v = getMember(name);
    if (!v.is(type)) {
      throw new TypeError("Invalid type " + v.getType().getName()
          + " for member " + name + " in object of type "
          + this.getType().getName() + ", expected " + type.getName());
    }
    return v;
  }
  
  public Value getMemberAs(String name, TypeValue type) throws StandardError {
    Value v = getMember(name, type);
    return v.as(type);
  }
  
  public String getMemberString(String name) throws StandardError {
    return ((StrValue)getMemberAs(name, StrValue.type())).getValue();
  }
  
  public int getMemberInt(String name) throws StandardError {
    return ((IntValue)getMemberAs(name, IntValue.type())).getValue();
  }
  
  public boolean getMemberBoolean(String name) throws StandardError {
    return (BoolValue)getMemberAs(name, IntValue.type()) == BoolValue.trueValue();
  }
  
  public CoordValue getMemberCoord(String name) throws StandardError {
    return (CoordValue)getMemberAs(name, ListValue.type());
  }
  
  public Value[] getMemberList(String name) throws StandardError {
    return ((ListValue)getMemberAs(name, ListValue.type())).getValues();
  }
  
  public Value[] getMemberList(String name, int minLength) throws StandardError {
    Value[] list = getMemberList(name);
    if (list.length < minLength) {
      throw new TypeError("Invalid length of list in member "
        + name + " in object of type " + this.getType().getName()
        + ", expected at least " + minLength);
    }
    return list;
  }
  
  public Value[] getMemberList(String name, TypeValue type) throws StandardError {
    Value[] list = getMemberList(name);
    for (Value v : list) {
      if (!v.is(type)) {
        throw new TypeError("Invalid type " + v.getType().getName()
          + " for value of list in member " + name
          + " in object of type " + this.getType().getName()
          + ", expected " + type.getName());
      } 
    }
    return list;
  }
  
  public Value[] getMemberList(String name, TypeValue type, int minLength) throws StandardError {
    Value[] list = getMemberList(name, minLength);
    for (Value v : list) {
      if (!v.is(type)) {
        throw new TypeError("Invalid type " + v.getType().getName()
          + " for value of list in member " + name
          + " in object of type " + this.getType().getName()
          + ", expected " + type.getName());
      } 
    }
    return list;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    try {
      if (getType() == null) {
        return "unknown:" + getClass().getSimpleName() + "@" + hashCode();
      }
      return getType().getName() + "@" + hashCode();
    }
    catch (StandardError e) {
      return "unknown:" + getClass().getSimpleName() + "@" + hashCode();
    }
  }
  
  public Value getClone() throws InternalError {
    try {
      return (Value)clone();
    }
    catch (CloneNotSupportedException e) {
      throw new InternalError(e);
    }
  }
}