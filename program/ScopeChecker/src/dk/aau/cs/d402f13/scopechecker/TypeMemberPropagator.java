package dk.aau.cs.d402f13.scopechecker;
import dk.aau.cs.d402f13.utilities.errors.ScopeError;
import dk.aau.cs.d402f13.utilities.scopechecker.Member;
import dk.aau.cs.d402f13.utilities.scopechecker.TypeSymbolInfo;
import dk.aau.cs.d402f13.utilities.scopechecker.TypeTable;

public class TypeMemberPropagator {
  TypeTable tt;
  public TypeMemberPropagator(TypeTable tt) throws ScopeError{
    this.tt = tt;
  }
  void propagateMembers() throws ScopeError{
    //ensure that types are topological sorted
    //such that a type always appear before all of its subtypes
    tt.topologicalSort();  
    for (TypeSymbolInfo tsi : this.tt){
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
}
