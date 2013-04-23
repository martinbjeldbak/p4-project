package dk.aau.cs.d402f13.values;

import javax.swing.text.StyledEditorKit.BoldAction;

import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;
import dk.aau.cs.d402f13.utilities.errors.DivideByZeroError;
import dk.aau.cs.d402f13.utilities.errors.TypeError;

public class IntValue extends Value {
  private final int value;
  
  private static TypeValue type = new TypeValue("Integer", 1, false);

  @Override
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
    if(other.is(IntValue.type())) {
      IntValue oInt = (IntValue)other.as(IntValue.type());

      if(this.value < oInt.value)
        return BoolValue.trueValue();
      return BoolValue.falseValue();
    }
    throw new TypeError("Cannot use '<' on integer with " + other);
  }
  
  /** {@inheritDoc}  */
  @Override
  public BoolValue lessThanEq(Value other) throws TypeError {
    if(other.is(IntValue.type())) {
      IntValue oInt = (IntValue)other.as(IntValue.type());

      if(this.value <= oInt.value)
        return BoolValue.trueValue();
      return BoolValue.falseValue();
    }
    throw new TypeError("Cannot use '<=' on int with " + other);
  }
  
  /** {@inheritDoc}  */
  @Override
  public BoolValue greaterThan(Value other) throws TypeError {
    if(other.is(IntValue.type())) {
      IntValue oInt = (IntValue)other.as(IntValue.type());

      if(this.value > oInt.value)
        return BoolValue.trueValue();
      return BoolValue.falseValue();
    }
    throw new TypeError("Cannot use '>' on int with " + other);
  }
  
  /** {@inheritDoc}  */
  @Override
  public BoolValue greaterThanEq(Value other) throws TypeError {
    if(other.is(IntValue.type())) {
      IntValue oInt = (IntValue)other.as(IntValue.type());

      if(this.value >= oInt.value)
        return BoolValue.trueValue();
      return BoolValue.falseValue();
    }
    throw new TypeError("Cannot use '>=' on int with " + other);
  }
  
  /** {@inheritDoc}  */
  @Override
  public BoolValue equalsOp(Value other) {
    if(other.is(IntValue.type())) {
      IntValue oInt = null;
      try {
        oInt = (IntValue)other.as(IntValue.type());
      } catch (TypeError typeError) {
        return BoolValue.falseValue();
      }
      if(this.value == oInt.value)
        return BoolValue.trueValue();
    }
    return BoolValue.falseValue();
  }
  
  /** {@inheritDoc}  */
  @Override
  public Value add(Value other) throws TypeError {
    if(other.is(IntValue.type())) {
      IntValue oInt = (IntValue)other.as(IntValue.type());
      return new IntValue(this.value + oInt.value);
    }
    else if(other.is(StrValue.type())) {
      StrValue oStr = (StrValue)other.as(StrValue.type());
      return new StrValue(value + oStr.getValue());
    }
    else if(other.is(ListValue.type()))
      return ListValue.prepend(this, other);
    throw new TypeError("Addition cannot be done on integers with " + other);
  }
  
  /** {@inheritDoc}  */
  @Override
  public Value subtract(Value other) throws TypeError {
    if(other.is(IntValue.type())) {
      IntValue oInt = (IntValue)other.as(IntValue.type());
      return new IntValue(this.value - oInt.getValue());
    }
    throw new TypeError("Subtraction cannot be done on integers with " + other);
  }
  
  /** {@inheritDoc}  */
  @Override
  public Value multiply(Value other) throws TypeError {
    if(other.is(IntValue.type())) {
      IntValue oInt = (IntValue)other.as(IntValue.type());
      return new IntValue(this.value * oInt.getValue());
    }
    throw new TypeError("Multiplication cannot be done on integers with " + other);
  }
  
  /** {@inheritDoc}  */
  @Override
  public Value divide(Value other) throws TypeError, DivideByZeroError {
    if(other.is(IntValue.type())) {
      IntValue oInt = (IntValue)other.as(IntValue.type());
      if(oInt.getValue() == 0)
        throw new DivideByZeroError("Cannot divide by 0");
      return new IntValue(this.value / oInt.getValue());
    }
    throw new TypeError("Division cannot be done on integers with " + other);
  }
  
  /** {@inheritDoc}  */
  @Override
  public Value mod(Value other) throws TypeError {
    if(other.is(IntValue.type())) {
      IntValue oInt = (IntValue)other.as(IntValue.type());
      return new IntValue(value % oInt.getValue());
    }
    throw new TypeError("Modular arithmetic cannot be done on integers with " + other);
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