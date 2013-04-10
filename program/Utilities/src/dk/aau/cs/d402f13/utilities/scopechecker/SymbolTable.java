package dk.aau.cs.d402f13.utilities.scopechecker;
import dk.aau.cs.d402f13.utilities.errors.ScopeError;
import dk.aau.cs.d402f13.utilities.Levenshtein;
import dk.aau.cs.d402f13.utilities.Tuple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class SymbolTable {
  public enum SymbolType {
    FUNCTION,
    VARIABLE
  }
  
  HashMap<SymbolInfo, Boolean> symbols; //Boolean is flagged if symbol is a declaration
  SymbolTable parent; //points to a parent scope or null if global scope
  
  public SymbolTable getParent(){
    return this.parent;
  }
  
  public SymbolTable(){//for instantiating outer scope
    symbols = new HashMap<SymbolInfo, Boolean>(); //Boolean means declared or undeclared
  }
  
  public SymbolTable(SymbolTable parent){//for instantiating non outer scopes
    this(); //call other constructor
    this.parent = parent;
  }
  
  public void empty(){
    symbols.clear();
  }
  
  int getNestLevel(){
    return this.parent == null ? 0 : this.parent.getNestLevel() + 1; 
  }
  
  public void checkFunctionErrors() throws ScopeError {
    //this error check is done after closing the global scope
    for (SymbolInfo s : symbols.keySet()){   //this method is called just before exiting a scope
      if (s.type == SymbolType.FUNCTION && !symbols.get(s)){ //if any function is marked as undeclared 
            error(s, "Undeclared");
      }
    }
  }
  
  void error(SymbolInfo s, String msg) throws ScopeError{
    //Throw an error and try to find a suggestion, E.g. "Couldn't find findSometing, did you mean findSomething?"
    SymbolInfo closeMatching = findSuggestion(s).x; //Looks in all visible scopes for similar symbols
    String suggestion = closeMatching != null ? ", did you mean '" + closeMatching.name + "'?" : "";
    throw new ScopeError(msg + ": '" + s.name + "'" + suggestion, s);
  }
  
  Boolean visibleInScopes(SymbolInfo s){
    //Checks if a symbol is declared in either local or outer scopes
  if (symbols.containsKey(s)) //true if declared in local scope, false if used in local scope, null if neither
    return true;
  if (this.parent == null)    //if this symbol table is in outer scope, symbol is not declared
    return false;
  else
    return this.parent.visibleInScopes(s); //else check if enclosing scope contains symbol 
  }

  
  public String nestPrefix(){ //used for printing, to show nest level
    String prefix = "";
    for (int i = 0; i < getNestLevel(); i++){
      prefix += " ";
    }
    prefix += "#";
    return prefix;
  }
  
  public void foundDeclVar(SymbolInfo symbol) throws ScopeError{
    if (this.symbols.containsKey(symbol)){
      error(symbol, "Double declaration");
    }
    else{
      this.symbols.put(symbol, null); //null, cause not necessary to save if a variable is decl or ref.
    }
  }  
  public void foundUsedVar(SymbolInfo symbol) throws ScopeError{
    if (!visibleInScopes(symbol)){
      error(symbol, "Not declared");
    }
  }
  public void foundDeclFunc(SymbolInfo symbol) throws ScopeError{
    if (globalScope().symbols.containsKey(symbol)){
      if (globalScope().symbols.get(symbol) == true){//true means declaration
        error(symbol, "Double declaration");
      }
      else{
        globalScope().symbols.put(symbol, true); //overwrites the symbol key to have true value associated -> means declaration 
      }
    }
    else{
      globalScope().symbols.put(symbol, true); //overwrites the symbol key to have true value associated -> means declaration 
    }
  }
  public void foundUsedFunc(SymbolInfo symbol){
    
    if (!this.globalScope().symbols.containsKey(symbol)){    //if function doesn't exist in global scope
      this.globalScope().symbols.put(symbol, false);        //insert function as undeclared
    }
  }
  
  SymbolTable globalScope(){ //Find the global scope recursively by following parent references
    return this.parent == null ? this : this.parent.globalScope();
  }
  
  public Tuple<SymbolInfo, Integer> findSuggestion(SymbolInfo si){ //looks recursively in this scope + enclosing scopes
    int maxDis = 2; //allows "fial" -> "fail", "fil" -> "fail"
    //searches current scope for best suggestion
    SymbolInfo bestMatch = null;
    int bestDist = 1000; //start with high difference so any match will be better than this
    for (SymbolInfo s : symbols.keySet()){
      if (symbols.get(s) && si.type == s.type){
        int dist =  Levenshtein.computeDistance(si.name, s.name);
        if (dist <= maxDis && dist < bestDist){
          bestMatch = s;
          bestDist = dist;
        }
      }
    }
    Tuple<SymbolInfo, Integer> parentSuggestion = this.parent == null ? null : this.parent.findSuggestion(si);
    
    if (parentSuggestion == null){
      return new Tuple<SymbolInfo, Integer>(bestMatch, bestDist);
    }
    else{
      if (parentSuggestion.y < bestDist){ //if parent's suggestion is better
       return parentSuggestion; 
      }
      else{
        return new Tuple<SymbolInfo, Integer>(bestMatch, bestDist);
      }
    }
    
  }
  
}
  
  