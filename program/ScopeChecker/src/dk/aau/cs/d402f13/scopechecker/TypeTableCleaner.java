package dk.aau.cs.d402f13.scopechecker;

import java.util.ArrayList;
import java.util.Collections;

import dk.aau.cs.d402f13.utilities.errors.ScopeError;
import dk.aau.cs.d402f13.utilities.scopechecker.Member;
import dk.aau.cs.d402f13.utilities.scopechecker.TypeSymbolInfo;

public class TypeTableCleaner {
  public ArrayList<TypeSymbolInfo> clean(ArrayList<TypeSymbolInfo> typeTable) throws ScopeError{
   addParentReferences(typeTable);          //adds references to a types parent, since only the name is kept at the moment
   typeTable = topologicalSort(typeTable);  //topological sort, so super type always appear before children, used for member propagation
   propagateMembers(typeTable);             //if a type's has a member, this member shall also be available in the derived types
   return typeTable;
  }
  void propagateMembers(ArrayList<TypeSymbolInfo> typeTable) throws ScopeError{
    for (TypeSymbolInfo tsi : typeTable){
       if (tsi.parent != null){
         propagateAbstractMembers(tsi); //if parent has an abstract member, give it to this type as well + check for errors
         propagateConcreteMembers(tsi); //if parent has an concrete member, give it to this type as well + check for errors
       }
    }
  }
  void propagateAbstractMembers(TypeSymbolInfo tsi) throws ScopeError{
    for (Member pm : tsi.parent.abstractMembers){
      Boolean overridden = false;
      for (Member m : tsi.concreteMembers){
        if (m.name.equals(pm.name)){
         if (pm.args != m.args){
          throw new ScopeError("Number of arguments does not match overloaded member " + m.name, tsi);
        }
         else{
           overridden = true;
         }
      }
    }
    if (!overridden)
      tsi.addAbstractMember(pm); //propagate the parents member to this type
    }
  }
  void propagateConcreteMembers(TypeSymbolInfo tsi) throws ScopeError{
    for (Member pm : tsi.parent.concreteMembers){
      Boolean overridden = false;
      for (Member m : tsi.concreteMembers){
        if (m.name.equals(pm.name)){
         if (pm.args != m.args){
          throw new ScopeError("Number of arguments does not match overloaded member " + m.name, tsi);
        }
         else{
           overridden = true;
         }
      }
    }
    if (!overridden)
      tsi.addConcreteMember(pm); //propagate the parents member to this type
    }
  }
  ArrayList<TypeSymbolInfo> topologicalSort(ArrayList<TypeSymbolInfo> typeTable) throws ScopeError{
    ArrayList<TypeSymbolInfo> sorted = new ArrayList<TypeSymbolInfo>();
    for (TypeSymbolInfo tsi : typeTable){
    if (tsi.parent != null)
      tsi.parent.children++;
    }
    for (TypeSymbolInfo tsi: typeTable){
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
    return sorted;
  }
  void addParentReferences(ArrayList<TypeSymbolInfo> typeTable) throws ScopeError{
    //this name must be looked up in the SymbolTable to update the real reference TypeInfo t.parent
    for (TypeSymbolInfo tsi : typeTable){
      if (tsi.parentName.equals(tsi.name)){ //type cannot extend itself
        throw new ScopeError("Type cannot extend itself", tsi);
      }
      if (tsi.parentName != ""){
      Boolean foundParentType = false;
      for (TypeSymbolInfo ti2 : typeTable){
        if (tsi.parentName.equals(ti2.name)){
          tsi.parent = ti2;
          foundParentType = true;
          break;
        }
      }
      if (!foundParentType)
        throw new ScopeError("Type extends undefined type", tsi);
      }
    }
  }
  public void markAbstractTypes(ArrayList<TypeSymbolInfo> typeTable) {
    //if a type contains any abstract member (also propagated members), it must be marked as abstract for use in interpreter
    for (TypeSymbolInfo tsi : typeTable){
      if (tsi.abstractMembers.size() != 0)
        tsi.markASTnodeAsAbstract();
    }
  }
}
