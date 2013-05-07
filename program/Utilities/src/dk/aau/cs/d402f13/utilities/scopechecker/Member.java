package dk.aau.cs.d402f13.utilities.scopechecker;

public class Member{
  public int args;
  public String name;
  public int line, offset;
  public Boolean abstrct;
 
  //if a member is put in a type because the member existed in the parent 
  //type, propagated must be true
  
  public Member(String name){
    //Only name is used for doing an isEqual check
    this.name = name;
    this.abstrct = false;
    this.args = -1; //constant as default x > -1 means function with x arguments
  }
  
  public Member(String name, int line, int offset){
    //For a data member, it is not needed to know in which type it was declared
    //since data members are not propagated to subtypes
    this(name);
    this.line = line;
    this.offset = offset;
  }
  public Member(String name, int args, int line, int offset){
    this(name,line, offset);
    this.args = args;
  }
  public void IncrArg(){
    this.args++;
  }
  
  //override equals and hashcode so a an ArrayList of members can be searched to contain a member
  @Override
  public int hashCode() {
    return this.name.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
      if (obj == null)
          return false;
      if (obj == this)
          return true;
      if (obj.getClass() != getClass())
          return false;
  
      Member other = (Member) obj;
      if (other.name.equals(this.name))
        return true;
      return false;
  }
  
}