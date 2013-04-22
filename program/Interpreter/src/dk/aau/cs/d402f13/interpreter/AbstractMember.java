package dk.aau.cs.d402f13.interpreter;

import dk.aau.cs.d402f13.utilities.ast.AstNode;
import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.values.FunValue;
import dk.aau.cs.d402f13.values.Value;

public class AbstractMember {
  private boolean function = false;
  private int minArity = 0;
  private boolean varParams = false;

  public AbstractMember() {
  }
  
  public AbstractMember(int minArity, boolean varParams) {
    this.minArity = minArity;
    this.varParams = varParams;
  }
  
  public AbstractMember(AstNode definition) {
    if(definition.size() == 2) {
      function = true;
      if (definition.get(1).size() > 0 && definition.get(1).getLast().type == Type.VARS) {
        varParams = true;
        minArity = definition.get(1).size() - 1;
      }
      else {
        minArity = definition.get(1).size();
      }
    }
  }
  
  public boolean isConstant() {
    return !function;
  }
  
  public boolean isFunction() {
    return function;
  }
  
  public int getArity() {
    return minArity;
  }
  
  public boolean acceptsVarParams() {
    return varParams;
  }
}