package dk.aau.cs.d402f13.evaluation;

import java.util.HashSet;

public class DFAState extends State {
 
  HashSet<NFAState> states;
  public DFAState(HashSet<NFAState> states){
    this.states = states;
  }
  
  @Override
  public String getName(){
    StringBuilder sb = new StringBuilder();
    String prefix = "";
    for (NFAState s : states){
      sb.append(prefix);
      sb.append(s.getName());
      prefix = ",";
    }
    return sb.toString();
  }
  
  public DFAState(){
    this.states = new HashSet<NFAState>();
  }
  
  public Boolean contains(State s){
    return this.states.contains(s);
  }
  public void add(NFAState s){
    this.states.add(s);
  }
  
  @Override
  public int hashCode() {
    int hash = 0;
    for (NFAState s : this.states)
      hash ^= s.hashCode();
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
      if (obj == null)
          return false;
      if (obj == this)
          return true;
      if (obj.getClass() != getClass())
          return false;
  
      DFAState other = (DFAState)obj;
     if (this.states.size() != other.states.size())
       return false;
     for (NFAState s : this.states){
       if (!other.contains(s))
         return false;
     }
      return true;
  }
  
}
