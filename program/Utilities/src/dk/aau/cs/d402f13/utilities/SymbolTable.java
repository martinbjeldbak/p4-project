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
  public SymbolTable(){
    symbols = new ArrayList<SymbolInfo>();
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
    for (SymbolInfo s : symbols){   //returns a list of error that tells which ID's have been used without declaration.
      if (!s.declared){
        SymbolInfo closeMatching = findSuggestion(s);
        String suggestion = closeMatching != null ? ", did you mean '" + closeMatching.name + "'?" : "";
        switch (s.type){
        case FUNCTION: throw new ScopeError("Scope error, could not find declaration of function: '" + s.name + "'" + suggestion, s);
        }
      }
    }
  }
  
  public void foundDeclaredSymbol(SymbolType type, String name, int line, int offset){
    for (SymbolInfo s : symbols){//if symbol is found but not declared, change it to declared
      if (s.type == type && s.name.equals(name)){ 
        if (!s.declared){
          s.declared = true;
          s.line = line;
          s.offset = offset;
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
  
  public SymbolInfo findSuggestion(SymbolInfo si){
    SymbolInfo bestMatch = null;
    int best = 3;
    for (SymbolInfo s : symbols){   //if a symbol in the symbol table deports with only 1 char
      int dist =  Levenshtein.computeDistance(si.name, s.name);
      if (s.declared && si.type == s.type && dist < best){
        bestMatch = s;
        best = dist;
      }
    }
    return bestMatch;
  } 
}