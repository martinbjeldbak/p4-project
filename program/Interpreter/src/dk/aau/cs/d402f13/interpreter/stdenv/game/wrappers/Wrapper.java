package dk.aau.cs.d402f13.interpreter.stdenv.game.wrappers;

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

  public Wrapper(GameEnvironment env, ObjectValue object) {
    this.object = object;
    this.env = env;
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
    return getMemberInt(name);
  }
  
  protected boolean getMemberBoolean(String name) throws StandardError {
    return getMemberBoolean(name);
  }
  
  protected CoordValue getMemberCoord(String name) throws StandardError {
    return getMemberCoord(name);
  }
  
  protected Value[] getMemberList(String name) throws StandardError {
    return getMemberList(name);
  }
  
  protected Value[] getMemberList(String name, int minLength) throws StandardError {
    return getMemberList(name, minLength);
  }
  
  protected Value[] getMemberList(String name, TypeValue type) throws StandardError {
    return getMemberList(name, type);
  }
  
  protected Value[] getMemberList(String name, TypeValue type, int minLength) throws StandardError {
    return getMemberList(name, type, minLength);
  }
}
