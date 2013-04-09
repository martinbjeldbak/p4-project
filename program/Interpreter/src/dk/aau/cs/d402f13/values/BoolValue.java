package dk.aau.cs.d402f13.values;

import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;

public class BoolValue extends Value {
  
  
  private static BoolValue trueValue = new BoolValue();
  private static BoolValue falseValue = new BoolValue();

  private BoolValue() {
    // TODO Auto-generated constructor stub
  }
  
  public static BoolValue trueValue() {
    return trueValue;
  }
  
  public static BoolValue falseValue() {
    return falseValue;
  }

  @Override
  public Type getType() {
    // TODO Auto-generated method stub
    return null;
  }

}
