package dk.aau.cs.d402f13.values;

import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;
import dk.aau.cs.d402f13.utilities.errors.TypeError;

public class BoolValue extends Value {
  
  private static BoolValue trueValue = new BoolValue();
  private static BoolValue falseValue = new BoolValue();

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
  
  @Override
  public String toString() {
    if(this == trueValue)
      return "true";
    return "false";
  }
  
  @Override
  public BoolValue equalsOp(Value other) {
    if(other instanceof BoolValue) {
      if(this == (BoolValue)other)
        return trueValue;
    }
    return falseValue;
  }
}
