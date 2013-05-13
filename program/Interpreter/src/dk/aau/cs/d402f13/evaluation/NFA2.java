package dk.aau.cs.d402f13.evaluation;

import dk.aau.cs.d402f13.values.Value;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

public class NFA2 {
  private State2 startState;
  private ArrayList<State2> states = new ArrayList<State2>();
  private ArrayList<State2> acceptStates = new ArrayList<State2>();
  private ArrayList<Transition> transitions = new ArrayList<Transition>();
  
  public NFA2(State2 startState, ArrayList<State2> states, ArrayList<State2> acceptStates, ArrayList<Transition> transitions){
    this.startState = startState;
    this.states = states;
    this.acceptStates = acceptStates;
    this.transitions = transitions;
  }
  
  public void not(){
    ArrayList<State2> newAccept = new ArrayList<State2>();
    for (State2 s : this.states){
      if (!this.acceptStates.contains(s))
        newAccept.add(s);
    }
    this.acceptStates = newAccept;
  }

  public void concat(NFA2 other) {
    for(State2 s : this.acceptStates) {
      this.transitions.add(new Transition(s, other.startState, null));
    }

    this.states.addAll(other.states);
    this.transitions.addAll(other.transitions);
    this.acceptStates = other.acceptStates;
  }

  public void kleeneStar(){
    State2 newStart = new State2();
    this.transitions.add(new Transition(newStart, this.startState, null));

    for (State2 s : this.acceptStates)
      this.transitions.add(new Transition(s, this.startState, null));

    this.startState = newStart;
    this.acceptStates.add(newStart);
  }

  public void union(NFA2 other){
    State2 newStart = new State2();

    this.states.addAll(other.states);
    this.acceptStates.addAll(other.acceptStates);
    
    this.transitions.add(new Transition(newStart, this.startState, null));
    this.transitions.add(new Transition(newStart, other.startState, null));
    
    this.startState = newStart;
  }
}
