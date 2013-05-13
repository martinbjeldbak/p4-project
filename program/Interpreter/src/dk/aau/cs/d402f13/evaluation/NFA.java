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

  /**
   * Runs the not operation (!) on the current NFA, updating
   * its accept states.
   */
  public void not(){
    ArrayList<State> newAccept = new ArrayList<State>();
    for (State s : this.states){
      if (!this.acceptStates.contains(s))
        newAccept.add(s);
    }
    this.acceptStates = newAccept;
  }

  /**
   * Concatenates the current NFA with the NFA supplied as parameter.
   * @param other the other NFA to be concatenated with
   */
  public void concat(NFA other) {
    for(State s : this.acceptStates) {
      this.transitions.add(new Transition(s, other.startState, null));
    }

    this.states.addAll(other.states);
    this.transitions.addAll(other.transitions);
    this.acceptStates = other.acceptStates;
  }

  /**
   * Runs the kleene star operator (*) on the NFA, updating its start state,
   * accept state, and adds epsilon-transitions.
   */
  public void kleeneStar(){
    State newStart = new State();
    this.transitions.add(new Transition(newStart, this.startState, null));

    for (State s : this.acceptStates)
      this.transitions.add(new Transition(s, this.startState, null));

    this.startState = newStart;
    this.acceptStates.add(newStart);
  }

  /**
   * The union/or operation. Adds a new start state and creates epsilon
   * transitions from that start state to the current NFA and the NFA
   * supplied as parameter.
   * @param other the other NFA in the union
   */
  public void union(NFA other){
    State newStart = new State();

    this.transitions.add(new Transition(newStart, this.startState, null));
    this.transitions.add(new Transition(newStart, other.startState, null));

    this.states.addAll(other.states);
    this.acceptStates.addAll(other.acceptStates);

    this.startState = newStart;
  }
}
