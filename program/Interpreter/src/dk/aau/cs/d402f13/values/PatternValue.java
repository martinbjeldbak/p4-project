package dk.aau.cs.d402f13.values;

import dk.aau.cs.d402f13.utilities.ast.AstNode;
import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;

public class PatternValue extends Value {

  public PatternValue(AstNode children) {
    
  }

  @Override
  public Type getType() {
    return Type.PATTERN;
  }

}
