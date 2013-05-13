package dk.aau.cs.d402f13.scopechecker;

import dk.aau.cs.d402f13.utilities.errors.ScopeError;
import dk.aau.cs.d402f13.utilities.scopechecker.ConstantMember;
import dk.aau.cs.d402f13.utilities.scopechecker.FunctionMember;
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
    current.members.add(new FunctionMember("call", 1));
     tt.addType(current);
    
    //Integer
    current = new TypeSymbolInfo(null, "Integer", -1, 0);
    current.args = 1;
     tt.addType(current);
    
    //List
    current = new TypeSymbolInfo(null, "List", -1, 0);
    current.args = 1;
    current.members.add(new ConstantMember("size"));
    current.members.add(new FunctionMember("sort", 1));
    current.members.add(new FunctionMember("map", 1));
    current.members.add(new FunctionMember("filter", 1));
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
    current.members.add(new FunctionMember("isSubtypeOf", 1));
    current.members.add(new FunctionMember("isSupertypeOf", 1));
     tt.addType(current);
    
    //GLOBAL FUNCTIONS
     tt.getGlobal().addMember(new ConstantMember("true"));
     tt.getGlobal().addMember(new ConstantMember("false"));
     tt.getGlobal().addMember(new FunctionMember("typeOf", 1));
     FunctionMember union = new FunctionMember("union", 1);
     union.setVarArgs(true);
     tt.getGlobal().addMember(union);
  }
}