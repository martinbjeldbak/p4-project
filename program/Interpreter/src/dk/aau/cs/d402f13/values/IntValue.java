package dk.aau.cs.d402f13.values;

import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;

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
  public String toString() {
    return "" + this.value;
  }

  @Override
  public Type getType() {
    return Type.INT_LIT;
  }
}
