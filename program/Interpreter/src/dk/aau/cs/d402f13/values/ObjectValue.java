package dk.aau.cs.d402f13.values;

import dk.aau.cs.d402f13.interpreter.Scope;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.errors.TypeError;

public class ObjectValue extends Value {

  private TypeValue type;
  private Scope scope;
  
  private Value parent;
  
  public ObjectValue(TypeValue type, Scope scope) {
    this.type = type;
    this.scope = scope;
  }
  
  public ObjectValue(TypeValue type, Scope scope, Value parent) {
    this(type, scope);
    this.parent = parent;
  }

  @Override
  public TypeValue getType() {
    return type;
  }
  
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

}
