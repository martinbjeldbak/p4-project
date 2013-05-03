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
    Value v = getMember(name);
    if (!v.is(type)) {
      throw new TypeError("Invalid type " + v.getType().getName()
          + " for member " + name + " of object of type " + object.getType().getName());
    }
    return v;
  }
  
  protected Value getMemberAs(String name, TypeValue type) throws StandardError {
    Value v = getMember(name, type);
    return v.as(type);
  }
  
  protected String getMemberString(String name) throws StandardError {
    return ((StrValue)getMemberAs(name, StrValue.type())).getValue();
  }
  
  protected int getMemberInt(String name) throws StandardError {
    return ((IntValue)getMemberAs(name, IntValue.type())).getValue();
  }
  
  protected boolean getMemberBoolean(String name) throws StandardError {
    return (BoolValue)getMemberAs(name, IntValue.type()) == BoolValue.trueValue();
  }
  
  protected CoordValue getMemberCoord(String name) throws StandardError {
    return (CoordValue)getMemberAs(name, ListValue.type());
  }
  
  protected Value[] getMemberList(String name) throws StandardError {
    return ((ListValue)getMemberAs(name, ListValue.type())).getValues();
  }
  
  protected Value[] getMemberList(String name, int minLength) throws StandardError {
    Value[] list = getMemberList(name);
    if (list.length < minLength) {
      throw new TypeError("Invalid length of list in member "
        + name + " of object of type " + object.getType().getName()
        + ", expected at least " + minLength);
    }
    return list;
  }
  
  protected Value[] getMemberList(String name, TypeValue type) throws StandardError {
    Value[] list = getMemberList(name);
    for (Value v : list) {
      if (!v.is(type)) {
        throw new TypeError("Invalid type " + type.getName()
          + "for value of list in member " + name
          + " of object of type " + object.getType().getName()
          + ", expected " + type);
      } 
    }
    return list;
  }
  
  protected Value[] getMemberList(String name, TypeValue type, int minLength) throws StandardError {
    Value[] list = getMemberList(name, minLength);
    for (Value v : list) {
      if (!v.is(type)) {
        throw new TypeError("Invalid type " + type.getName()
          + "for value of list in member " + name
          + " of object of type " + object.getType().getName()
          + ", expected " + type);
      } 
    }
    return list;
  }
}
