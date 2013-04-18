package dk.aau.cs.d402f13.interpreter;

import dk.aau.cs.d402f13.utilities.ast.AstNode;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.values.FunValue;
import dk.aau.cs.d402f13.values.Value;

public class Member {
  private AstNode params;
  private AstNode expression;
  
  
  public Member(AstNode definition) {
    expression = definition.getLast();

    if(definition.size() == 3) {
      params = definition.get(1);
    }
  }
  
  public Value getValue(Interpreter interpreter) throws StandardError {
    if (params == null) {
      return interpreter.visit(expression);
    }
    return new FunValue(params, expression, interpreter.getSymbolTable().currentScope());
  }
}