package dk.aau.cs.d402f13.values;

import java.util.HashMap;

import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.errors.TypeError;

import static dk.aau.cs.d402f13.values.TypeValue.expect;

public class BoolValue extends Value {
  
  private static BoolValue trueValue = new BoolValue();
  private static BoolValue falseValue = new BoolValue();
  
  private static TypeValue type = new TypeValue("Boolean", 1, false);

  @Override
  public TypeValue getType() {
    return type;
  }
  
  public static TypeValue type() {
    return type;
  }

  private BoolValue() {
  }
  
  public static BoolValue trueValue() {
    return trueValue;
  }
  
  public static BoolValue falseValue() {
    return falseValue;
  }
  
  public BoolValue not() {
    if (this == trueValue)
      return falseValue;
    return trueValue;
  }
  
  public BoolValue and(BoolValue other) {
    if (this == trueValue && other == trueValue)
      return trueValue;
    return falseValue;
  }
  
  public BoolValue or(BoolValue other) {
    if (this == falseValue && other == falseValue)
      return falseValue;
    return trueValue;
  }
  
  /** {@inheritDoc}  */
  @Override
  public Value add(Value other) throws StandardError {
    if(other.is(StrValue.type())) {
      StrValue str = (StrValue)other.as(StrValue.type());
      return new StrValue(this.toString() + str.getValue());
    }
    throw new TypeError("Addition cannot be done on boolean with " + other);
  }
  
  @Override
  public String toString() {
    if(this == trueValue)
      return "true";
    return "false";
  }
  
  /** {@inheritDoc}  */
  @Override
  public BoolValue equalsOp(Value other) throws StandardError {
    if(other.is(BoolValue.type())) {
      BoolValue b = null;
      try {
        b = (BoolValue)other.as(BoolValue.type());
      } catch (TypeError typeError) {
        return falseValue;
      }
      if(this == b)
        return  trueValue;
    }
    return falseValue;
  }
}
