package dk.aau.cs.d402f13.values;

import java.util.HashMap;

import dk.aau.cs.d402f13.interpreter.AbstractMember;
import dk.aau.cs.d402f13.interpreter.Callable;
import dk.aau.cs.d402f13.interpreter.Member;
import dk.aau.cs.d402f13.utilities.ast.AstNode;

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

  public void addAbstractMember(String name, AbstractMember member) {
    abstractMembers.put(name, member);
  }
}
