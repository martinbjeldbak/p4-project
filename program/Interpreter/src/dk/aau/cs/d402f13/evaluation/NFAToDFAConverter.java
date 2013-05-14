package dk.aau.cs.d402f13.evaluation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

public class NFAToDFAConverter {
 
  private DFAState startState;
  private ArrayList<DFAState> states = new ArrayList<DFAState>();
  private ArrayList<DFAState> acceptStates = new ArrayList<DFAState>();
  private ArrayList<Transition> transitions = new ArrayList<Transition>();
 
  //Used to quickly find the transitions from a given state
  private HashMap<State, HashSet<Transition>> transitionsFromStates;
  
  public DFA ToDFA(NFA nfa){   
    initTransitionsFromStates();
    
    DFAState startState = epsilonClosure(nfa.startState);
    return new DFA(startState, states, acceptStates, transitions);  
  }
  
  /*
   * Returns the set of transitions starting from the given state
   */
  public void initTransitionsFromStates(){
    this.transitionsFromStates = new HashMap<State, HashSet<Transition>>();
    for (State s : this.states){
      this.transitionsFromStates.put(s, new HashSet<Transition>());
    }
    for (Transition t : this.transitions){
      this.transitionsFromStates.get(t.from).add(t);
    }
  };
  
  /*
   * Returns the set of states that can be reached from the
   * input state, using only epsilon-transitions
   */
    private DFAState epsilonClosure(NFAState s){
    DFAState closure = new DFAState();
    Stack<NFAState> queue = new Stack<NFAState>();
    
    queue.add(s);
    while (!queue.isEmpty()){
      NFAState temp = queue.pop();
      closure.add(temp);
      for (Transition t : this.transitionsFromStates.get(temp)){
        if (t.val == null){ //epsilon-transition
          if (!closure.contains(t.to) && !queue.contains(t.to))
            queue.add((NFAState)t.to);
        }
      }
    }
    return closure;
  }
  
   
}
