package dk.aau.cs.d402f13.values;

import java.util.HashMap;

import dk.aau.cs.d402f13.interpreter.AbstractMember;
import dk.aau.cs.d402f13.interpreter.Callable;
import dk.aau.cs.d402f13.interpreter.Interpreter;
import dk.aau.cs.d402f13.interpreter.Member;
import dk.aau.cs.d402f13.interpreter.ParentCallable;
import dk.aau.cs.d402f13.utilities.ast.AstNode;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.errors.TypeError;

public class AbstractTypeValue extends TypeValue {
  private HashMap<String, AbstractMember> abstractMembers = new HashMap<String, AbstractMember>();

  public AbstractTypeValue(String name, AstNode params, String parent,
      AstNode parenParams) {
    super(name, params, parent, parenParams);
  }

  public AbstractTypeValue(String name, AstNode params) {
    super(name, params);
  }

  public AbstractTypeValue(String name, int minArity, boolean varArgs,
      Callable callable) {
    super(name, minArity, varArgs, callable);
  }

  public AbstractTypeValue(String name, boolean varArgs, String... params) {
    super(name, varArgs, params);
  }

  public AbstractTypeValue(String name, int minArity, boolean varArgs) {
    super(name, minArity, varArgs);
  }

  public AbstractTypeValue(String name, TypeValue parent,
      ParentCallable callable, boolean varArgs, String... params) {
    super(name, parent, callable, varArgs, params);
  }

  public void addAbstractMember(String name, AbstractMember member) {
    abstractMembers.put(name, member);
  }
  
  @Override
  public Value call(Interpreter interpreter, Value... actualParameters)
      throws StandardError {
    throw new TypeError("Unable to construct abstract type " + toString());
  }

  @Override
  public Value add(Value other) throws StandardError {
    if(other.is(ListValue.type()))
      return ListValue.prepend(this, other);
    throw new TypeError("Cannot add " + other + " to an abstract type");
  }
}