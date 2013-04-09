package dk.aau.cs.d402f13.interpreter;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Stack;

import dk.aau.cs.d402f13.values.*;

public class SymbolTable {
  
  private HashMap<String, FunValue> functions = new HashMap<String, FunValue>();
  private HashMap<String, Value> identifiers = new HashMap<String, Value>();
  private Stack<Scope> scopeStack = new Stack<Scope>();

  public SymbolTable() {
    
  }
  
  public void addIdentifier(String name, Value value) {
    identifiers.put(name, value);
  }
  
  public void getIdentifier(String name) {
    getIdentifier(name);
  }
  
  public void addFunction(String name, FunValue value) {
    functions.put(name, value);
  }
  
  public FunValue getFunction(String name) {
    return functions.get(name);
  }
  
  public void addVariable(String variable, Value value) {
    currentScope().addVariable(variable, value);
  }
  
  public Value getVariable(String variable) {
    if(currentScope() == null) {
      return null;
    }
    return currentScope().getVariable(variable);
  }
  
  public Scope currentScope() {
    try {
      return scopeStack.peek();
    }
    catch(EmptyStackException ex) {
      return null;
    }
  }
  
  public void openScope() {
    scopeStack.push(new Scope(currentScope()));
  }
  
  public void openScope(Scope scope) {
    scopeStack.push(scope);
  }
  
  public void closeScope() {
    scopeStack.pop();
  }
}
