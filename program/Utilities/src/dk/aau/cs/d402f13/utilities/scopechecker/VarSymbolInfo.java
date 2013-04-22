package dk.aau.cs.d402f13.utilities.scopechecker;


public class VarSymbolInfo extends SymbolInfo{
  public VarSymbolInfo(String name, int line, int offset) {
    //we dont wanna save if a Var is a declaration. Every var declaration found is inserted into symbol table.
    //if we find a var USE, it MUST be present in the symbol table (as a declaration), or else we found an error
    super(name, true, line, offset);
  }

}
