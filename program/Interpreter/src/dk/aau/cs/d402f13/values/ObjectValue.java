package dk.aau.cs.d402f13.values;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import dk.aau.cs.d402f13.interpreter.Interpreter;
import dk.aau.cs.d402f13.interpreter.Member;
import dk.aau.cs.d402f13.interpreter.Scope;
import dk.aau.cs.d402f13.utilities.errors.DivideByZeroError;
import dk.aau.cs.d402f13.utilities.errors.InternalError;
import dk.aau.cs.d402f13.utilities.errors.NameError;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.errors.TypeError;

public class ObjectValue extends Value implements Cloneable {

  private TypeValue type;
  private Scope scope;
  
  private Interpreter interpreter;
  
  private Value parent;
  private ObjectValue child;
  
  private boolean isSuper = false;
  
  private HashMap<String, Value> attributes = new HashMap<String, Value>();
  private HashMap<String, Value> memberCache = new HashMap<String, Value>();
  
  private Value[] parameters;
  
  public ObjectValue(Interpreter interpreter, TypeValue type, Scope scope, Value ... parameters) {
    this.interpreter = interpreter;
    this.type = type;
    this.scope = scope;
    this.parameters = parameters;
  }
  
  public ObjectValue(Interpreter interpreter, TypeValue type, Scope scope, Value parent, Value ... parameters) {
    this(interpreter, type, scope, parameters);
    this.parent = parent;
    if (parent instanceof ObjectValue) {
      ((ObjectValue)parent).child = this;
    }
  }
  
  public void addAttribute(String attribute, Value value) {
    attributes.put(attribute, value);
  }
  
  public Value getObjectMember(String name) throws StandardError {
    Value value = memberCache.get(name);
    if (value != null) {
      return value;
    }
    Member member = getType().getTypeMember(name);
    if (member == null) {
      if (parent != null) {
        if (parent instanceof ObjectValue) {
          return ((ObjectValue)parent).getObjectMember(name);
        }
        return parent.getMember(name);
      }
      throw new NameError("Undefined member: " + name);
    }
    value = member.getValue(interpreter, scope);
    memberCache.put(name, value);
    return value;
  }
  
  @Override
  public Value getMember(String member) throws StandardError {
    if (!isSuper && child != null) {
      return child.getMember(member);
    }
    return getObjectMember(member);
  }
  
  public Value getMember(String member, TypeValue type) throws StandardError {
    Value value = getMember(member);
    if (!value.is(type)) {
      throw new TypeError("Invalid type for member " + member
          + " of object of type " + this.type.toString()
          + ", expected " + type.toString());
    }
    return value;
  }
  
  public Value getAttribute(String attribute) throws StandardError {
    Value value = attributes.get(attribute);
    if (value == null) {
      return value;
    }
    if (value instanceof MemberValue) {
      value = ((MemberValue)value).getValue(interpreter, scope);
      attributes.put(attribute, value);
    }
    if (value instanceof ConstValue) {
      value = ((ConstValue)value).evaluate();
      attributes.put(attribute, value);
    }
    return value;
  }
  
  private ObjectValue cloneParentTree() throws InternalError {
    try {
      ObjectValue c = (ObjectValue)clone();
      c.scope = new Scope(scope.getParent(), c);
      c.memberCache = new HashMap<String, Value>();
      if (c.parent != null) {
        if (c.parent instanceof ObjectValue) {
          c.parent = ((ObjectValue)c.parent).cloneParentTree();
          ((ObjectValue)c.parent).child = c;
        }
      }
      return c;
    }
    catch (CloneNotSupportedException e) {
      throw new InternalError(e);
    }
  }
  
  private ObjectValue cloneChildTree() throws InternalError {
    try {
      ObjectValue c = (ObjectValue)clone();
      c.scope = new Scope(scope.getParent(), c);
      c.memberCache = new HashMap<String, Value>();
      if (c.child != null) {
        c.child = c.child.getClone();
        c.child.parent = c;
      }
      return c;
    }
    catch (CloneNotSupportedException e) {
      throw new InternalError(e);
    }
  }
  
  @Override
  public ObjectValue getClone() throws InternalError {
    ObjectValue c = cloneChildTree();
    if (c.parent != null && c.parent instanceof ObjectValue) {
      c.parent = ((ObjectValue)c.parent).cloneParentTree();
      ((ObjectValue)c.parent).child = c;
    }
    return c;
  }
  
  private ObjectValue clone = null;
  private ObjectValue cloneReturn = null;
  
  public void beginClone() throws InternalError {
    clone = (ObjectValue)getClone();
    clone.attributes = (HashMap<String, Value>)attributes.clone();
    cloneReturn = clone;
    while (cloneReturn.child != null) {
      cloneReturn = cloneReturn.child;
    }
  }
  
  public Value setAttribute(String attribute, Value value) throws StandardError {
    if (attributes.get(attribute) == null) {
      throw new NameError("Undefined attribute: $" + attribute);
    }
    if (clone == null) {
      beginClone();
      clone.addAttribute(attribute, value);
      return endClone();
    }
    clone.addAttribute(attribute, value);
    return null;
  }
  
  public Value endClone() {
    clone = null;
    return cloneReturn;
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
  
  /**
   * The most inefficient equals operation in the world...
   */
  @Override
  public BoolValue equalsOp(Value other) throws StandardError {
    if (other == null) {
      return BoolValue.falseValue();
    }
    if (other == this) {
      return BoolValue.trueValue();
    }
    if (!(other instanceof ObjectValue)) {
      return BoolValue.falseValue();
    }
    ObjectValue otherObject = (ObjectValue)other;
    if (otherObject.type != type) {
      return BoolValue.falseValue();
    }
    if (parameters.length != otherObject.parameters.length) {
      return BoolValue.falseValue();
    }
    if (attributes.size() != otherObject.attributes.size()) {
      return BoolValue.falseValue();
    }
    if (!Arrays.equals(parameters, otherObject.parameters)) {
      return BoolValue.falseValue();
    }
    for (String att : attributes.keySet()) {
      if (!getAttribute(att).equals(otherObject.getAttribute(att))) {
        return BoolValue.falseValue();
      }
    }
    if (parent == null) {
      return BoolValue.trueValue();
    }
    return parent.equalsOp(otherObject.parent);
  }
  
  /** {@inheritDoc}  */
  @Override
  public Value as(TypeValue type) throws StandardError {
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
  public BoolValue lessThan(Value other) throws StandardError {
    if (parent != null) {
      return getRoot().lessThan(other);
    }
    return super.lessThan(other);
  }
  
  /** {@inheritDoc}  */
  @Override
  public BoolValue lessThanEq(Value other) throws StandardError {
    if (parent != null) {
      return getRoot().lessThanEq(other);
    }
    return super.lessThanEq(other);
  }
  
  /** {@inheritDoc}  */
  @Override
  public BoolValue greaterThan(Value other) throws StandardError {
    if (parent != null) {
      return getRoot().greaterThan(other);
    }
    return super.greaterThan(other);
  }

  /** {@inheritDoc}  */
  @Override
  public BoolValue greaterThanEq(Value other) throws StandardError {
    if (parent != null) {
      return getRoot().greaterThanEq(other);
    }
    return super.greaterThanEq(other);
  }
  
  /** {@inheritDoc}  */
  @Override
  public Value add(Value other) throws StandardError {
    if (parent != null) {
      return getRoot().add(other);
    }
    return super.add(other);
  }

  /** {@inheritDoc}  */
  @Override
  public Value subtract(Value other) throws StandardError {
    if (parent != null) {
      return getRoot().subtract(other);
    }
    return super.subtract(other);
  }
  
  /** {@inheritDoc}  */
  @Override
  public Value multiply(Value other) throws StandardError {
    if (parent != null) {
      return getRoot().multiply(other);
    }
    return super.multiply(other);
  }
  
  /** {@inheritDoc}  */
  @Override
  public Value divide(Value other) throws StandardError {
    if (parent != null) {
      return getRoot().divide(other);
    }
    return super.divide(other);
  }
  
  /** {@inheritDoc}  */
  @Override
  public Value mod(Value other) throws StandardError {
    if (parent != null) {
      return getRoot().mod(other);
    }
    return super.mod(other);
  }
  
  /** {@inheritDoc}  */
  @Override
  public Value negate() throws StandardError {
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
