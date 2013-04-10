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
