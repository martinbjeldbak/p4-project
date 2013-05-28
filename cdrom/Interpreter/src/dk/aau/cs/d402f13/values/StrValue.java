package dk.aau.cs.d402f13.values;

import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.errors.TypeError;

public class StrValue extends Value {
  private final String value;
  
  private static TypeValue type = new TypeValue("String", 1, false);

  @Override
  public TypeValue getType() {
    return type;
  }
  
  public static TypeValue type() {
    return type;
  }

  public StrValue(String value) {
    this.value = value;
  }
  
  public String getValue() {
    return this.value;
  }
  
  /** {@inheritDoc}  */
  @Override
  public BoolValue equalsOp(Value other) throws StandardError {
    if(other.is(StrValue.type())) {
      try {
        StrValue oStr = (StrValue)other.as(StrValue.type());

        if(this.value.equals(oStr.value))
          return BoolValue.trueValue();
      } catch (TypeError typeError) {
        return BoolValue.falseValue();
      }
    }
    return BoolValue.falseValue();
  }
  
  /** {@inheritDoc}  */
  @Override
  public Value add(Value other) throws StandardError {
    if(other.is(StrValue.type()))
      return new StrValue(this.value + other.as(StrValue.type()).toString());
    else if(other.is(IntValue.type()))
      return new StrValue(this.value + other.as(IntValue.type()).toString());
    else if(other.is(BoolValue.type()))
      return new StrValue(this.value + other.as(BoolValue.type()).toString());
    else if(other.is(CoordValue.type()))
      return new StrValue(this.value + other.as(CoordValue.type()).toString());
    else if(other.is(DirValue.type()))
      return new StrValue(this.value + other.as(DirValue.type()).toString());
    else if(other.is(ListValue.type()))
      return ListValue.prepend(this, other);
    throw new TypeError("Cannot concatenate a " + other + " to a string");
  }
  
  @Override
  public String toString() {
    return "" + this.value;
  }
}
