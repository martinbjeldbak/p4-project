package dk.aau.cs.d402f13.interpreter.stdenv.game.wrappers;

import dk.aau.cs.d402f13.interpreter.Interpreter;
import dk.aau.cs.d402f13.interpreter.stdenv.game.GameEnvironment;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.errors.TypeError;
import dk.aau.cs.d402f13.values.BoolValue;
import dk.aau.cs.d402f13.values.CoordValue;
import dk.aau.cs.d402f13.values.IntValue;
import dk.aau.cs.d402f13.values.ListValue;
import dk.aau.cs.d402f13.values.ObjectValue;
import dk.aau.cs.d402f13.values.StrValue;
import dk.aau.cs.d402f13.values.TypeValue;
import dk.aau.cs.d402f13.values.Value;

public abstract class Wrapper {
  protected ObjectValue object;
  protected GameEnvironment env;
  protected Interpreter interpreter;

  public Wrapper(GameEnvironment env, ObjectValue object) {
    this.object = object;
    this.env = env;
    this.interpreter = env.getInterpreter();
  }
  
  public Wrapper(GameEnvironment env, Value object) {
    this(env, (ObjectValue)object);
  }
  
  public ObjectValue getObject() {
    return object;
  }
  
  public GameEnvironment getEnv() {
    return env;
  }
  
  protected Value getMember(String name) throws StandardError {
    return object.getMember(name);
  }
  
  protected Value getMember(String name, TypeValue type) throws StandardError {
    return object.getMember(name, type);
  }
  
  protected Value getMemberAs(String name, TypeValue type) throws StandardError {
    return object.getMemberAs(name, type);
  }
  
  protected String getMemberString(String name) throws StandardError {
    return object.getMemberString(name);
  }
  
  protected int getMemberInt(String name) throws StandardError {
    return object.getMemberInt(name);
  }
  
  protected boolean getMemberBoolean(String name) throws StandardError {
    return object.getMemberBoolean(name);
  }
  
  protected CoordValue getMemberCoord(String name) throws StandardError {
    return object.getMemberCoord(name);
  }
  
  protected Value[] getMemberList(String name) throws StandardError {
    return object.getMemberList(name);
  }
  
  protected Value[] getMemberList(String name, int minLength) throws StandardError {
    return object.getMemberList(name, minLength);
  }
  
  protected Value[] getMemberList(String name, TypeValue type) throws StandardError {
    return object.getMemberList(name, type);
  }
  
  protected Value[] getMemberList(String name, TypeValue type, int minLength) throws StandardError {
    return object.getMemberList(name, type, minLength);
  }
  
  protected Value callMember(String name, Value ... actualParameters) throws StandardError {
    return object.callMember(name, interpreter, actualParameters);
  }
  
  protected Value callMember(String name, TypeValue type, Value ... actualParameters) throws StandardError {
    return object.callMember(name, type, interpreter, actualParameters);
  }
  
  protected Value callMemberAs(String name, TypeValue type, Value ... actualParameters) throws StandardError {
    return object.callMemberAs(name, type, interpreter, actualParameters);
  }
  
  protected String callMemberString(String name, Value ... actualParameters) throws StandardError {
    return object.callMemberString(name, interpreter, actualParameters);
  }
  
  protected int callMemberInt(String name, Value ... actualParameters) throws StandardError {
    return object.callMemberInt(name, interpreter, actualParameters);
  }
  
  protected boolean callMemberBoolean(String name, Value ... actualParameters) throws StandardError {
    return object.callMemberBoolean(name, interpreter, actualParameters);
  }
  
  protected CoordValue callMemberCoord(String name, Value ... actualParameters) throws StandardError {
    return object.callMemberCoord(name, interpreter, actualParameters);
  }
  
  protected Value[] callMemberList(String name, Value ... actualParameters) throws StandardError {
    return object.callMemberList(name, interpreter, actualParameters);
  }
  
  protected Value[] callMemberList(String name, int minLength, Value ... actualParameters) throws StandardError {
    return object.callMemberList(name, minLength, interpreter, actualParameters);
  }
  
  protected Value[] callMemberList(String name, TypeValue type, Value ... actualParameters) throws StandardError {
    return object.callMemberList(name, type, interpreter, actualParameters);
  }
  
  protected Value[] callMemberList(String name, TypeValue type, int minLength, Value ... actualParameters) throws StandardError {
    return object.callMemberList(name, type, minLength, interpreter, actualParameters);
  }
  
  protected Value getAttribute(String name) throws StandardError {
    return object.getAttribute(name);
  }
  
  protected Value getAttribute(String name, TypeValue type) throws StandardError {
    return object.getAttribute(name, type);
  }
  
  protected Value getAttributeAs(String name, TypeValue type) throws StandardError {
    return object.getAttributeAs(name, type);
  }
  
  protected String getAttributeString(String name) throws StandardError {
    return object.getAttributeString(name);
  }
  
  protected int getAttributeInt(String name) throws StandardError {
    return getAttributeInt(name);
  }
  
  protected boolean getAttributeBoolean(String name) throws StandardError {
    return getAttributeBoolean(name);
  }
  
  protected CoordValue getAttributeCoord(String name) throws StandardError {
    return getAttributeCoord(name);
  }
  
  protected Value[] getAttributeList(String name) throws StandardError {
    return getAttributeList(name);
  }
  
  protected Value[] getAttributeList(String name, int minLength) throws StandardError {
    return getAttributeList(name, minLength);
  }
  
  protected Value[] getAttributeList(String name, TypeValue type) throws StandardError {
    return getAttributeList(name, type);
  }
  
  protected Value[] getAttributeList(String name, TypeValue type, int minLength) throws StandardError {
    return getAttributeList(name, type, minLength);
  }
}
