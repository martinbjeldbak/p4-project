package dk.aau.cs.d402f13.scopechecker;

import dk.aau.cs.d402f13.utilities.errors.ScopeError;
import dk.aau.cs.d402f13.utilities.scopechecker.TypeSymbolInfo;
import dk.aau.cs.d402f13.utilities.scopechecker.TypeTable;

public class TypeParentRefMaker {

  TypeTable tt;
  public TypeParentRefMaker(TypeTable tt){
    this.tt = tt;
  }
  public void makeReferences() throws ScopeError{
    //this name must be looked up in the SymbolTable to update the real reference TypeInfo t.parent
    for (TypeSymbolInfo tsi : this.tt){
      if (tsi.parentName.equals(tsi.name)){ //type cannot extend itself
        throw new ScopeError("Type " + tsi.name + " cannot extend itself", tsi.line, tsi.offset);
      }
      if (tsi.parentName != ""){
      if (tt.typeExists(tsi.parentName))
        tsi.parent = tt.getType(tsi.parentName);
      else
        throw new ScopeError("Type " + tsi.name + " extends undefined type " + tsi.parentName, tsi.line, tsi.offset);
      }
    }
  }
}
