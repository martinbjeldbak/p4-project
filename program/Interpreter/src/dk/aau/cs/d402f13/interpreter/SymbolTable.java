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
  
  private HashMap<String, FunValue> functions = new HashMap<String, FunValue>();
  private HashMap<String, Value> identifiers = new HashMap<String, Value>();
  private Stack<Scope> scopeStack = new Stack<Scope>();

  public SymbolTable() {  
  }
  
  /**
   * Stores an identifier in the symbol table.
   * @param name  a unique string identifier of the value
   * @param value a value that 'name' maps to
   */
  public void addIdentifier(String name, Value value) {
    identifiers.put(name, value);
  }
  
  /**
   * Retrieves an identifier from the symbol table. Returns null if
   * none are found.
   * @param name the unique string the identifier was stored with
   * @return     the stored Value
   */
  public Value getIdentifier(String name) {
    return identifiers.get(name);
  }
  
  /**
   * Stores a function in the symbol table.
   * @param name  a unique string identifying the function
   * @param value the function and all it entails which was
   *              stored with 'name'
   */
  public void addFunction(String name, FunValue value) {
    functions.put(name, value);
  }
  
  /**
   * Get a function from the symbol table. Returns null if none
   * are found.
   * @param name the unique string the function was stored with
   * @return     the stored function
   */
  public FunValue getFunction(String name) {
    return functions.get(name);
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
