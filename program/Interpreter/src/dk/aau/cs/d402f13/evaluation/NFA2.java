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
  private State startState;
  private ArrayList<State> states = new ArrayList<State>();
  private ArrayList<State> acceptStates = new ArrayList<State>();
  private ArrayList<Transition> transitions = new ArrayList<Transition>();
  
  public NFA2(State startState, ArrayList<State> states, ArrayList<State> acceptStates, ArrayList<Transition> transitions){
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
  public void kleeneStar(){
    State newStart = new State();
    this.transitions.add(new Transition(newStart, this.startState, null));
    this.startState = newStart;
    for (State s : this.acceptStates)
      this.transitions.add(new Transition(s, newStart, null));
    this.acceptStates.add(newStart);
  }
  public void union(NFA2 other){
    State newStart = new State();
    for (State s : other.states)
      this.states.add(s);
    for (State s : other.acceptStates)
      this.acceptStates.add(s);
    
    this.transitions.add(new Transition(newStart, this.startState, null));
    this.transitions.add(new Transition(newStart, other.startState, null));
    
    this.startState = newStart;
  }
}
