package dk.aau.cs.d402f13.interpreter;

import dk.aau.cs.d402f13.utilities.ast.AstNode;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.values.ConstValue;
import dk.aau.cs.d402f13.values.FunValue;
import dk.aau.cs.d402f13.values.ObjectValue;
import dk.aau.cs.d402f13.values.Value;

public class Member {
  private AstNode params;
  private AstNode expression;
  
  private ConstantCallable constant;
  
  private int minArity;
  private boolean varArgs;
  private Callable function;
  
  private Member member;
  private Scope scope;
  
  public Member(AstNode definition) {
    expression = definition.getLast();

    if(definition.size() == 3) {
      params = definition.get(1);
    }
  }
  
  public Member(ConstantCallable constant) {
    this.constant = constant;
  }

  public Member(int minArity, boolean varArgs, Callable function) {
    this.minArity = minArity;
    this.varArgs = varArgs;
    this.function = function;
  }
  
  public Member(Member member, Scope scope) {
    this.member = member;
    this.scope = scope;
  }
  
  public Value getValue(Interpreter interpreter, Scope scope) throws StandardError {
    if (member != null) {
      return member.getValue(interpreter, this.scope);
    }
    if (constant != null) {
      interpreter.getSymbolTable().openScope(scope);
      Value ret = constant.call(interpreter, scope.getThis());
      interpreter.getSymbolTable().closeScope();
      return ret;
    }
    if (function != null) {
      return new FunValue(minArity, varArgs, function, scope);
    }
    if (params == null) {
      interpreter.getSymbolTable().openScope(scope);
      Value ret = interpreter.visit(expression);
      interpreter.getSymbolTable().closeScope();
      return ret;
    }
    return new FunValue(params, expression, scope);
  }
  
  public Value getValue(Interpreter interpreter) throws StandardError {
    if (member != null) {
      return member.getValue(interpreter, this.scope);
    }
    return getValue(interpreter, interpreter.getSymbolTable().currentScope());
  }
  
  public Value getValue(Interpreter interpreter, Value value) throws StandardError {
    if (member != null) {
      return member.getValue(interpreter, this.scope);
    }
    if (value instanceof ObjectValue) {
      return getValue(interpreter, ((ObjectValue)value).getScope());
    }
    return getValue(interpreter, new Scope(value));
  }
}