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
  public State StartState;
  public ArrayList<State> States = new ArrayList<State>();
  public ArrayList<State> AcceptStates = new ArrayList<State>();
  public ArrayList<Transition> Transitions = new ArrayList<Transition>();
  
  private NFA(State startState, ArrayList<State> states, ArrayList<State> acceptStates, ArrayList<Transition> transitions){
    this.StartState = startState;
    this.States = states;
    this.AcceptStates = acceptStates;
    this.Transitions = transitions;
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

    this.States.add(start);
    this.States.add(accept);

    this.StartState = start;
    this.AcceptStates.add(accept);

    this.Transitions.add(new Transition(start, accept, null));
  }

  public NFA(Value v) {
    State start = new State();
    State accept = new State();

    this.States.add(start);
    this.States.add(accept);

    this.StartState = start;
    this.AcceptStates.add(accept);

    this.Transitions.add(new Transition(start, accept, v));
  }

  /**
   * Runs the not operation (!) on the current NFA, updating
   * its accept states.
   */
  public void not(){
    ArrayList<State> newAccept = new ArrayList<State>();
    for (State s : this.States){
      if (!this.AcceptStates.contains(s))
        newAccept.add(s);
    }
    this.AcceptStates = newAccept;
  }

  /**
   * Concatenates the current NFA with the NFA supplied as parameter.
   * @param other the other NFA to be concatenated with
   */
  public void concat(NFA other) {
    for(State s : this.AcceptStates) {
      this.Transitions.add(new Transition(s, other.StartState, null));
    }

    this.States.addAll(other.States);
    this.Transitions.addAll(other.Transitions);
    this.AcceptStates = other.AcceptStates;
  }

  /**
   * Runs the kleene star operator (*) on the NFA, updating its start state,
   * accept state, and adds epsilon-transitions.
   */
  public void kleeneStar() {
    State newStart = new State();
    this.States.add(newStart);

    this.Transitions.add(new Transition(newStart, this.StartState, null));

    for (State s : this.AcceptStates)
      this.Transitions.add(new Transition(s, this.StartState, null));

    this.StartState = newStart;
    this.AcceptStates.add(newStart);
  }

  /**
   * The plus '+' operator. Transforms the NFA to run one-or-more times
   * before accepting.
   */
  public void plus() {
    State newStart = new State();
    this.States.add(newStart);

    this.Transitions.add(new Transition(newStart, this.StartState, null));

    for (State s : this.AcceptStates)
      this.Transitions.add(new Transition(s, this.StartState, null));

    this.StartState = newStart;
  }

  /**
   * The union/or operation. Adds a new start state and creates epsilon
   * transitions from that start state to the current NFA and the NFA
   * supplied as parameter.
   * @param other the other NFA in the union
   */
  public void union(NFA other){
    State newStart = new State();

    this.Transitions.add(new Transition(newStart, this.StartState, null));
    this.Transitions.add(new Transition(newStart, other.StartState, null));
    this.Transitions.addAll(other.Transitions);

    this.States.add(newStart);
    this.States.addAll(other.States);
    this.AcceptStates.addAll(other.AcceptStates);

    this.StartState = newStart;
  }

  public void optional() {
    State newState = new State();

    this.States.add(newState);
    this.Transitions.add(new Transition(newState, this.StartState, null));

    this.AcceptStates.add(newState);
    this.StartState = newState;
  }

  public void toDot() {
    Path file = createFile("NFA.dot");

    try(BufferedWriter writer = Files.newBufferedWriter(file, Charset.defaultCharset())) {
      writeLine("digraph NFA {", writer);

      //writeLine("rankdir=LR;", writer);
      writeLine("  node[shape = circle];", writer);

      // Print out the label for each state
      for(int i = 0; i < this.States.size(); i++) {
        State s = this.States.get(i);

        if(this.AcceptStates.contains(s))
          writeLine("  " + s.hashCode() + label("" + i) + " [shape = doublecircle]" + ";" , writer);
        else
          writeLine("  " + s.hashCode() + label("" + i) + ";", writer);
      }

      writeLine("", writer);

      // For every transition
      for(Transition tra : this.Transitions) {
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
