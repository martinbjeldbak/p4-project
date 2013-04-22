package dk.aau.cs.d402f13.utilities.scopechecker;

public class Member{
  public int args;
  public String name;
  public Member(String name){
    this.name = name;
  }
  public void IncrArg(){
    this.args++;
  }
}