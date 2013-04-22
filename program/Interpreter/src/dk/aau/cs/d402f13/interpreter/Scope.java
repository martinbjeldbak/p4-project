package dk.aau.cs.d402f13.interpreter;

import java.util.HashMap;

import dk.aau.cs.d402f13.values.*;

public class Scope {
  
  private Scope parent;
  private HashMap<String, Value> variableValues = new HashMap<String, Value>();
  
  private ObjectValue thisObject;
  
  /**
   * Instantiate a new, empty scope.
   */
  public Scope() {
    this.parent = null;
  }
  
  public Scope(ObjectValue thisObject) {
    this.thisObject = thisObject;
  }

  /**
   * Instantiate a new scope as a child of the argument. 
   * @param parent The scope to add the new scope to
   */
  public Scope(Scope parent) {
    this.parent = parent;
    if (parent != null) {
      thisObject = parent.thisObject;
    }
  }
  
  public Scope(Scope parent, ObjectValue thisObject) {
    this.parent = parent;
    this.thisObject = thisObject;
  }
  
  public ObjectValue getThis() {
    return thisObject;
  }
  
  /**
   * Add a variable to the current scope
   * @param var an identifying string for the varaible, used to
   *            retrieve the value again
   * @param value a Value that var maps to
   */
  public void addVariable(String var, Value value) {
    variableValues.put(var, value);
  }
  
  /**
   * Retrieves a value from the current scope or any parent scopes. Returns
   * null if none exist.
   * @param var the identifying string that the value was stored with
   * @return The stored Value
   */
  public Value getVariable(String var) {
    Value v = variableValues.get(var);
    if (v == null && parent != null) {
      return parent.getVariable(var);
    }
    return v;
  }
  
  /**
   * Returns the current scope's parent scope, if one exists.
   * @return the parent scope
   */
  public Scope getParent() {
    return parent;
  }
}
