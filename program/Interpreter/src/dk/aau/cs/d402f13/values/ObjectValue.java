package dk.aau.cs.d402f13.values;

import dk.aau.cs.d402f13.interpreter.Scope;

public class ObjectValue extends Value {

  private TypeValue type;
  private Scope scope;
  
  public ObjectValue(TypeValue type, Scope scope) {
    this.type = type;
    this.scope = scope;
  }

  @Override
  public TypeValue getType() {
    return type;
  }

}
