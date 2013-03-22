package dk.aau.cs.d402f13.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import dk.aau.cs.d402f13.utilities.errors.ScopeError;

public class SymbolTable {
  public enum SymbolType {
    FUNCTION,
    VARIABLE,
    IDENTIFIER
  }
  public class SymbolInfo{
    SymbolType type;
    Boolean declared;
    String name;
    int line;
    int offset;
    
    SymbolInfo(SymbolType type, Boolean declared, String name, int line, int offset){
      this.type = type;
      this.declared = declared;
      this.name = name;
      this.line = line;
      this.offset = offset;
    }
    public void print(){
      String dec = this.declared ? "DECLARED" : "UNDECLARED";
      System.out.println(this.type + " : " + this.name + "[" + dec + "]");
    }
    public int getLine(){
      return this.line;
    }
    public int getOffset(){
      return this.offset;
    }
  }
  
  ArrayList<SymbolInfo> symbols;
  SymbolTable parent; //points to the scope enclosing its own scope, null if outermost scope
  
  public SymbolTable getParent(){
    return this.parent;
  }
  
  public SymbolTable(){//for instantiating outer scope
    symbols = new ArrayList<SymbolInfo>();
  }
  
  public SymbolTable(SymbolTable parent){//for instantiating non outer scopes
    this(); //call other constructor
    this.parent = parent;
  }
  
  public void print(){
    System.out.println("*******SYMBOL TABLE*******");
    for (SymbolInfo si : symbols){
      si.print();
    }
  }
  
  public void empty(){
    symbols.clear();
  }
  
  public void checkErrors() throws ScopeError {
    for (SymbolInfo s : symbols){   //call this just before closing a scope. throws error if something in scope has not been declared
      if (!declared(s)){
        SymbolInfo closeMatching = findSuggestion(s); //find a name that looks similar and may have been mistyped
        String suggestion = closeMatching != null ? ", did you mean '" + closeMatching.name + "'?" : "";
        switch (s.type){
        case FUNCTION: throw new ScopeError("Scope error, could not find declaration of function: '" + s.name + "'" + suggestion, s);
        }
      }
    }
  }
  
  Boolean declared(SymbolInfo s){
    for (SymbolInfo si : symbols){
      if (si.type == s.type && s.name.equals(si.name) && si.declared)
        return true;
    }
    if (this.parent == null)    //if this symbol table is in outer scope, symbol is not declared
      return false;
    else
      return this.parent.declared(s); //else check if enclosing scope contains symbol 
  }
  
  public void foundDeclaredSymbol(SymbolType type, String name, int line, int offset) throws ScopeError{
    for (SymbolInfo s : symbols){//if symbol is found but not declared, change it to declared
      if (s.type == type && s.name.equals(name)){ 
        if (!s.declared){
          s.declared = true;
          s.line = line;
          s.offset = offset;
        }
        else{ //If a declaration in current scope already exists, the same type has been declared two times
          throw new ScopeError("Double declaration.", new SymbolInfo(type, false, name, line, offset));
        }
        return;
      }  
    }
    //if symbol is not found, insert it as declared
    symbols.add(new SymbolInfo(type, true, name, line, offset));
  }
  
  public void foundUsedSymbol(SymbolType type, String name, int line, int offset){
    for (SymbolInfo s : symbols){//if symbol is already in table, don't insert it
      if (s.type == type && s.name.equals(name)){
        return;
      }  
    }
    //if symbol is not found, insert it as undeclared
    symbols.add(new SymbolInfo(type, false, name, line, offset));
  }
  
  public SymbolInfo findSuggestion(SymbolInfo si){ //looks recursively in this scope + enclosing scopes
    if (this.parent == null)
      return findSuggestionThisScope(si);
    else{
      SymbolInfo bestThisScope = findSuggestionThisScope(si);
      SymbolInfo bestEnclosingScope = this.parent.findSuggestion(si);
      if (bestThisScope == null) {  //if symboltable does not contain any declarations
        return null;
      }
      //to reach this point, none of bestThisScope and bestEnclosingScope are null
      int disThisScope = Levenshtein.computeDistance(bestThisScope.name, si.name);
      int disEnclosingScope = Levenshtein.computeDistance(bestEnclosingScope.name, si.name);
      int maxDis = 2;
      if (disThisScope < disEnclosingScope && disThisScope <= maxDis){
        return bestThisScope;
      }
      else if (disEnclosingScope <= maxDis){
        return bestEnclosingScope;
      }
      else{
        return null;    //if none match enough, no suggestions was found
      }
    }
  }
  
  SymbolInfo findSuggestionThisScope(SymbolInfo si){
    SymbolInfo bestMatch = null;
    int best = 100000; //start with high difference so any match will be better than this
    for (SymbolInfo s : symbols){
      int dist =  Levenshtein.computeDistance(si.name, s.name);
      if (s.declared && si.type == s.type && dist < best){
        bestMatch = s;
        best = dist;
      }
    }
    return bestMatch;
  } 
}