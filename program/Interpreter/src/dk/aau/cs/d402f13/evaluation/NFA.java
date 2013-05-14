package dk.aau.cs.d402f13.evaluation;

import dk.aau.cs.d402f13.values.Value;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class NFA {
  private State startState;
  private ArrayList<State> states = new ArrayList<State>();
  private ArrayList<State> acceptStates = new ArrayList<State>();
  private ArrayList<Transition> transitions = new ArrayList<Transition>();
  
  private NFA(State startState, ArrayList<State> states, ArrayList<State> acceptStates, ArrayList<Transition> transitions){
    this.startState = startState;
    this.states = states;
    this.acceptStates = acceptStates;
    this.transitions = transitions;
  }

  public NFA() {
    /*
    // Should be like on page 6 where R = Ø
    //http://courses.engr.illinois.edu/cs373/sp2009/lectures/lect_06.pdf

    State state = new State();

    this.startState = state;
    this.states.add(state);
    this.transitions.add(new Transition(null, state, null));
    */

    State start = new State();
    State accept = new State();

    this.states.add(start);
    this.states.add(accept);

    this.startState = start;
    this.acceptStates.add(accept);

    this.transitions.add(new Transition(start, accept, null));
  }

  public NFA(Value v) {
    State start = new State();
    State accept = new State();

    this.states.add(start);
    this.states.add(accept);

    this.startState = start;
    this.acceptStates.add(accept);

    this.transitions.add(new Transition(start, accept, v));
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
  public void kleeneStar() {
    State newStart = new State();
    this.states.add(newStart);

    this.transitions.add(new Transition(newStart, this.startState, null));

    for (State s : this.acceptStates)
      this.transitions.add(new Transition(s, this.startState, null));

    this.startState = newStart;
    this.acceptStates.add(newStart);
  }

  /**
   * The plus '+' operator. Transforms the NFA to run one-or-more times
   * before accepting.
   */
  public void plus() {
    State newStart = new State();
    this.states.add(newStart);

    this.transitions.add(new Transition(newStart, this.startState, null));

    for (State s : this.acceptStates)
      this.transitions.add(new Transition(s, this.startState, null));

    this.startState = newStart;
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
    this.transitions.addAll(other.transitions);

    this.states.add(newStart);
    this.states.addAll(other.states);
    this.acceptStates.addAll(other.acceptStates);

    this.startState = newStart;
  }

  public void optional() {
    State newState = new State();

    this.states.add(newState);
    this.transitions.add(new Transition(newState, this.startState, null));

    this.acceptStates.add(newState);
    this.startState = newState;
  }

  public void toDot() {
    Path file = createFile("NFA.dot");

    try(BufferedWriter writer = Files.newBufferedWriter(file, Charset.defaultCharset())) {
      writeLine("digraph NFA {", writer);

      //writeLine("rankdir=LR;", writer);
      writeLine("  node[shape = circle];", writer);

      // Print out the label for each state
      for(int i = 0; i < this.states.size(); i++) {
        State s = this.states.get(i);

        if(this.acceptStates.contains(s))
          writeLine("  " + s.hashCode() + label("" + i) + " [shape = doublecircle]" + ";" , writer);
        else
          writeLine("  " + s.hashCode() + label("" + i) + ";", writer);
      }

      writeLine("", writer);

      // For every transition
      for(Transition tra : this.transitions) {
        State from = tra.from;
        State to = tra.to;
        Value v = tra.val;

        if(v == null)
          writeLine("  " + from.hashCode() + " -> " + to.hashCode() + label("&#949;") + ";", writer);
        else
          writeLine("  " + from.hashCode() + " -> " + to.hashCode() + label(v.toString()) + ";", writer);
      }

      writeLine("}", writer);
      writer.close();
    }
    catch(IOException ex) {
      System.out.println("Error writing to file");
    }
  }

  private Path createFile(String fileName) {
    Path file = Paths.get(fileName);
    try {
      Files.deleteIfExists(file);
      file = Files.createFile(file);
    } catch (IOException e) {
      System.out.println("Error creating file");
    }
    return file;
  }

  private void writeLine(CharSequence s, BufferedWriter writer) {
    try {
      writer.append(s);
      writer.newLine();
    } catch (IOException e) {
      System.out.print("Failed to write line");
    }
  }

  private String label(String label) {
    return " [label=\"" + label + "\"]";
  }
}
