package dk.aau.cs.d402f13.interpreter;

import java.util.HashMap;

import dk.aau.cs.d402f13.values.*;

public class Scope {
  
  private Scope parent;
  
  private HashMap<String, Value> variableValues = new HashMap<String, Value>();
  
  public Scope() {
    this.parent = null;
  }

  public Scope(Scope parent) {
    this.parent = parent;
  }
  
  public void addVariable(String var, Value value) {
    variableValues.put(var, value);
  }
  
  public Value getVariable(String var) {
    Value v = variableValues.get(var);
    if (v == null && parent != null) {
      return parent.getVariable(var);
    }
    return v;
  }
  
  public Scope getParent() {
    return parent;
  }
}
