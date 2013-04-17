package dk.aau.cs.d402f13.scopechecker;

import java.util.ArrayList;
import java.util.HashSet;
import dk.aau.cs.d402f13.utilities.errors.ScopeError;
import dk.aau.cs.d402f13.utilities.scopechecker.SymbolInfo;
import dk.aau.cs.d402f13.utilities.scopechecker.SymbolTable.SymbolType;

public class TypeExtendErrorChecker {
  public void check(ArrayList<TypeInfo> typeTable) throws ScopeError{ //must be invoked after visitor is finish traversing AST
    //every TypeInfo t only contains the name of its super type in its String t.parentName
    //this name must be looked up in the SymbolTable to update the real reference TypeInfo t.parent
    for (TypeInfo ti : typeTable){
      if (ti.parentName.equals(ti.name)){ //type cannot extend itself
        throw new ScopeError("Type cannot extend itself", new SymbolInfo(SymbolType.TYPE, ti.name, ti.line, ti.offset));
      }
      if (ti.parentName != ""){
      Boolean foundParentType = false;
      for (TypeInfo ti2 : typeTable){
        if (ti.parentName.equals(ti2.name)){
          ti.parent = ti2;
          foundParentType = true;
          break;
        }
      }
      if (!foundParentType)
        throw new ScopeError("Type extends undefined type", new SymbolInfo(SymbolType.TYPE, ti.name, ti.line, ti.offset));
      }
    }
    checkForCycles(typeTable);
  }
  void checkForCycles(ArrayList<TypeInfo> typeTable) throws ScopeError{
    ArrayList<TypeInfo> types = new ArrayList<TypeInfo>();
    for (TypeInfo ti : typeTable){ //make copy of original list so we dont modify it
      types.add(ti);
    }
   HashSet<TypeInfo> path = new HashSet<TypeInfo>();
   while (types.size() > 0){
     TypeInfo current = types.get(0);
     types.remove(current);
     path.clear();
     while (current.parent != null){
       types.remove(current);
       path.add(current);
       current = current.parent;
       if (path.contains(current))
         throw new ScopeError("Cycle detected in type inheritance", new SymbolInfo(SymbolType.TYPE, current.name, current.line, current.offset));
     }
   }
  }
}
