package dk.aau.cs.d402f13.utilities.scopechecker;

public class Member{
  public String name;
  public int line, offset;
  public boolean abstrct;
  
  public Member(String name){
    //Only name is used for doing an isEqual check
    this.name = name;
    this.line = -1;  //if no line or offset is provided, it is just treated as standard environment
    this.offset = 0;
  }
  
  public Member(String name, int line, int offset){
    //For a data member, it is not needed to know in which type it was declared
    //since data members are not propagated to subtypes
    this(name);
    this.line = line;
    this.offset = offset;
  }
  
  public Member(String name, Boolean abstrct, int line, int offset){
    //For a data member, it is not needed to know in which type it was declared
    //since data members are not propagated to subtypes
    this(name, line, offset);
    this.abstrct = true;
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
      if (!(obj instanceof Member))
          return false;
  
      Member other = (Member) obj;
      if (other.name.equals(this.name))
        return true;
      return false;
  }
  
}