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
  
  public void foundUsedVar(VarSymbolInfo symbol) throws ScopeError{
    //any variable used must have been declared prior to its use
    if (!visibleInScopes(symbol))    
      throw new ScopeError("Variable " + symbol.name + " used but not declared", symbol.line, symbol.offset);
  }
  
  public void foundDeclVar(VarSymbolInfo symbol) throws ScopeError{
    if (this.symbols.contains(symbol)){
      throw new ScopeError("Variable " + symbol.name + " is already declared", symbol.line, symbol.offset);
    }
    else{
      this.symbols.add(symbol);
    }
  }

  SymbolTable globalScope(){ //Find the global scope recursively by following parent references
    return this.parent == null ? this : this.parent.globalScope();
  }
  
  public Tuple<SymbolInfo, Integer> findSuggestion(SymbolInfo si){ //looks recursively in this scope + enclosing scopes
    
    if (si instanceof VarSymbolInfo){
      int maxDis = 2; //allows "fial" -> "fail", "fil" -> "fail"
      //searches current scope for best suggestion
      SymbolInfo bestMatch = null;
      int bestDist = 1000; //start with high difference so any match will be better than this
      for (SymbolInfo s : symbols){
        if (si.getClass() == s.getClass()){
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
    else
      return null;
  }
}