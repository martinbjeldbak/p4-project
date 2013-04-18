package dk.aau.cs.d402f13.values;

import javax.swing.text.StyledEditorKit.BoldAction;

import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;
import dk.aau.cs.d402f13.utilities.errors.DivideByZeroError;
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
  
  /** {@inheritDoc}  */
  @Override
  public BoolValue lessThan(Value other) throws TypeError {
    if(other instanceof IntValue) {
      if(this.value < ((IntValue)other).value)
        return BoolValue.trueValue();
      return BoolValue.falseValue();
    }
    throw new TypeError("Cannot use '<' on int with " + other);
  }
  
  /** {@inheritDoc}  */
  @Override
  public BoolValue lessThanEq(Value other) throws TypeError {
    if(other instanceof IntValue) {
      if(this.value <= ((IntValue)other).value)
        return BoolValue.trueValue();
      return BoolValue.falseValue();
    }
    throw new TypeError("Cannot use '<=' on int with " + other);
  }
  
  /** {@inheritDoc}  */
  @Override
  public BoolValue greaterThan(Value other) throws TypeError {
    if(other instanceof IntValue) {
      if(this.value > ((IntValue)other).value)
        return BoolValue.trueValue();
      return BoolValue.falseValue();
    }
    throw new TypeError("Cannot use '>' on int with " + other);
  }
  
  /** {@inheritDoc}  */
  @Override
  public BoolValue greaterThanEq(Value other) throws TypeError {
    if(other instanceof IntValue) {
      if(this.value >= ((IntValue)other).value)
        return BoolValue.trueValue();
      return BoolValue.falseValue();
    }
    throw new TypeError("Cannot use '>=' on int with " + other);
  }
  
  /** {@inheritDoc}  */
  @Override
  public BoolValue equalsOp(Value other) {
    if(other instanceof IntValue) {
      if(this.value == ((IntValue)other).value)
        return BoolValue.trueValue();
    }
    return BoolValue.falseValue();
  }
  
  /** {@inheritDoc}  */
  @Override
  public Value add(Value other) throws TypeError {
    return new IntValue(this.value + ((IntValue)other).getValue());
  }
  
  /** {@inheritDoc}  */
  @Override
  public Value subtract(Value other) throws TypeError {
    return new IntValue(this.value - ((IntValue)other).getValue());
  }
  
  /** {@inheritDoc}  */
  @Override
  public Value multiply(Value other) throws TypeError {
    return new IntValue(this.value * ((IntValue)other).getValue());
  }
  
  /** {@inheritDoc}  */
  @Override
  public Value divide(Value other) throws TypeError, DivideByZeroError {
    IntValue rOperand = (IntValue)other;
    
    if(rOperand.getValue() == 0)
      throw new DivideByZeroError("Division by 0");
    
    return new IntValue(this.value / rOperand.getValue());
  }
  
  @Override
  /** {@inheritDoc}  */
  public Value negate() throws TypeError {
    return new IntValue(-this.value);
  }
  
  @Override
  public String toString() {
    return "" + this.value;
  }
}