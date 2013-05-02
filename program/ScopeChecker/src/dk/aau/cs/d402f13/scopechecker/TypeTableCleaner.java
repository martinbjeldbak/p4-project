package dk.aau.cs.d402f13.scopechecker;
import dk.aau.cs.d402f13.utilities.errors.ScopeError;
import dk.aau.cs.d402f13.utilities.scopechecker.Member;
import dk.aau.cs.d402f13.utilities.scopechecker.TypeSymbolInfo;
import dk.aau.cs.d402f13.utilities.scopechecker.TypeTable;

public class TypeTableCleaner {
  public void clean(TypeTable tt) throws ScopeError{
   addParentReferences(tt);          //adds references to a types parent, since only the name is kept at the moment
   tt.topologicalSort();            //checks for extend cycles, so super type always appear before children, used for member propagation
   propagateMembers(tt);            //if a type's has a member, this member shall also be available in the derived types
   markAbstractTypes(tt);
  }
  void propagateMembers(TypeTable tt) throws ScopeError{
    for (TypeSymbolInfo tsi : tt){
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

  void addParentReferences(TypeTable tt) throws ScopeError{
    //this name must be looked up in the SymbolTable to update the real reference TypeInfo t.parent
    for (TypeSymbolInfo tsi : tt){
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
  void markAbstractTypes(TypeTable tt) {
    //if a type contains any abstract member (also propagated members), it must be marked as abstract for use in interpreter
    for (TypeSymbolInfo tsi : tt){
     for (Member m : tsi.members){
       if (m.abstrct){
        tsi.markASTnodeAsAbstract();
        continue;
       }
     }
    }
  }
}
