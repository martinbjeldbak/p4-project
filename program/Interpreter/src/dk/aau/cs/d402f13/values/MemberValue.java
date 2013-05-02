package dk.aau.cs.d402f13.values;


import dk.aau.cs.d402f13.interpreter.Callable;
import dk.aau.cs.d402f13.interpreter.ConstantCallable;
import dk.aau.cs.d402f13.interpreter.Interpreter;
import dk.aau.cs.d402f13.interpreter.Member;
import dk.aau.cs.d402f13.interpreter.Scope;
import dk.aau.cs.d402f13.interpreter.SymbolTable;
import dk.aau.cs.d402f13.utilities.ast.AstNode;
import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;
import dk.aau.cs.d402f13.utilities.errors.ArgumentError;
import dk.aau.cs.d402f13.utilities.errors.StandardError;

public class MemberValue extends Value {
  private Member member;

  @Override
  public TypeValue getType() {
    return null;
  }

  public MemberValue(Member member) {
    this.member = member;
  }

  public Value getValue(Interpreter interpreter, Scope scope) throws StandardError {
    return member.getValue(interpreter, scope);
  }
  
  public Value getValue(Interpreter interpreter, Value value) throws StandardError {
    return member.getValue(interpreter, value);
  }
  
  public Value getValue(Interpreter interpreter) throws StandardError {
    return member.getValue(interpreter);
  }
}
