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
        if (tsi.parent.varArgs == true){
          if (tsi.parentCallArgs < tsi.parent.args)
            throw new ScopeError("Number of arguments does not match for call to parent type " + tsi.parent.name + " in type " + tsi.name + ". Expected a minimum of " + tsi.parent.args + ", received " + tsi.parentCallArgs+".", tsi.line, tsi.offset);
        }
        else if (tsi.parentCallArgs != tsi.parent.args){
          //it has no varArgs, so argcount must match exactly
          throw new ScopeError("Number of arguments does not match for call to parent type " + tsi.parent.name + " in type " + tsi.name + ". Expected " + tsi.parent.args + ", received " + tsi.parentCallArgs+".", tsi.line, tsi.offset);
        }
      }
    }  
  }
}
