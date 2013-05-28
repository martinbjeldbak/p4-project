package dk.aau.cs.d402f13.utilities.scopechecker;

public class ConstantMember extends Member{
 
  
  public ConstantMember(String name){
  super(name);  
  }
  public ConstantMember(String name, int line, int offset){
    super(name, line, offset);  
  }
  public ConstantMember(String name, Boolean abstrct, int line, int offset){
    super(name, abstrct, line, offset);  
    }
  
}