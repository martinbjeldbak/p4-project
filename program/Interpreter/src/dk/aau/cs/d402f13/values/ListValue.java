package dk.aau.cs.d402f13.values;

import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;

public class ListValue extends Value {

  private Value[] values = null;
  
  public ListValue(Value ... values) {
    this.values = values;
  }
  
  public Value[] getValues() {
    return values;
  }

  @Override
  public Type getType() {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public BoolValue equalsOp(Value other) {
    if (!(other instanceof ListValue)) {
      return BoolValue.falseValue();
    }
    Value[] otherValues = ((ListValue)other).getValues();
    if (otherValues.length != values.length) {
      return BoolValue.falseValue();
    }
    for (int i = 0; i < values.length; i++) {
      if (otherValues[i].equalsOp(values[i]) != BoolValue.trueValue()) {
        return BoolValue.falseValue();
      }
    }
    return BoolValue.trueValue();
  }
  
  @Override
  public String toString() {
    String s = "[";
    if (values.length > 0) {
      s += values[0].toString();
    }
    for (int i = 1; i < values.length; i++) {
      s += ", " + values[i].toString();
    }
    return s + "]";
  }

}
