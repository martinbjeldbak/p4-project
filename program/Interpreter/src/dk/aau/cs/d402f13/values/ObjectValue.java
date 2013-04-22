package dk.aau.cs.d402f13.values;

import java.util.HashMap;

import dk.aau.cs.d402f13.interpreter.Interpreter;
import dk.aau.cs.d402f13.interpreter.Scope;
import dk.aau.cs.d402f13.utilities.errors.DivideByZeroError;
import dk.aau.cs.d402f13.utilities.errors.InternalError;
import dk.aau.cs.d402f13.utilities.errors.NameError;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.errors.TypeError;

public class ObjectValue extends Value implements Cloneable {

  private TypeValue type;
  private Scope scope;
  
  private Value parent;
  private Value child;
  
  private boolean isSuper = false;
  
  private HashMap<String, Value> members = new HashMap<String, Value>();
  
  public ObjectValue(TypeValue type, Scope scope) {
    this.type = type;
    this.scope = scope;
  }
  
  public ObjectValue(TypeValue type, Scope scope, Value parent) {
    this(type, scope);
    this.parent = parent;
    if (parent instanceof ObjectValue) {
      ((ObjectValue)parent).child = this;
    }
  }
  
  public void addMember(String member, Value value) {
    members.put(member, value);
  }
  
  public Value getObjectMember(String member) throws StandardError {
    Value value = members.get(member);
    if (value == null) {
      if (parent != null) {
        if (parent instanceof ObjectValue) {
          return ((ObjectValue)parent).getObjectMember(member);
        }
        return parent.getMember(member);
      }
      throw new NameError("Undefined member: " + member);
    }
    return value;
  }
  
  @Override
  public Value getMember(String member) throws StandardError {
    if (!isSuper && child != null) {
      return child.getMember(member);
    }
    return getObjectMember(member);
  }

  /** {@inheritDoc}  */
  @Override
  public TypeValue getType() {
    return type;
  }
  
  public Value getAsSuper() throws InternalError {
    try {
      ObjectValue clone = (ObjectValue)clone();
      clone.isSuper = true;
      return clone;
    }
    catch (CloneNotSupportedException e) {
      throw new InternalError(e);
    }
  }
  
  public Value getParent() {
    return parent;
  }
  
  public Value getRoot() {
    Value obj = this;
    while (obj instanceof ObjectValue && ((ObjectValue)obj).parent != null) {
      obj = ((ObjectValue)obj).parent;
    }

    return obj;
  }
  
  /** {@inheritDoc}  */
  @Override
  public Value as(TypeValue type) throws TypeError {
    Value obj = this;
    while (obj.getType() != type) {
      if (!(obj instanceof ObjectValue) || ((ObjectValue)obj).parent == null) {
        throw new TypeError("Invalid cast to type " + type);
      }
      obj = ((ObjectValue)obj).parent;
    }
    return obj;
  }

  /** {@inheritDoc}  */
  @Override
  public Value call(Interpreter interpreter, Value ... actualParameters) throws StandardError {
    if (parent != null) {
      return getRoot().call(interpreter, actualParameters); 
    }
    return super.call(interpreter, actualParameters);
  }
  
  /** {@inheritDoc}  */
  @Override
  public BoolValue lessThan(Value other) throws TypeError {
    if (parent != null) {
      return getRoot().lessThan(other);
    }
    return super.lessThan(other);
  }
  
  /** {@inheritDoc}  */
  @Override
  public BoolValue lessThanEq(Value other) throws TypeError {
    if (parent != null) {
      return getRoot().lessThanEq(other);
    }
    return super.lessThanEq(other);
  }
  
  /** {@inheritDoc}  */
  @Override
  public BoolValue greaterThan(Value other) throws TypeError {
    if (parent != null) {
      return getRoot().greaterThan(other);
    }
    return super.greaterThan(other);
  }

  /** {@inheritDoc}  */
  @Override
  public BoolValue greaterThanEq(Value other) throws TypeError {
    if (parent != null) {
      return getRoot().greaterThanEq(other);
    }
    return super.greaterThanEq(other);
  }
  
  /** {@inheritDoc}  */
  @Override
  public Value add(Value other) throws TypeError {
    if (parent != null) {
      return getRoot().add(other);
    }
    return super.add(other);
  }

  /** {@inheritDoc}  */
  @Override
  public Value subtract(Value other) throws TypeError {
    if (parent != null) {
      return getRoot().subtract(other);
    }
    return super.subtract(other);
  }
  
  /** {@inheritDoc}  */
  @Override
  public Value multiply(Value other) throws TypeError {
    if (parent != null) {
      return getRoot().multiply(other);
    }
    return super.multiply(other);
  }
  
  /** {@inheritDoc}  */
  @Override
  public Value divide(Value other) throws TypeError, DivideByZeroError {
    if (parent != null) {
      return getRoot().divide(other);
    }
    return super.divide(other);
  }
  
  /** {@inheritDoc}  */
  @Override
  public Value mod(Value other) throws TypeError {
    if (parent != null) {
      return getRoot().mod(other);
    }
    return super.mod(other);
  }
  
  /** {@inheritDoc}  */
  @Override
  public Value negate() throws TypeError {
    if (parent != null) {
      return getRoot().negate();
    }
    return super.negate();
  }

  public void setScope(Scope scope) {
    this.scope = scope;
  }
  
  public Scope getScope() {
    return scope;
  }
}
