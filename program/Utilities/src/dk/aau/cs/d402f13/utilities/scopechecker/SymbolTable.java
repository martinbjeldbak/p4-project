package dk.aau.cs.d402f13.utilities.scopechecker;
import dk.aau.cs.d402f13.utilities.errors.ScopeError;
import dk.aau.cs.d402f13.utilities.Levenshtein;
import dk.aau.cs.d402f13.utilities.Tuple;

import java.util.HashSet;

public class SymbolTable {
  
  HashSet<SymbolInfo> symbols; //Boolean is flagged if symbol is a declaration
  SymbolTable parent; //points to a parent scope or null if global scope
  
  public SymbolTable getParent(){
    return this.parent;
  }
  
  public SymbolTable(){//for instantiating outer scope
    symbols = new HashSet<SymbolInfo>(); //Boolean means declared or undeclared
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
  
  public void checkConstErrors() throws ScopeError {
    //this error check is done after closing the global scope
    for (SymbolInfo s : symbols){   //this method is called just before exiting a scope
      if (s instanceof ConstSymbolInfo && !((ConstSymbolInfo)s).declared){ //if any function is marked as undeclared 
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
  if (symbols.contains(s)) //true if declared in local scope, false if used in local scope, null if neither
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

  }  
  public void foundUsedVar(SymbolInfo symbol) throws ScopeError{
  
  }
  public void foundDeclConst(SymbolInfo symbol) throws ScopeError{
   
  }
  public void foundUsedConst(SymbolInfo symbol){
    
  }
  
  public void foundGlobalConst(ConstSymbolInfo symbol) throws ScopeError{
   //if a CONSTANT is found which is not inside a type body
    if (symbol.declared){//if the constant is a declaration
      if (globalScope().symbols.contains(symbol)){
        if (globalScope().symbols.contains(symbol) == true){//true means declaration
          error(symbol, "Double declaration");
        }
        else{
          globalScope().symbols.add(symbol); //overwrites the symbol key to have true value associated -> means declaration 
        }
      }
      else{
        globalScope().symbols.add(symbol); //overwrites the symbol key to have true value associated -> means declaration 
      }
    }
    else{ //const is a use
      if (!this.globalScope().symbols.contains(symbol)){    //if function doesn't exist in global scope
        this.globalScope().symbols.add(symbol);        //insert function as undeclared
      }
    }
  }
  public void foundVar(VarSymbolInfo symbol) throws ScopeError{
    if (symbol.declared){   //if var is a declaration
      if (this.symbols.contains(symbol)){
        error(symbol, "Double declaration");
      }
      else{
        this.symbols.add(symbol);
      }
    }
    else{
      if (!visibleInScopes(symbol)){
        error(symbol, "Not declared");
      }
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
    for (SymbolInfo s : symbols){
      if (s.declared && si.getClass() == s.getClass()){
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