package dk.aau.cs.d402f13.values;

import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;
import dk.aau.cs.d402f13.utilities.errors.TypeError;

public class StrValue extends Value {
  private final String value;

  public StrValue(String value) {
    this.value = value;
  }
  
  public String getValue() {
    return this.value;
  }
  
  @Override
  public BoolValue equalsOp(Value other) {
    if(other instanceof StrValue) {
      if(this.value.equals(((StrValue) other).value))
        return BoolValue.trueValue();
    }
    return BoolValue.falseValue();
  }
  
  @Override
  public BoolValue notEqual(Value other) throws TypeError {
    if(other instanceof StrValue) {
      if(!this.value.equals(((StrValue) other).value))
        return BoolValue.trueValue();
    return BoolValue.falseValue();
    }
    throw new TypeError("Cannot use '!=' on string with type " + other.getType());
  }
  
  @Override
  public String toString() {
    return "" + this.value;
  }

  @Override
  public Type getType() {
    return Type.STRING_LIT;
  }

}
