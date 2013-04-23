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
}
