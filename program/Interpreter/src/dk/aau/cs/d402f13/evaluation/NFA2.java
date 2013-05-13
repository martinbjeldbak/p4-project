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
  
  public NFA2(State startState, ArrayList<State2> states, ArrayList<State2> acceptStates, ArrayList<Transition> transitions){
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

  public void concat(NFA2 nfa2) {
    for(State2 s : this.acceptStates) {
      this.transitions.add(new Transition(s, nfa2.startState, null));
    }

    this.states.addAll(nfa2.states);
    this.transitions.addAll(nfa2.transitions);
    this.acceptStates = nfa2.acceptStates;
  }
 
}
