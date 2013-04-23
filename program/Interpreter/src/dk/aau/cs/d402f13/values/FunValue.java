package dk.aau.cs.d402f13.values;


import dk.aau.cs.d402f13.interpreter.Callable;
import dk.aau.cs.d402f13.interpreter.Interpreter;
import dk.aau.cs.d402f13.interpreter.Scope;
import dk.aau.cs.d402f13.interpreter.SymbolTable;
import dk.aau.cs.d402f13.utilities.ast.AstNode;
import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;
import dk.aau.cs.d402f13.utilities.errors.ArgumentError;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.errors.TypeError;

public class FunValue extends Value {
  private String[] formalParameters;
  private String varParams = null;
  private AstNode expression = null;
  private Callable callable = null;
  private Scope currentScope = null;
  
  private static TypeValue type = new TypeValue("Function", 1, false);
  
  public TypeValue getType() {
    return type;
  }
  
  public static TypeValue type() {
    return type;
  }

  public FunValue(AstNode params, AstNode expression) {
    this.expression = expression;
    if (params.size() > 0 && params.get(params.size() - 1).type == Type.VARS) {
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

  @Override
  public Value add(Value other) throws TypeError {
    if(other.is(ListValue.type())) {
      ListValue.prepend(this, other);
    }
    throw new TypeError("Cannot add " + other + " to a function");
  }

  @Override
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
  public String toString() {
    String s = "Function[";
    if (formalParameters.length > 0) {
      if (formalParameters[0] == null) {
        s += "$arg";
      }
      else {
        s += "$" + formalParameters[0];
      }
    }
    for (int i = 1; i < formalParameters.length; i++) {
      if (formalParameters[i] == null) {
        s += ", $arg";
      }
      else {
        s += ", $" + formalParameters[i];
      }
    }
    if (varParams != null) {
      if (formalParameters.length > 0) {
        s += ", ";
      }
      s += "... $";
      if (varParams.equals("")) {
        s += "args";
      }
      else {
        s += varParams;
      }
    }
    return s + "]";
  }
}
