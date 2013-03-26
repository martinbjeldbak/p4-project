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
    Boolean declaration;
    String name;
    int line;
    int offset;
    
    SymbolInfo(SymbolType type, Boolean declared, String name, int line, int offset){
      this.type = type;
      this.declaration = declared;
      this.name = name;
      this.line = line;
      this.offset = offset;
    }
    public void print(){
      String dec = this.declaration ? "DECLARED" : "UNDECLARED";
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
  
  int getNestLevel(){
    return this.parent == null ? 0 : this.parent.getNestLevel() + 1; 
  }
  
  public void checkErrors() throws ScopeError {
    for (SymbolInfo s : symbols){   //this method is called just before exiting a scope
      if (!s.declaration && !declared(s)){//if symbol is not a declaration and is not declared in visible scopes 
        SymbolInfo closeMatching = findSuggestion(s); //find a name that looks similar and may have been mistyped
        String suggestion = closeMatching != null ? ", did you mean '" + closeMatching.name + "'?" : "";
        throw new ScopeError("Scope error, could not find declaration of: '" + s.name + "'" + suggestion, s);
      }
    }
  }
  
  Boolean declared(SymbolInfo s){
    //Checks if a symbol is declared in either local or outer scopes
    for (SymbolInfo si : symbols){
      if (si.type == s.type && s.name.equals(si.name) && si.declaration){
        return true;
      }
    }
    if (this.parent == null)    //if this symbol table is in outer scope, symbol is not declared
      return false;
    else
      return this.parent.declared(s); //else check if enclosing scope contains symbol 
  }
  
  public void foundDeclaredSymbol(SymbolType type, String name, int line, int offset) throws ScopeError{
    for (SymbolInfo s : symbols){//if symbol is found but not declared, change it to declared
      if (s.type == type && s.name.equals(name)){ 
        if (!s.declaration){
          s.declaration = true;
          s.line = line;
          s.offset = offset;
        }
        else{ //If a declaration in current scope already exists, the same type has been declared two times
          throw new ScopeError("Double declaration.", new SymbolInfo(type, false, name, line, offset));
        }
        return;
      }  
    }
   
    System.out.println(nestPrefix() + name + " (" + type + ") decl on line " + line + ", offset " + offset  );
    //if symbol is not found, insert it as declared
    symbols.add(new SymbolInfo(type, true, name, line, offset));
  }
  
  public String nestPrefix(){ //used for printing, to show nest level
    String prefix = "";
    for (int i = 0; i < getNestLevel(); i++){
      prefix += " ";
    }
    prefix += "#";
    return prefix;
  }
  
  public void foundUsedSymbol(SymbolType type, String name, int line, int offset){
    for (SymbolInfo s : symbols){//if symbol is already in table, don't insert it
      if (s.type == type && s.name.equals(name)){
        System.out.println(nestPrefix() + name + " (" + type + ") used on line " + line + ", offset " + offset );
        return;
      }  
    }
    //if symbol is not found, insert it as undeclared
    symbols.add(new SymbolInfo(type, false, name, line, offset));
    System.out.println(nestPrefix() + name + " (" + type + ") used on line " + line + ", offset " + offset );
  }
  
  public SymbolInfo findSuggestion(SymbolInfo si){ //looks recursively in this scope + enclosing scopes
    if (this.parent == null)
      return findSuggestionThisScope(si);
    else{
      SymbolInfo bestThisScope = findSuggestionThisScope(si);
      SymbolInfo bestParentScope = this.parent.findSuggestion(si);
      if (bestThisScope == null && bestParentScope == null) 
        return null;    //if symboltable does not contain any declarations in any scopes
      else if (bestThisScope == null)
        return bestParentScope;
      else if (bestParentScope == null)
        return bestThisScope;
      
      //to reach this point, none of bestThisScope and bestParentScope are null
      int disThisScope = Levenshtein.computeDistance(bestThisScope.name, si.name);
      int disEnclosingScope = Levenshtein.computeDistance(bestParentScope.name, si.name);
      int maxDis = 2;
      if (disThisScope < disEnclosingScope && disThisScope <= maxDis){
        return bestThisScope;
      }
      else if (disEnclosingScope <= maxDis){
        return bestParentScope;
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
      if (s.declaration && si.type == s.type && dist < best){
        bestMatch = s;
        best = dist;
      }
    }
    return bestMatch;
  } 
}