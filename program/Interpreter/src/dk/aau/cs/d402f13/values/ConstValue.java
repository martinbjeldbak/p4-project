package dk.aau.cs.d402f13.values;


import dk.aau.cs.d402f13.interpreter.Callable;
import dk.aau.cs.d402f13.interpreter.ConstantCallable;
import dk.aau.cs.d402f13.interpreter.Interpreter;
import dk.aau.cs.d402f13.interpreter.Scope;
import dk.aau.cs.d402f13.interpreter.SymbolTable;
import dk.aau.cs.d402f13.utilities.ast.AstNode;
import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;
import dk.aau.cs.d402f13.utilities.errors.ArgumentError;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.errors.TypeError;

public class ConstValue extends Value {
  private AstNode expression = null;

  @Override
  public TypeValue getType() {
    return null;
  }

  public ConstValue(AstNode expression) {
    this.expression = expression;
  }

  public Value evaluate(Interpreter interpreter) throws StandardError {
    return interpreter.visit(expression);
  }

  @Override
  public Value add(Value other) throws TypeError {
    if(other.is(ListValue.type()))
      return ListValue.prepend(this, other);
    throw new TypeError("Cannot add " + other + " to a type");
  }
}
