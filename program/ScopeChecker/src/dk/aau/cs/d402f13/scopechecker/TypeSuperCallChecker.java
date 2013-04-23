package dk.aau.cs.d402f13.scopechecker;

import java.util.ArrayList;
import java.util.HashSet;
import dk.aau.cs.d402f13.utilities.errors.ScopeError;
import dk.aau.cs.d402f13.utilities.scopechecker.TypeSymbolInfo;

public class TypeSuperCallChecker {
  public void check(ArrayList<TypeSymbolInfo> typeTable) throws ScopeError{ //the typeTable is obtained from TypeVisitor and contains the name of all classes, their members (constant and abstract), their constructor args and parent type + parent constructor args
    checkSuperCallArgMatches(typeTable); //check that a type's call to a parents constructor matches the arg count
  }
  
  
  void checkSuperCallArgMatches(ArrayList<TypeSymbolInfo> typeTable) throws ScopeError{
    //ERROR WHEN type A[] extends B[$a, $b, $c] {}  if B's constructor argument count does not match
    for (TypeSymbolInfo tsi : typeTable){
      if (tsi.parent != null){
        if (tsi.parentCallArgs != tsi.parent.args){
          throw new ScopeError("Number of arguments does not match for call to parent type", tsi);
        }
      }
    }
  }
}
