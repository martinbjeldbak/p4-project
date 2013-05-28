package dk.aau.cs.d402f13.interpreter;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Stack;
import java.util.Map.Entry;

import dk.aau.cs.d402f13.utilities.errors.NameError;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.errors.TypeError;
import dk.aau.cs.d402f13.values.*;

/**
 * Symbol table for the interpreter. Keeps track of scopes and all functions,
 * variables, and identifiers existing within all open scopes. 
 */
public class SymbolTable {
  
  protected HashMap<String, Value> constants = new HashMap<String, Value>();
  protected HashMap<String, TypeValue> types = new HashMap<String, TypeValue>();
  private Stack<Scope> scopeStack = new Stack<Scope>();
  
  private Interpreter interpreter;
  
  public Interpreter getInterpreter() {
    return interpreter;
  }
  
  public void setInterpreter(Interpreter interpreter) {
    this.interpreter = interpreter;
  }

  public SymbolTable() {  
  }
  
  /**
   * Stores a constant to the symbol table.
   * @param name  a unique string identifying the function
   * @param value the function and all it entails which was
   *              stored with 'name'
   */
  public void addConstant(String name, Value value) {
    constants.put(name, value);
  }
  
  /**
   * Get a constant from the symbol table. Returns null if none
   * are found.
   * @param name the unique string the function was stored with
   * @return     the stored function
   */
  public Value getConstant(String name) throws StandardError {
    if (currentScope() != null) {
      Value thisObject = currentScope().getThis();
      if (thisObject != null) {
        try {
          return thisObject.getMember(name);
        }
        catch (NameError e) { }
      }
    }
    return constants.get(name);
  }
  
  /**
   * Stores a variable to the current scope.
   * @param variable a string value identifying the Value object
   * @param value    the Value object to store
   */
  public void addVariable(String variable, Value value) {
    currentScope().addVariable(variable, value);
  }
  
  /**
   * Gets a variable from the current scope or parent scope(s). Can return
   * null if nothing is found.
   * @param variable the string identifier the variable was stored with
   * @return         the stored Value
   * @throws StandardError 
   */
  public Value getVariable(String variable) throws StandardError {
    if(currentScope() == null) {
      return null;
    }
    return currentScope().getVariable(variable);
  }
  
  public Value getVariable(String name, TypeValue type) throws StandardError {
    Value v = getVariable(name);
    if (!v.is(type)) {
      throw new TypeError("Invalid type " + v.getType().getName()
          + " for parameter $" + name + ", expected " + type.getName());
    }
    return v;
  }
  
  public Value getVariableAs(String name, TypeValue type) throws StandardError {
    Value v = getVariable(name, type);
    return v.as(type);
  }
  
  public String getVariableString(String name) throws StandardError {
    return ((StrValue)getVariableAs(name, StrValue.type())).getValue();
  }
  
  public int getVariableInt(String name) throws StandardError {
    return ((IntValue)getVariableAs(name, IntValue.type())).getValue();
  }
  
  public boolean getVariableBoolean(String name) throws StandardError {
    return (BoolValue)getVariableAs(name, IntValue.type()) == BoolValue.trueValue();
  }
  
  public CoordValue getVariableCoord(String name) throws StandardError {
    return (CoordValue)getVariableAs(name, ListValue.type());
  }
  
  public Value[] getVariableList(String name) throws StandardError {
    return ((ListValue)getVariableAs(name, ListValue.type())).getValues();
  }
  
  public Value[] getVariableList(String name, int minLength) throws StandardError {
    Value[] list = getVariableList(name);
    if (list.length < minLength) {
      throw new TypeError("Invalid length of list in parameter $"
        + name + ", expected at least " + minLength);
    }
    return list;
  }
  
  public Value[] getVariableList(String name, TypeValue type) throws StandardError {
    Value[] list = getVariableList(name);
    for (Value v : list) {
      if (!v.is(type)) {
        throw new TypeError("Invalid type " + v.getType().getName()
          + " for value of list in parameter $" + name
          + ", expected " + type.getName());
      } 
    }
    return list;
  }
  
  public Value[] getVariableList(String name, TypeValue type, int minLength) throws StandardError {
    Value[] list = getVariableList(name, minLength);
    for (Value v : list) {
      if (!v.is(type)) {
        throw new TypeError("Invalid type " + v.getType().getName()
            + " for value of list in parameter $" + name
            + ", expected " + type.getName());
      } 
    }
    return list;
  }
  
  /**
   * Adds a type (class) to the symbol table with a unique identifier
   * @param name the unique name for the type
   * @param type an instance of the Type to be stored in the symbol table
   */
  public void addType(String name, TypeValue type) {
    types.put(name, type);
  }
  
  public void addType(TypeValue type) {
    addType(type.getName(), type);
  }
  
  public Value getThis() {
    if(currentScope() == null) {
      return null;
    }
    return currentScope().getThis();
  }
  
  /**
   * Gets a Type from the symbol table that was stored with the unique
   * identifier. Can return null
   * @param name the unique identifier for the Type
   * @return a Value with instance Type
   */
  public TypeValue getType(String name) {
    return types.get(name);
  }
  
  /**
   * Gets the current scope.
   * @return the current scope
   */
  public Scope currentScope() {
    try {
      return scopeStack.peek();
    }
    catch(EmptyStackException ex) {
      return null;
    }
  }
  
  /**
   * Creates a new empty child scope.
   */
  public void openScope() {
    scopeStack.push(new Scope(currentScope()));
  }
  
  /**
   * Creates a new child scope with the supplied scope.
   * @param scope the scope to be stored as a child of the current
   * scope
   */
  public void openScope(Scope scope) {
    scopeStack.push(scope);
  }
  
  /**
   * Closes the current scope.
   */
  public void closeScope() {
    scopeStack.pop();
  }

  public void finalizeTypes(Interpreter interpreter) throws StandardError {
    for (Entry<String, TypeValue> e : types.entrySet()) {
      e.getValue().ensureSuperType(interpreter);
    }
  }
}
