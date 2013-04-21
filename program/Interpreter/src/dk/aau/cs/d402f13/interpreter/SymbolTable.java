package dk.aau.cs.d402f13.interpreter;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Stack;

import dk.aau.cs.d402f13.values.*;

/**
 * Symbol table for the interpreter. Keeps track of scopes and all functions,
 * variables, and identifiers existing within all open scopes. 
 * @author Martin
 *
 */
public class SymbolTable {
  
  private HashMap<String, Value> constants = new HashMap<String, Value>();
  private HashMap<String, TypeValue> types = new HashMap<String, TypeValue>();
  private Stack<Scope> scopeStack = new Stack<Scope>();

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
  public Value getConstant(String name) {
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
   */
  public Value getVariable(String variable) {
    if(currentScope() == null) {
      return null;
    }
    return currentScope().getVariable(variable);
  }
  
  /**
   * Adds a type (class) to the symbol table with a unique identifier
   * @param name the unique name for the type
   * @param type an instance of the Type to be stored in the symbol table
   */
  public void addType(String name, TypeValue type) {
    types.put(name, type);
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
}
