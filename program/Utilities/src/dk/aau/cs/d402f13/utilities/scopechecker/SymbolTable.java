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
  
  public void checkErrors() throws ScopeError {
    for (SymbolInfo s : symbols.keySet()){   //this method is called just before exiting a scope
      if (!symbols.get(s) && !declared(s)){//if symbol is not a declaration and is not declared in visible scopes 
        if (s.type == SymbolType.VARIABLE){

         error(s);
        }
        else if (s.type == SymbolType.FUNCTION){

          //Functions you be used before they are declared, so when exiting a scope with an undcl function,
          //transfer this undeclared symbol to the parent scope's symbol table, since it might be decl there
          if (this.parent != null)
            this.parent.foundUsedSymbol(s.type, s.name, s.line, s.offset);
          else{
            Boolean a = symbols.containsKey(new SymbolInfo(SymbolType.FUNCTION, "toActions", 30, 29));
            Boolean b = symbols.containsKey(new SymbolInfo(SymbolType.FUNCTION, "findSquares", 30, 29));
            error(s);
          }
        }
      }
    }
  }
  
  void error(SymbolInfo s) throws ScopeError{
    //Throw an error and try to find a suggestion, E.g. "Couldn't find findSometing, did you mean findSomething?"
    SymbolInfo closeMatching = findSuggestion(s).x; //Looks in all visible scopes for similar symbols
    String suggestion = closeMatching != null ? ", did you mean '" + closeMatching.name + "'?" : "";
    throw new ScopeError("Could not find declaration of: '" + s.name + "'" + suggestion, s);
  }
  
  Boolean declared(SymbolInfo s){
    //Checks if a symbol is declared in either local or outer scopes
  if (symbols.containsKey(s) && symbols.get(s) == true) //true if declared in local scope, false if used in local scope, null if neither
    return true;
  if (this.parent == null)    //if this symbol table is in outer scope, symbol is not declared
    return false;
  else
    return this.parent.declared(s); //else check if enclosing scope contains symbol 
  }
  
  public void foundDeclaredSymbol(SymbolType type, String name, int line, int offset) throws ScopeError{
    
    SymbolInfo foundDec = new SymbolInfo(type, name, line, offset);
    
    //If a declaration in current scope already exists, the same type has been declared two times, which is an error
    if (symbols.containsKey(foundDec) && symbols.get(foundDec) == true){
      throw new ScopeError("Double declaration.", new SymbolInfo(type, name, line, offset));
    }
        
    System.out.println(nestPrefix() + name + " (" + type + ") decl on line " + line + ", offset " + offset  );
    //if symbol is not already declared, insert it as declared
    symbols.put(foundDec, true);
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
    SymbolInfo foundUse = new SymbolInfo(type, name, line, offset);
    
    if (symbols.containsKey(foundUse)){ //if another use or decl exists in local scope, don't add this use
      System.out.println(nestPrefix() + name + " (" + type + ") used on line " + line + ", offset " + offset );
      return;
    }
    //if symbol is not found, insert it as undeclared
    symbols.put(new SymbolInfo(type, name, line, offset), false);
    System.out.println(nestPrefix() + name + " (" + type + ") used on line " + line + ", offset " + offset );
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
  
  