package dk.aau.cs.d402f13.values;

import dk.aau.cs.d402f13.utilities.ast.AstNode;
import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.errors.TypeError;

public class PatternValue extends Value {
  private Value[] values = null;
  private static TypeValue type = new TypeValue("Pattern", 1, false);



  @Override
  public TypeValue getType() {
    return type;
  }
  
  public static TypeValue type() {
    return type;
  }

  public PatternValue(Value ... patterns) {
    this.values = patterns;
  }

  @Override
  public Value add(Value other) throws StandardError {
    if(other.is(ListValue.type()))
      return ListValue.prepend(this, other);
    throw new TypeError("Cannot add " + other + " to a pattern");
  }

  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer("(");

    for(int i = 0; i < values.length - 1; i++) {
      sb.append(values[i] + " ");
    }
    sb.append(values[values.length-1] + ")");

    return sb.toString();
  }
}
