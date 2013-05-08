package dk.aau.cs.d402f13.utilities.scopechecker;

public class FunctionMember extends Member{
 
  int argCount;
  
  public FunctionMember(String name){
  super(name);  
  }
  public FunctionMember(String name, int argCount){
    super(name);
    this.argCount = argCount;
    }
  public FunctionMember(String name, int line, int offset){
    super(name, line, offset);  
  }
  public FunctionMember(String name, int argCount, int line, int offset){
    super(name, line, offset);  
    this.argCount = argCount;
  }
  public FunctionMember(String name, Boolean abstrct, int line, int offset){
    super(name, abstrct, line, offset);  
  }
  public FunctionMember(String name, int argCount, Boolean abstrct, int line, int offset){
    super(name, abstrct, line, offset);  
    this.argCount = argCount;
  }
  
  public int argCount()
  {
  return this.argCount;
  }
}