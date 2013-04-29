package dk.aau.cs.d402f13.interpreter;

import dk.aau.cs.d402f13.utilities.ast.AstNode;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.values.ConstMemberValue;
import dk.aau.cs.d402f13.values.ConstValue;
import dk.aau.cs.d402f13.values.FunValue;
import dk.aau.cs.d402f13.values.Value;

public class Member {
  private AstNode params;
  private AstNode expression;
  
  private ConstMemberValue constValue;
  
  public Member(AstNode definition) {
    expression = definition.getLast();

    if(definition.size() == 3) {
      params = definition.get(1);
    }
  }
  
  public Member(ConstMemberValue constValue) {
    this.constValue = constValue;
  }
  
  public Value getValue(Interpreter interpreter) throws StandardError {
    if (constValue != null) {
      return constValue.evaluate(interpreter, interpreter.getSymbolTable().getThis());
    }
    if (params == null) {
      return new ConstValue(expression, interpreter, interpreter.getSymbolTable().currentScope());
    }
    return new FunValue(params, expression, interpreter.getSymbolTable().currentScope());
  }
}