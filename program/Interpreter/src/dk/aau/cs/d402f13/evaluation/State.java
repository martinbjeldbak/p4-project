package dk.aau.cs.d402f13.evaluation;

/*
 * This clase only servers the purpose that both 
 * NFAState and DFAState derives from it.
 * Thereby, a transitions can be a triple
 * (State from, State to, Value val)
 * and be used by both NFA's and DFA's
 */
public class State {
  String name;
  public String getName(){
    return this.name;
  }
}
