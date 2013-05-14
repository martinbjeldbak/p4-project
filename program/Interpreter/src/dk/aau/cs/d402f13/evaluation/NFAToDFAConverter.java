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
  private HashMap<NFAState, HashSet<Transition>> transitionsFromStates;
  
  NFA nfa;
  
  public DFA ToDFA(NFA nfa){  
    //algorithm used: http://web.cecs.pdx.edu/~harry/compilers/slides/LexicalPart3.pdf
    this.nfa = nfa;
    initTransitionsFromStates();
    
    Stack<DFAState> stack = new Stack<DFAState>();
    DFAState startState = epsilonClosure(nfa.startState);
    stack.add(startState);
    
    while(!stack.isEmpty()){
      DFAState temp = stack.pop();
      for (NFAState ns : temp.states){
        DFAState closure = epsilonClosure(ns);
        if (!this.states.contains(closure)){
          this.states.add(closure);
          stack.push(closure);
        }
      }
    }
      
    this.states.add(startState);
    return new DFA(this.startState, this.states, this.acceptStates, this.transitions);  
  }
  
  /*
   * Returns the set of transitions starting from the given state
   */
  public void initTransitionsFromStates(){
    this.transitionsFromStates = new HashMap<NFAState, HashSet<Transition>>();
    for (NFAState s : nfa.states){
      this.transitionsFromStates.put(s, new HashSet<Transition>());
    }
    for (Transition t : nfa.transitions){
      this.transitionsFromStates.get(t.from).add(t);
    }
  };
  
  /*
   * Returns the set of states that can be reached from the
   * input state, using only epsilon-transitions
   */
    private DFAState epsilonClosure(NFAState s){
    DFAState closure = new DFAState();
    Stack<NFAState> stack = new Stack<NFAState>();
    
    stack.add(s);
    while (!stack.isEmpty()){
      NFAState temp = stack.pop();
      closure.add(temp);
      for (Transition t : this.transitionsFromStates.get(temp)){
        if (t.val == null){ //epsilon-transition
          if (!closure.contains(t.to) && !stack.contains(t.to))
            stack.add((NFAState)t.to);
        }
      }
    }
    return closure;
  }
    
   
   
}
