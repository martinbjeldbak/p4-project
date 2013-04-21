package dk.aau.cs.d402f13.values;

import java.util.HashMap;

import dk.aau.cs.d402f13.interpreter.Callable;
import dk.aau.cs.d402f13.interpreter.Interpreter;
import dk.aau.cs.d402f13.interpreter.Member;
import dk.aau.cs.d402f13.interpreter.Scope;
import dk.aau.cs.d402f13.interpreter.stdenv.constructors.DefaultConstructor;
import dk.aau.cs.d402f13.utilities.ast.AstNode;
import dk.aau.cs.d402f13.utilities.errors.ArgumentError;
import dk.aau.cs.d402f13.utilities.errors.StandardError;

public class TypeValue extends Value {
  private HashMap<String, Member> members = new HashMap<String, Member>();
  private String[] formalParameters;
  private String name;
  private String varParams = null;
  private TypeValue parent;
  private String parentName;
  private AstNode parentConstructor;
  
  private Callable callable = null;
  
  private static TypeValue type = new TypeValue("Type", 1, false);
  
  public TypeValue getType() {
    return type;
  }
  
  public static TypeValue type() {
    return type;
  }

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
  
  public TypeValue(String name, AstNode params, String parent, AstNode parenParams) {
    this(name, params);
    this.parentName = parent;
    this.parentConstructor = parenParams;
  }
  
  public TypeValue(String name, int minArity, boolean varArgs, Callable callable) {
    this.name = name;
    this.callable = callable;
    formalParameters = new String[minArity];
    if (varArgs) {
      varParams = "";
    }
  }
  
  public TypeValue(String name, int minArity, boolean varArgs) {
    this.name = name;
    formalParameters = new String[minArity];
    if (varArgs) {
      varParams = "";
    }
    this.callable = new DefaultConstructor(this);
  }

  /** @TODO check inheritance */
  public boolean isSubtypeOf(TypeValue type) {
    if (type == this) {
      return true;
    }
    return false;
  }
  
  @Override
  public String toString() {
    return name;
  }
  
  public void addMember(String name, Member member) {
    members.put(name, member);
  }
  
  public Member getMember(String name) {
    return members.get(name);
  }
  
  @Override
  public Value call(Interpreter interpreter, Value... actualParameters)
      throws StandardError {
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
    interpreter.getSymbolTable().openScope(new Scope());
    Value ret;
    if (callable != null) {
      ret = callable.call(interpreter, actualParameters);
    }
    else {
      return super.call(interpreter, actualParameters);
    }
    interpreter.getSymbolTable().closeScope();
    return ret;
  }
}
