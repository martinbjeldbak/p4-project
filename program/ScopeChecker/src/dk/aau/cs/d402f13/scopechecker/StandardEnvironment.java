package dk.aau.cs.d402f13.scopechecker;

import dk.aau.cs.d402f13.utilities.errors.ScopeError;
import dk.aau.cs.d402f13.utilities.scopechecker.Member;
import dk.aau.cs.d402f13.utilities.scopechecker.TypeSymbolInfo;
import dk.aau.cs.d402f13.utilities.scopechecker.TypeTable;

public class StandardEnvironment {
  public static void insertInto(TypeTable tt) throws ScopeError{
    
    TypeSymbolInfo current;
    
    //Boolean
    current = new TypeSymbolInfo(null, "Boolean", -1, 0);
    current.args = 1;
    tt.addType(current);
    
    //Coordinate
    current = new TypeSymbolInfo(null, "Coordinate", -1, 0);
    current.args = 1;
     tt.addType(current);
    
    //Direction
    current = new TypeSymbolInfo(null, "Direction", -1, 0);
    current.args = 1;
     tt.addType(current);
    
    //Function
    current = new TypeSymbolInfo(null, "Function", -1, 0);
    current.args = 1;
    current.members.add(new Member("call", 0, -1, 0));
     tt.addType(current);
    
    //Integer
    current = new TypeSymbolInfo(null, "Integer", -1, 0);
    current.args = 1;
     tt.addType(current);
    
    //List
    current = new TypeSymbolInfo(null, "List", -1, 0);
    current.args = 1;
    current.members.add(new Member("size", 0, -1, 0));
    current.members.add(new Member("sort", 0, -1, 0));
    current.members.add(new Member("map", 0, -1, 0));
    current.members.add(new Member("filter", 0, -1, 0));
     tt.addType(current);
    
    //Pattern
    current = new TypeSymbolInfo(null, "Pattern", -1, 0);
    current.args = 1;
    current.members.add(new Member("size"));
     tt.addType(current);
    
    //String
    current = new TypeSymbolInfo(null, "String", -1, 0);
    current.args = 1;
     tt.addType(current);
    
    //Type
    current = new TypeSymbolInfo(null, "Type", -1, 0);
    current.args = 1;
    current.members.add(new Member("isSubtypeOf", 0, -1, 0));
    current.members.add(new Member("isSupertypeOf", 0, -1, 0));
     tt.addType(current);
    
    //GLOBAL FUNCTIONS
     tt.getGlobal().addMember(new Member("typeOf", 0, -1, 0));
     tt.getGlobal().addMember(new Member("union", 0, -1, 0));
  }
}
