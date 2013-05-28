package dk.aau.cs.d402f13.utilities.scopechecker;

public class Data{
  public String name;
  public TypeSymbolInfo declaredInType;
  public int line, offset;
  //if a member is put in a type because the member existed in the parent 
  //type, propagated must be true
  
  public Data(String name){
    //Only name is used for doing an isEqual check
    this.name = name;
  }
  
  public Data(String name, int line, int offset){
    //For a data member, it is not needed to know in which type it was declared
    //since data members are not propagated to subtypes
    this(name);
    this.line = line;
    this.offset = offset;
  }
  public Data(String name, TypeSymbolInfo declaredInType, int line, int offset){
    this(name, line, offset);
    this.declaredInType = declaredInType;
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
  
      Data other = (Data) obj;
      if (other.name.equals(this.name))
        return true;
      return false;
  }
  
}