package dk.aau.cs.d402f13.scopechecker;

import dk.aau.cs.d402f13.utilities.ast.AstNode;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.scopechecker.TypeTable;

public class ScopeChecker {
  public void visit(AstNode node) throws StandardError{
    
    TypeTable tt = new TypeTable();
    
    //Insert the standard environment types and their members into TypeTable
    StandardEnvironment.insertInto(tt);
    
    //Find types, their members, constructor and super constructor call
    TypeVisitor typeVisitor = new TypeVisitor();
    typeVisitor.setTypeTable(tt);
    typeVisitor.visit(node);
    
    //Make reference from types to their parents, 
    //propogate type members to derived types, topological sort types
    //and mark types containing at least 1 abstract member as an abstract type AstNode
    TypeTableCleaner ttc = new TypeTableCleaner();
    ttc.clean(tt);
 
    //Check that a call to a parent type constructor has the right number of arguments
    TypeSuperCallChecker teec = new TypeSuperCallChecker();
    teec.check(tt);
    
    //Check that every variable, constant and function used are declared in correct scopes
    UsesAreDeclaredVisitor uadv = new UsesAreDeclaredVisitor(tt);
    uadv.visit(node);
    
   
    
    TypeTablePrettyPrinter.print(tt);
  }
}
