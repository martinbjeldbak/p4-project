package dk.aau.cs.d402f13.values;

import javax.swing.text.StyledEditorKit.BoldAction;

import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;
import dk.aau.cs.d402f13.utilities.errors.DivideByZeroError;
import dk.aau.cs.d402f13.utilities.errors.TypeError;

public class IntValue extends Value {
  private final int value;
  
  private static TypeValue type = new TypeValue("Integer", 1, false);
  
  public TypeValue getType() {
    return type;
  }
  
  public static TypeValue type() {
    return type;
  }

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
    if(other instanceof IntValue)
      return new IntValue(this.value + ((IntValue)other).getValue());
    else if(other instanceof StrValue)
      return new StrValue(value + ((StrValue)other).getValue());
    throw new TypeError("Addition cannot be done on ints with " + other);
  }
  
  /** {@inheritDoc}  */
  @Override
  public Value subtract(Value other) throws TypeError {
    if(other instanceof IntValue)
      return new IntValue(this.value - ((IntValue)other).getValue());
    throw new TypeError("Subtraction cannot be done on ints with " + other);   
  }
  
  /** {@inheritDoc}  */
  @Override
  public Value multiply(Value other) throws TypeError {
    if(other instanceof IntValue)
      return new IntValue(this.value * ((IntValue)other).getValue());
    throw new TypeError("Multiplication cannot be done on ints with " + other);
  }
  
  /** {@inheritDoc}  */
  @Override
  public Value divide(Value other) throws TypeError, DivideByZeroError {
    if(other instanceof IntValue) {
      IntValue rOperand = (IntValue)other;
      
      if(rOperand.getValue() == 0)
        throw new DivideByZeroError("Division by 0");
      
      return new IntValue(this.value / rOperand.getValue());
    }
    throw new TypeError("Division cannot be done on ints with " + other);
  }
  
  /** {@inheritDoc}  */
  @Override
  public Value mod(Value other) throws TypeError {
    if(other instanceof IntValue)
      return new IntValue(value % ((IntValue)other).getValue());
    throw new TypeError("Modular arithmetic cannot be done on ints with " + other);
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