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

public class ConstMemberValue extends Value {
  private ConstantCallable constant;
  private int minArity;
  private boolean varArgs;
  private Callable function;

  @Override
  public TypeValue getType() {
    return null;
  }

  public ConstMemberValue(ConstantCallable constant) {
    this.constant = constant;
  }

  public ConstMemberValue(int minArity, boolean varArgs, Callable function) {
    this.minArity = minArity;
    this.varArgs = varArgs;
    this.function = function;
  }

  public Value evaluate(Interpreter interpreter, Value object) throws StandardError {
    if (function != null) {
      return new FunValue(minArity, varArgs, function, new Scope(object));
    }
    return constant.call(interpreter, object);
  }
}
