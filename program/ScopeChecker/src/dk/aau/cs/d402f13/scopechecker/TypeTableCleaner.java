package dk.aau.cs.d402f13.scopechecker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import dk.aau.cs.d402f13.utilities.errors.ScopeError;
import dk.aau.cs.d402f13.utilities.scopechecker.Member;
import dk.aau.cs.d402f13.utilities.scopechecker.TypeSymbolInfo;

public class TypeTableCleaner {
  public ArrayList<TypeSymbolInfo> clean(HashMap<String, TypeSymbolInfo> typeTable) throws ScopeError{
   addParentReferences(typeTable);          //adds references to a types parent, since only the name is kept at the moment
   ArrayList<TypeSymbolInfo> topSorted;
   topSorted = topologicalSort(typeTable);  //checks for extend cycles, so super type always appear before children, used for member propagation
   propagateMembers(topSorted);             //if a type's has a member, this member shall also be available in the derived types
   return topSorted;
  }
  void propagateMembers(ArrayList<TypeSymbolInfo> topSortedList) throws ScopeError{
    for (TypeSymbolInfo tsi : topSortedList){
       if (tsi.parent != null){
         for (Member mp : tsi.parent.members){
           Boolean found = false;
           for (Member m : tsi.members){
             if (m.name.equals(mp.name)){
               found = true;
               if (m.args != mp.args){
                 if (m.args == -1 || mp.args == -1)
                   throw new ScopeError("Mistmatch between function and constant with name "+m.name+" in type " + 
                                         tsi.name, m.line, m.offset);
                 else
                   throw new ScopeError("Number of arguments for member " + m.name + " in type does not match. " +
                   		                "Expected " + mp.args + ", received " + m.args, m.line, m.offset);
               }
               break;
             }
             
           }
           if (!found) //add the member to a type from its parent if it is not present in the type already
             tsi.addMember(mp);
         }
       }
    }
  }

  ArrayList<TypeSymbolInfo> topologicalSort(HashMap<String, TypeSymbolInfo> typeTable) throws ScopeError{
    ArrayList<TypeSymbolInfo> sorted = new ArrayList<TypeSymbolInfo>();
    for (TypeSymbolInfo tsi : typeTable.values()){
    if (tsi.parent != null)
      tsi.parent.children++;
    }
    for (TypeSymbolInfo tsi: typeTable.values()){
      while (tsi.children == 0){
        sorted.add(tsi);
        tsi.children = -1; //make sure this type is only added once
        if (tsi.parent == null){
          break;
        }
        else{
          tsi.parent.children--;
          tsi = tsi.parent;
        }
      }
    }
    Collections.reverse(sorted); //reverse list so we get super classes first
    if (sorted.size() != typeTable.size()){
      for (TypeSymbolInfo sti : typeTable.values()){
        Boolean found = false;
        for (TypeSymbolInfo sortedSti : sorted){
          if (sti == sortedSti){
            found = true;
            break;
          }
        }
        if (!found)
          throw new ScopeError("Extend cycle found in type " + sti.name, sti.line, sti.offset );
      }
    }
    return sorted;
  }
 
  void addParentReferences(HashMap<String, TypeSymbolInfo> typeTable) throws ScopeError{
    //this name must be looked up in the SymbolTable to update the real reference TypeInfo t.parent
    for (TypeSymbolInfo tsi : typeTable.values()){
      if (tsi.parentName.equals(tsi.name)){ //type cannot extend itself
        throw new ScopeError("Type " + tsi.name + " cannot extend itself", tsi.line, tsi.offset);
      }
      if (tsi.parentName != ""){
      if (typeTable.containsKey(tsi.parentName))
        tsi.parent = typeTable.get(tsi.parentName);
      else
        throw new ScopeError("Type " + tsi.name + " extends undefined type " + tsi.parentName, tsi.line, tsi.offset);
      }
    }
  }
  public void markAbstractTypes(ArrayList<TypeSymbolInfo> typeTable) {
    //if a type contains any abstract member (also propagated members), it must be marked as abstract for use in interpreter
    for (TypeSymbolInfo tsi : typeTable){
     for (Member m : tsi.members){
       if (m.abstrct){
        tsi.markASTnodeAsAbstract();
        continue;
       }
     }
    }
  }
}
