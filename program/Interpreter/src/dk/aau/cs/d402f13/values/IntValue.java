package dk.aau.cs.d402f13.values;

import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;
import dk.aau.cs.d402f13.utilities.errors.TypeError;

public class IntValue extends Value {
  private final int value;

  public IntValue(String value) {
      this.value = Integer.parseInt(value);
  }
  
  public IntValue(int value) {
    this.value = value;
  }
  
  public int getValue() {
    return this.value;
  }
  
  @Override
  public BoolValue lessThan(Value other) throws TypeError {
    if(other instanceof IntValue) {
      if(this.value < ((IntValue)other).value)
        return BoolValue.trueValue();
      return BoolValue.falseValue();
    }
    throw new TypeError("Cannot use '<' on int with type " + other.getType());
  }
  
  @Override
  public BoolValue lessThanEq(Value other) throws TypeError {
    if(other instanceof IntValue) {
      if(this.value <= ((IntValue)other).value)
        return BoolValue.trueValue();
      return BoolValue.falseValue();
    }
    throw new TypeError("Cannot use '<=' on int with type " + other.getType());
  }
  
  @Override
  public BoolValue greaterThan(Value other) throws TypeError {
    if(other instanceof IntValue) {
      if(this.value > ((IntValue)other).value)
        return BoolValue.trueValue();
      return BoolValue.falseValue();
    }
    throw new TypeError("Cannot use '>' on int with type " + other.getType());
  }
  
  @Override
  public BoolValue greaterThanEq(Value other) throws TypeError {
    if(other instanceof IntValue) {
      if(this.value >= ((IntValue)other).value)
        return BoolValue.trueValue();
      return BoolValue.falseValue();
    }
    throw new TypeError("Cannot use '>=' on int with type "  + other.getType());
  }
  
  @Override
  public BoolValue equalsOp(Value other) {
    if(other instanceof IntValue) {
      if(this.value == ((IntValue)other).value)
        return BoolValue.trueValue();
    }
    return BoolValue.falseValue();
  }
  
  @Override
  public BoolValue notEqual(Value other) throws TypeError {
    if(other instanceof IntValue) {
      if(this.value != ((IntValue)other).value)
      return BoolValue.trueValue();
    return BoolValue.falseValue();
    }
    throw new TypeError("Cannot use '!=' with integer on type " + other.getType());
  }
  
  @Override
  public Value add(Value other) {
    return null;
  }
  
  @Override
  public String toString() {
    return "" + this.value;
  }

  @Override
  public Type getType() {
    return Type.INT_LIT;
  }
}
