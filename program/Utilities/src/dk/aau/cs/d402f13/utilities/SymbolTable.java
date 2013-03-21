package dk.aau.cs.d402f13.utilities;

import java.util.ArrayList;

import dk.aau.cs.d402f13.utilities.errors.ScopeError;

public class SymbolTable {
  
  public class SymbolInfo{
    Token.Type type;
    Boolean declared;
    String name;
    int value; //functions: nr of arguments
    
    SymbolInfo(Token.Type type, Boolean declared, String name, int value){
      this.type = type;
      this.declared = declared;
      this.name = name;
      this.value = value;
    }
    public void Print(){
      String dec = this.declared ? "DECLARED" : "UNDECLARED";
      System.out.println(this.type + " : " + this.name + "[" + dec + "]" + " : " + this.value);
    }
  }
  
  ArrayList<SymbolInfo> symbols;
  
  public SymbolTable(){
    symbols = new ArrayList<SymbolInfo>();
  }
  
  public void Print(){
    System.out.println("*******SYMBOL TABLE*******");
    for (SymbolInfo si : symbols){
      si.Print();
    }
  }
  
  public void Empty(){
    symbols.clear();
  }
  
  public void CheckErrors() throws ScopeError {
    ArrayList<String> errors = new ArrayList<String>();
    for (SymbolInfo s : symbols){   //returns a list of error that tells which ID's have been used without declaration.
      if (!s.declared){  
        switch (s.type){
        case FUNCTION: throw new ScopeError("Scope error, could not find declaration of function: " + s.name + " with " + s.value + " arguments", s);
        }
      }
    }
  }
  
  public void FoundDeclaredSymbol(Token.Type type, String name, int val){
    for (SymbolInfo s : symbols){//if symbol is found but not declared, change it to declared
      if (s.type == type && s.name.equals(name) && s.value == val){ 
        if (!s.declared){
          s.declared = true;
        }
        return;
      }  
    }
    //if symbol is not found, insert it as declared
    symbols.add(new SymbolInfo(type, true, name, val));
  }
  
  public void FoundUsedSymbol(Token.Type type, String name, int val){
    for (SymbolInfo s : symbols){//if symbol is already in table, don't insert it
      if (s.type == type && s.name.equals(name) && (s.value == val || s.value == -1)){
        return;
      }  
    }
    //if symbol is not found, insert it as undeclared
    symbols.add(new SymbolInfo(type, false, name, val));
  }
}