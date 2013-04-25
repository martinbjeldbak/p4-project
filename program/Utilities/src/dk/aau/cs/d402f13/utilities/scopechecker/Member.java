package dk.aau.cs.d402f13.utilities.scopechecker;

public class Member{
  public int args;
  public String name;
  public TypeSymbolInfo declaredInType;
  //if a member is put in a type because the member existed in the parent 
  //type, propagated must be true
  public Member(String name, TypeSymbolInfo declaredInType){
    this.name = name;
    this.declaredInType = declaredInType;
  }
  public Member(String name, int args, TypeSymbolInfo declaredInType){
    this(name, declaredInType);
    this.args = args;
  }
  public void IncrArg(){
    this.args++;
  }
}