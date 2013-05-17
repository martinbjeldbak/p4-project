package dk.aau.cs.d402f13.evaluation;

import java.util.ArrayList;
import java.util.HashSet;

import dk.aau.cs.d402f13.utilities.SimpleDir;
import dk.aau.cs.d402f13.utilities.Tuple;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.values.DirValue;
import dk.aau.cs.d402f13.values.PatternKeyValue;
import dk.aau.cs.d402f13.values.Value;



public class DirSeq { 
  
  SimpleDir offset;
  ArrayList<PatternCheckVal> vals = new ArrayList<PatternCheckVal>();
  public DirSeq(){
    this.offset = new SimpleDir(0,0);
  }
  public DirSeq(int x, int y){
    this.offset = new SimpleDir(x,y);
  }
  public void addOffset(int x, int y){
    this.offset.x += x;
    this.offset.y += y;
  }
 public DirSeq(DirSeq left, DirSeq right) throws StandardError{
    for (PatternCheckVal v : left.vals)
      vals.add(v);
    for (PatternCheckVal v : right.vals)
      vals.add(v);
  }
 public DirSeq makeClone() throws StandardError{
   DirSeq result = new DirSeq(this.offset.x, this.offset.y);
   for (PatternCheckVal v : this.vals){
     result.add(v.clone());
   }
   return result;
 }
  public void add(PatternCheckVal v) throws StandardError{
        vals.add(v); 
  }
  public void addKeyVal(String val){
    this.vals.add(new PatternCheckVal(offset.clone(), val));
  }
  public boolean checkPatternFromPos(SimpleDir startPos){
    return true;
  }
  
  public void print(){  
    for (PatternCheckVal v : vals){
       v.print();
    }
    System.out.println("");
  }
  @Override
  public int hashCode() {
    return this.vals.size() + this.offset.x * 31 + this.offset.y * 117;
  }

  @Override
  public boolean equals(Object obj) {
      if (obj == null)
          return false;
      if (obj == this)
          return true;
      if (obj.getClass() != getClass())
          return false;
  
      DirSeq other = (DirSeq) obj;
      if (this.vals.size() != other.vals.size())
        return false;
      if (this.offset.x != other.offset.x || this.offset.y != other.offset.y)
        return false;
      for (int i = 0; i < this.vals.size(); i++){
        if (!this.vals.get(i).equals(other.vals.get(i)))
          return false;
      }
      return true;
  }

}
