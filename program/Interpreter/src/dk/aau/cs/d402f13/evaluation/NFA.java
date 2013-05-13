package dk.aau.cs.d402f13.evaluation;

import java.util.ArrayList;

public class NFA {
  private State startState;
  private ArrayList<State> states = new ArrayList<State>();
  private ArrayList<State> acceptStates = new ArrayList<State>();
  private ArrayList<Transition> transitions = new ArrayList<Transition>();
  
  public NFA(State startState, ArrayList<State> states, ArrayList<State> acceptStates, ArrayList<Transition> transitions){
    this.startState = startState;
    this.states = states;
    this.acceptStates = acceptStates;
    this.transitions = transitions;
  }
  
  public void not(){
    ArrayList<State> newAccept = new ArrayList<State>();
    for (State s : this.states){
      if (!this.acceptStates.contains(s))
        newAccept.add(s);
    }
    this.acceptStates = newAccept;
  }

  public void concat(NFA other) {
    for(State s : this.acceptStates) {
      this.transitions.add(new Transition(s, other.startState, null));
    }

    this.states.addAll(other.states);
    this.transitions.addAll(other.transitions);
    this.acceptStates = other.acceptStates;
  }

  public void kleeneStar(){
    State newStart = new State();
    this.transitions.add(new Transition(newStart, this.startState, null));

    for (State s : this.acceptStates)
      this.transitions.add(new Transition(s, this.startState, null));

    this.startState = newStart;
    this.acceptStates.add(newStart);
  }

  public void union(NFA other){
    State newStart = new State();

    this.states.addAll(other.states);
    this.acceptStates.addAll(other.acceptStates);
    
    this.transitions.add(new Transition(newStart, this.startState, null));
    this.transitions.add(new Transition(newStart, other.startState, null));
    
    this.startState = newStart;
  }
}
