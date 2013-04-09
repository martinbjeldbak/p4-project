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

}
