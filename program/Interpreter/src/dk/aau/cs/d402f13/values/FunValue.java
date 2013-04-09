package dk.aau.cs.d402f13.values;


import dk.aau.cs.d402f13.interpreter.Callable;
import dk.aau.cs.d402f13.interpreter.Interpreter;
import dk.aau.cs.d402f13.interpreter.Scope;
import dk.aau.cs.d402f13.interpreter.SymbolTable;
import dk.aau.cs.d402f13.utilities.ast.AstNode;
import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;
import dk.aau.cs.d402f13.utilities.errors.ArgumentError;
import dk.aau.cs.d402f13.utilities.errors.StandardError;

public class FunValue extends Value {
  private String[] formalParameters;
  private String varParams = null;
  private AstNode expression = null;
  private Callable callable = null;
  private Scope currentScope = null;

  public FunValue(AstNode params, AstNode expression) {
    this.expression = expression;
    if (params.get(params.size() - 1).type == Type.VARS) {
      this.formalParameters = new String[params.size() - 1];
      varParams = params.get(params.size() - 1).value;
    }
    else {
      this.formalParameters = new String[params.size()];
    }
    for(int i = 0; i < this.formalParameters.length; i++) {
      this.formalParameters[i] = params.get(i).value;
    }
  }
  
  public FunValue(int minArity, boolean varArgs, Callable callable) {
    this.callable = callable;
    formalParameters = new String[minArity];
    if (varArgs) {
      varParams = "";
    }
  }
  
  public FunValue(AstNode params, AstNode expression, Scope currentScope) {
    this(params, expression);
    
    this.currentScope = currentScope;
  }
  
  public Value call(Interpreter interpreter, Value ... actualParameters) throws StandardError {
    if (varParams == null) {
      if (actualParameters.length != formalParameters.length) {
        throw new ArgumentError("Invalid number of arguments, expected " + formalParameters.length);
      }
    }
    else {
      if (actualParameters.length < formalParameters.length) {
        throw new ArgumentError("Invalid number of arguments, expected at least " + formalParameters.length);
      }
    }
    interpreter.getSymbolTable().openScope(new Scope(currentScope));
    Value ret;
    if (callable != null) {
      ret = callable.call(interpreter, actualParameters);
    }
    else {
      for (int i = 0; i < formalParameters.length; i++) {
        interpreter.getSymbolTable().addVariable(formalParameters[i], actualParameters[i]);
      }
      if (varParams != null) {
        int numParamsList = actualParameters.length - formalParameters.length;
        Value[] varParamsList = new Value[numParamsList];
        for (int i = 0; i < varParamsList.length; i++) {
          varParamsList[i] = actualParameters[i + formalParameters.length];
        }
        interpreter.getSymbolTable().addVariable(varParams, new ListValue(varParamsList));
      }
      ret = interpreter.visit(expression);
    }
    interpreter.getSymbolTable().closeScope();
    return ret;
  }

  @Override
  public Type getType() {
    return Type.FUNCTION;
  }

}
