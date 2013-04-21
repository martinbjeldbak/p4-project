package dk.aau.cs.d402f13.values;

import dk.aau.cs.d402f13.utilities.ast.AstNode;
import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;

public class PatternValue extends Value {
  
  private static TypeValue type = new TypeValue("Pattern", 1, false);
  
  public TypeValue getType() {
    return type;
  }
  
  public static TypeValue type() {
    return type;
  }

  public PatternValue(AstNode children) {
    
  }
}
