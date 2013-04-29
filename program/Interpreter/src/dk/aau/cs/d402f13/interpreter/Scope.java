package dk.aau.cs.d402f13.interpreter;

import java.util.HashMap;

import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.values.*;

public class Scope {
  
  private Scope parent;
  private HashMap<String, Value> variableValues = new HashMap<String, Value>();
  
  private Value thisObject;
  
  /**
   * Instantiate a new, empty scope.
   */
  public Scope() {
    this.parent = null;
  }
  
  public Scope(Value thisObject) {
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
  
  public Scope(Scope parent, Value thisObject) {
    this.parent = parent;
    this.thisObject = thisObject;
  }
  
  public Value getThis() {
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
   * @throws StandardError 
   */
  public Value getVariable(String var) throws StandardError {
    Value v = variableValues.get(var);
    if (v == null) {
      if (parent != null) {
        return parent.getVariable(var);
      }
      else if (thisObject != null && thisObject instanceof ObjectValue){
        return ((ObjectValue)thisObject).getAttribute(var);
      }
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
