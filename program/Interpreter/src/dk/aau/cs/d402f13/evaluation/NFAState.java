package dk.aau.cs.d402f13.evaluation;

import dk.aau.cs.d402f13.values.PatternValue;
import dk.aau.cs.d402f13.values.Value;

import java.util.*;
import java.util.ArrayList;

public class NFAState extends State {
  static int nextId = 0; //id used for printing dot files for nicely comparing NFA to equivalent DFA
  public NFAState(){
    this.name = ""+nextId;
    nextId++;
  }
}
