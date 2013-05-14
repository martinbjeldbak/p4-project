package dk.aau.cs.d402f13.evaluation;

import java.util.ArrayList;
import java.util.HashSet;

public class DFA {
  public State StartState;
  public ArrayList<State> States = new ArrayList<State>();
  public ArrayList<State> AcceptStates = new ArrayList<State>();
  public ArrayList<Transition> Transitions = new ArrayList<Transition>();
  
  public DFA(NFA nfa){
    
  }
  HashSet<State> epsilonClosure(State s){
    HashSet<State> closure = new HashSet<State>();
    closure.add(s);
    return closure;
  }
  
  Boolean hashSetEquals(HashSet<State> set1, HashSet<State> set2){
    if (set1.size() != set2.size())
      return false;
    for (State s1 : set1){
      if (!set2.contains(s1))
        return false;
    }
    return true;
  }
  
}
