package dk.aau.cs.d402f13.scopechecker;

import java.util.ArrayList;
import java.util.HashSet;
import dk.aau.cs.d402f13.utilities.errors.ScopeError;
import dk.aau.cs.d402f13.utilities.scopechecker.TypeSymbolInfo;

public class TypeExtendErrorChecker {
  public void check(ArrayList<TypeSymbolInfo> typeTable) throws ScopeError{ //the typeTable is obtained from TypeVisitor and contains the name of all classes, their members (constant and abstract), their constructor args and parent type + parent constructor args
    checkForCycles(typeTable);  //throw error on extend cycles
    checkSuperCallArgMatches(typeTable); //check that a type's call to a parents constructor matches the arg count
  }
  
 
  void checkForCycles(ArrayList<TypeSymbolInfo> typeTable) throws ScopeError{
    ArrayList<TypeSymbolInfo> types = new ArrayList<TypeSymbolInfo>();
    for (TypeSymbolInfo ti : typeTable){ //make copy of original list so we dont modify it
      types.add(ti);
    }
   HashSet<TypeSymbolInfo> path = new HashSet<TypeSymbolInfo>();
   while (types.size() > 0){
     TypeSymbolInfo current = types.get(0);
     types.remove(current);
     path.clear();
     while (current.parent != null){
       types.remove(current);
       path.add(current);
       current = current.parent;
       if (path.contains(current))
         throw new ScopeError("Cycle detected in type inheritance", current);
     }
   }
  }
  void checkSuperCallArgMatches(ArrayList<TypeSymbolInfo> typeTable) throws ScopeError{
    //ERROR ON: type A[] extends B[$a, $b, $c] {}  if B's constructor needs more or less than 3 args
    for (TypeSymbolInfo tsi : typeTable){
      if (tsi.parent != null){
        if (tsi.parentCallArgs != tsi.parent.args){
          throw new ScopeError("Number of arguments does not match for call to parent type", tsi);
        }
      }
    }
  }
}
