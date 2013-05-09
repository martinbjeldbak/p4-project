package dk.aau.cs.d402f13.values;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import dk.aau.cs.d402f13.interpreter.Callable;
import dk.aau.cs.d402f13.interpreter.ConstantCallable;
import dk.aau.cs.d402f13.interpreter.Interpreter;
import dk.aau.cs.d402f13.interpreter.Member;
import dk.aau.cs.d402f13.interpreter.ParentCallable;
import dk.aau.cs.d402f13.interpreter.Scope;
import dk.aau.cs.d402f13.interpreter.SetterCallable;
import dk.aau.cs.d402f13.interpreter.stdenv.constructors.DefaultConstructor;
import dk.aau.cs.d402f13.utilities.ast.AstNode;
import dk.aau.cs.d402f13.utilities.errors.ArgumentError;
import dk.aau.cs.d402f13.utilities.errors.NameError;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.errors.TypeError;

public class TypeValue extends Value {
  private HashMap<String, Member> members = new HashMap<String, Member>();
  private HashMap<String, Member> attributes = new HashMap<String, Member>();
  private String[] formalParameters;
  private String name;
  private String varParams = null;
  private TypeValue parent;
  private String parentName;
  private AstNode parentConstructor;
  
  private Callable callable = null;
  
  private ParentCallable parentCallable = null;
  
  private static TypeValue type = new TypeValue("Type", 1, false);

  @Override
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

  public TypeValue(String name, int minArity, boolean varArgs) {
    this.name = name;
    formalParameters = new String[minArity];
    if (varArgs) {
      varParams = "";
    }
    this.callable = new DefaultConstructor(this);
  }
  
  public TypeValue(String name, AstNode params, String parent, AstNode parentParams) {
    this(name, params);
    this.parentName = parent;
    this.parentConstructor = parentParams;
  }
  
  public TypeValue(String name, int minArity, boolean varArgs, Callable callable) {
    this.name = name;
    this.callable = callable;
    formalParameters = new String[minArity];
    if (varArgs) {
      varParams = "";
    }
  }
  
  public TypeValue(String name, boolean varArgs, String ... params) {
    this.name = name;
    formalParameters = params;
    if (varArgs) {
      varParams = params[params.length - 1];
      formalParameters = Arrays.copyOf(formalParameters, formalParameters.length - 1);
    }
  }

  public TypeValue(String name, TypeValue parent, ParentCallable callable,
      boolean varArgs, String ... params) {
    this(name, varArgs, params);
    this.parent = parent;
    this.parentCallable = callable;
  }

  public boolean isSubtypeOf(TypeValue type) {
    if (type == this) {
      return true;
    }
    if (parent != null && parent.isSubtypeOf(type)) {
      return true;
    }
    return false;
  }

  public void ensureSuperType(Interpreter interpreter) throws NameError {
    if (parent == null && parentName != null) {
      parent = interpreter.getSymbolTable().getType(parentName);
      if (parent == null) {
        throw new NameError("Type extends undefined type: " + parentName);
      }
    }
  }
  
  public boolean isSupertypeOf(TypeValue type) {
    return type.isSubtypeOf(this);
  }
  
  @Override
  public BoolValue equalsOp(Value other) {
    if (other == this) {
      return BoolValue.trueValue();
    }
    return BoolValue.falseValue();
  }
  
  @Override
  public String toString() {
    String s = name + "[";
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

  @Override
  public Value add(Value other) throws StandardError {
    if(other.is(ListValue.type()))
      return ListValue.prepend(this, other);
    throw new TypeError("Cannot add " + other + " to a type");
  }
  
  public Member getTypeMember(String name) {
    return members.get(name);
  }
  
  public HashMap<String, Member> getTypeMembers() {
    return members;
  }

  public void addTypeMember(String name, Member member) {
    members.put(name, member);
  }
  
  public void addAttribute(String name, Member member) {
    attributes.put(name, member);
  }
  
  public Value getInstance(Interpreter interpreter, Value... actualParameters) throws StandardError {
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
    Scope scope = new Scope();
    interpreter.getSymbolTable().openScope(scope);
    if (callable != null) {
      Value ret;
      ret = callable.call(interpreter, actualParameters);
      interpreter.getSymbolTable().closeScope();
      return ret;
    }
    else {
      ObjectValue ret;
      // Initialize constructor arguments
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
      // Find parent
      ensureSuperType(interpreter);
      if (parentCallable != null) {
        ret = new ObjectValue(interpreter, this, scope, parentCallable.call(interpreter), actualParameters);
      }
      else if (parent == null) {
        ret = new ObjectValue(interpreter, this, scope, actualParameters);
      }
      else {
        Value[] parentParams = ((ListValue)interpreter.visit(parentConstructor)).getValues();
        ret = new ObjectValue(interpreter, this, scope, parent.getInstance(interpreter, parentParams), actualParameters);
      }
      ret.setScope(new Scope(scope, ret));
      interpreter.getSymbolTable().openScope(ret.getScope());
      // Initialize attributes
      for (Entry<String, Member> e : attributes.entrySet()) {
        ret.addAttribute(e.getKey(), new MemberValue(e.getValue()));
      }
      interpreter.getSymbolTable().closeScope();
      interpreter.getSymbolTable().closeScope();
      return ret;
    }
  }

  @Override
  public Value call(Interpreter interpreter, Value... actualParameters)
      throws StandardError {
    return getInstance(interpreter, actualParameters);
  }

  public static Value expect(Value parameter, TypeValue type) throws StandardError {
    if (!parameter.is(type)) {
      throw new TypeError("Invalid type for value, expected " + type.toString());
    }
    return parameter.as(type);
  }

  public static Value[] expect(TypeValue type, Value ... parameters) throws StandardError {
    Value[] casted = new Value[parameters.length];
    for (int i = 0; i < parameters.length; i++) {
      if (!parameters[i].is(type)) {
        throw new TypeError("Invalid type for value, expected " + type.toString());
      }
      casted[i] = parameters[i].as(type); 
    }
    return casted;
  }

  public static Value expect(Value[] parameters, int i, TypeValue type) throws StandardError {
    if (!parameters[i].is(type)) {
      throw new TypeError("Invalid type for argument #" + i + ", expected " + type.toString());
    }
    return parameters[i].as(type);
  }

  public String getName() {
    return name;
  }

  public void addSetter(String attribute, TypeValue type) {
    String methodName = "set";
    methodName += Character.toUpperCase(attribute.charAt(0));
    methodName += attribute.substring(1);
    addTypeMember(methodName, new Member(1, false, new SetterCallable(attribute, type)));
  }
}
