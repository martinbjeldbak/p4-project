public class TypeSuperCallChecker {
  public void check(TypeTable tt) throws ScopeError{
    for (TypeSymbolInfo tsi : tt){
      if (tsi.parent != null){
        if (tsi.parent.varArgs == true){
		  //if parent type has x args and varargs, the subtype must provide at least x args in constructor call
          if (tsi.parentCallArgs < tsi.parent.args)
            throw new ScopeError("Number of arguments does not match for call to parent type " + tsi.parent.name + " in type " + tsi.name + ". Expected a minimum of " + tsi.parent.args + ", received " + tsi.parentCallArgs+".", tsi.line, tsi.offset);
        }
        else if (tsi.parentCallArgs != tsi.parent.args){
          //if parent type has no varargs, the arg count must match exactly
          throw new ScopeError("Number of arguments does not match for call to parent type " + tsi.parent.name + " in type " + tsi.name + ". Expected " + tsi.parent.args + ", received " + tsi.parentCallArgs+".", tsi.line, tsi.offset);
        }
      }
    }  
  }
}