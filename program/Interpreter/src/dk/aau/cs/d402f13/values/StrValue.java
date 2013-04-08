package dk.aau.cs.d402f13.values;

import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;

public class StrValue extends Value {
  private final String value;

  public StrValue(String value) {
    this.value = value;
  }
  
  public String getValue() {
    return this.value;
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
