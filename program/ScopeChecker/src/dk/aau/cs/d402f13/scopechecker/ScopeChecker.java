package dk.aau.cs.d402f13.scopechecker;

import java.util.ArrayList;
import java.util.HashMap;

import dk.aau.cs.d402f13.utilities.ast.AstNode;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.scopechecker.TypeSymbolInfo;

public class ScopeChecker {
  HashMap<String, TypeSymbolInfo> typeHashMap = new HashMap<String, TypeSymbolInfo>();
  ArrayList<TypeSymbolInfo> typeList = new ArrayList<TypeSymbolInfo>();
  //The HashMap and ArrayList contains the same elements
  //Some times it is nice to have a hashmap for quick lookup on type names
  //In other situations it is nice to have types in a list, for topological sorting and iteration.
  
  public void visit(AstNode node) throws StandardError{
    //Find types, their members, constructor and super constructor call
    TypeVisitor typeVisitor = new TypeVisitor(); 
    typeVisitor.visit(node);
    typeHashMap = typeVisitor.getTypeTable();
    for (TypeSymbolInfo tsi : typeHashMap.values())
      typeList.add(tsi);
    
    
    //Make reference from types to their parents, propogate type members to derived types, topological sort types
    TypeTableCleaner ttc = new TypeTableCleaner();
    typeList = ttc.clean(typeHashMap);
    
    //Any type containing at least one abstract member is marked as abstract type for use in interpreter
    ttc.markAbstractTypes(typeList); 
    
    //Check that a call to a parent type constructor has the right number of arguments
    TypeSuperCallChecker teec = new TypeSuperCallChecker();
    teec.check(typeList);
    
    //Check that every variable, constant and function used are declared in correct scopes
    UsesAreDeclaredVisitor uadv = new UsesAreDeclaredVisitor(typeHashMap);
    uadv.visit(node);
    
    //Check that members are accessed correct. E.g. in:  $a.b.g.k  member b, g and k must exist in some type
    //TypeMemberAccessVisitor tmav = new TypeMemberAccessVisitor();
    //tmav.setTypeTable(typeHashMap);
    //tmav.visit(node); not tested yet
    
    TypeTablePrettyPrinter.print(typeList);
  }
}
