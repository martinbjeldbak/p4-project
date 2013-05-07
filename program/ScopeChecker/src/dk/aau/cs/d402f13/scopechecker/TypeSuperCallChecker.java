package dk.aau.cs.d402f13.scopechecker;

import dk.aau.cs.d402f13.utilities.errors.ScopeError;
import dk.aau.cs.d402f13.utilities.scopechecker.TypeSymbolInfo;
import dk.aau.cs.d402f13.utilities.scopechecker.TypeTable;

public class TypeSuperCallChecker {
  public void check(TypeTable tt) throws ScopeError{
  //throws an error if the call arguments in a call to a constructor does not match the
  //required number of arguments
    for (TypeSymbolInfo tsi : tt){
      if (tsi.parent != null){
        if (tsi.parent.args != -1 && tsi.parentCallArgs != tsi.parent.args){
        //arg count of -1 means a varlist which can be 0 to many arguments
          throw new ScopeError("Number of arguments does not match for call to parent type " + tsi.parent.name + " in type " + tsi.name + ". Expected " + tsi.parent.args + ", received " + tsi.parentCallArgs+".", tsi.line, tsi.offset);
        }
      }
    }  
  }
}
