package dk.aau.cs.d402f13.values;

import java.util.HashMap;

import dk.aau.cs.d402f13.interpreter.Member;
import dk.aau.cs.d402f13.utilities.ast.AstNode;

public class TypeValue extends Value {
  private HashMap<String, Member> members = new HashMap<String, Member>();
  private String[] formalParameters;
  private String name;
  private String varParams = null;
  private TypeValue parent;
  private AstNode parentConstructor;

  public TypeValue(String name, AstNode params) {
    this.name = name;
    
    if (params.size() > 0 && params.get(params.size() - 1).type == AstNode.Type.VARS) {
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
  
  public TypeValue(String name, AstNode params, TypeValue parent, AstNode parenParams) {
    this(name, params);
    this.parent = parent;
    this.parentConstructor = parenParams;
  }
  
  public void addMember(String name, Member member) {
    members.put(name, member);
  }
  
  public Member getMember(String name) {
    return members.get(name);
  }
}
