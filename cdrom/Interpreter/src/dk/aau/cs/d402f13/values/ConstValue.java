package dk.aau.cs.d402f13.values;


import dk.aau.cs.d402f13.interpreter.Interpreter;
import dk.aau.cs.d402f13.interpreter.Scope;
import dk.aau.cs.d402f13.utilities.ast.AstNode;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.errors.TypeError;

public class ConstValue extends Value {
  private AstNode expression = null;
  private Interpreter interpreter = null;
  private Scope scope;

  @Override
  public TypeValue getType() {
    return null;
  }

  public ConstValue(AstNode expression, Interpreter interpreter) {
    this.expression = expression;
    this.interpreter = interpreter;
  }
  
  public ConstValue(AstNode expression, Interpreter interpreter, Scope scope) {
    this.expression = expression;
    this.interpreter = interpreter;
    this.scope = scope;
  }

  public Value evaluate() throws StandardError {
    if (scope != null) {
      interpreter.getSymbolTable().openScope(scope);
    }
    Value v = interpreter.visit(expression);
    if (scope != null) {
      interpreter.getSymbolTable().closeScope();
    }
    return v;
  }

  @Override
  public Value add(Value other) throws StandardError {
    if(other.is(ListValue.type()))
      return ListValue.prepend(this, other);
    throw new TypeError("Cannot add " + other + " to a type");
  }
}
