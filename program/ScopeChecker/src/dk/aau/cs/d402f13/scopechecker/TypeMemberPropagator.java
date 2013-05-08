package dk.aau.cs.d402f13.scopechecker;
import dk.aau.cs.d402f13.utilities.errors.ScopeError;
import dk.aau.cs.d402f13.utilities.scopechecker.ConstantMember;
import dk.aau.cs.d402f13.utilities.scopechecker.FunctionMember;
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
               if (m instanceof ConstantMember && mp instanceof ConstantMember){ //if one member is function and another is constant
                   continue;
               }
               else if (m instanceof FunctionMember && mp instanceof FunctionMember){
                 if (((FunctionMember)m).argCount() != ((FunctionMember)mp).argCount())  
                   throw new ScopeError("Number of arguments for member " + m.name + " in type " + tsi.name + " does not match. " +
                                        "Expected " + ((FunctionMember)mp).argCount() + ", " +
                                        "received " + ((FunctionMember)m).argCount(), m.line, m.offset);
               }
               else if (m instanceof FunctionMember && mp instanceof ConstantMember){
                 throw new ScopeError("Expected member " + m.name + " to be a constant, not a function, in type " + 
                     tsi.name, m.line, m.offset);
               }
               else if (m instanceof ConstantMember && mp instanceof FunctionMember){
                 throw new ScopeError("Expected member " + m.name + " to be a function, not a constant, in type " + 
                     tsi.name, m.line, m.offset);
               }
               break; //if a member with the given name exists and no errors are found, no other
                      //errors can exists for that member, since all member names are unique
             }
           }
           if (!found) //add the member to a type from its parent if it is not present in the type already
             tsi.addMember(mp);
         }
       }
    }
  }
}
