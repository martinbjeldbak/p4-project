package dk.aau.cs.d402f13.evaluation;

import dk.aau.cs.d402f13.values.PatternValue;
import dk.aau.cs.d402f13.values.Value;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

public class NFA {
  private State entry;
  private ArrayList<State> exit = new ArrayList<>();

  /**
   * Sets the entry point in this NFA.
   * @param entry the new entry state to be
   *              set in this NFA
   */
  public void setEntry(State entry) {
    this.entry = entry;
  }

  /**
   * Gets the entry state for this NFA.
   * @return the entry state
   */
  public State getEntry() {
    return entry;
  }

  /**
   * Gets the exit state(s) for this NFA.
   * @return the exit state(s)
   */
  public ArrayList<State> getExit() {
    return exit;
  }

  /**
   * Sets a singular state as the exit for
   * this NFA. It also marks the state as
   * an accept state.
   * @param exit the state to be marked
   *             as an exit
   */
  public void addExit(State exit) {
    exit.setAccept(true);
    this.exit.add(exit);
  }

  /**
   * Sets multiple exit states in this NFA,
   * marking them all as accept states.
   * @param exits the array of states to be added
   */
  public void addExit(State ... exits) {
    for(State s : exits) {
      s.setAccept(true);
      this.exit.add(s);
    }
  }

  /** Same as the method {@link #addExit(State...)} */
  public void addExit(ArrayList<State> exits) {
    for(State s : exits) {
      s.setAccept(true);
      this.exit.add(s);
    }
  }

  private NFA(State entry, State exit) {
    this.entry = entry;
    addExit(exit);
  }

  private NFA(State entry, ArrayList<State> exits) {
    this.entry = entry;
    this.addExit(exits);
  }

  public NFA() {}

  // ----- Methods to build up small expressions to larger expressions

  /**
   * A single transition from a new entry state to a new exit state
   * with the edge v going from the entry state to the exit state.
   * @param v the edge value
   * @return  a construction of a new NFA
   *          with 'v' as the edge between an
   *          entry and exit state.
   */
  public static final NFA v(Value v) {
    State entry = new State();
    State exit = new State();
    exit.setAccept(true);
    entry.addValEdge(v, exit);
    return new NFA(entry, exit);
  }

  /**
   * Add an epsilon edge between two new states
   * @return a construction of a new NFA with
   *         an epsilon edge between the two
   *         states
   */
  public static final NFA e() {
    State entry = new State();
    State exit = new State();
    exit.setAccept(true);
    return new NFA(entry, exit);
  }

  /**
   * Creates an NFA which which matches zero-or-more
   * repetitions of the given NFA. Also known as the
   * kleene star regular expression operation ('*').
   * @param nfa the NFA to add a kleene star to
   * @return    the NFA with the kleene star operator
   */
  public static final NFA kleeneStar(NFA nfa) {
    for(State exit : nfa.getExit())
      exit.addEmptyEdge(nfa.getEntry());

    State entry = new State();
    entry.setAccept(true);
    entry.addEmptyEdge(nfa.getEntry());

    return new NFA(entry, nfa.getExit());
  }

  /**
   * Creates an NFA which matches one-to-many
   * repetitions of the given NFA. The
   * ('+') regular expression operation.
   * @param nfa the NFA to add a plus operation
   *            on
   * @return    the NFA with the plus operation
   */
  public static final NFA plus(NFA nfa) {
    for(State exit : nfa.getExit())
      exit.addEmptyEdge(nfa.getEntry());

    State entry = new State();
    entry.addEmptyEdge(nfa.getEntry());

    return new NFA(entry, nfa.getExit());
  }

  /**
   * The concatenate operation. Adds the second
   * NFA to the first NFA, concatenating the two.
   * @param first  the NFA to be prepended to the second
   * @param second the second NFA
   * @return       a new NFA with the two NFAs
   *               concatenated
   */
  public static final NFA concat(NFA first, NFA second) {
    for(State s : first.getExit()) {
      s.setAccept(false);
      s.addEmptyEdge(second.getEntry());
    }

    return new NFA(first.getEntry(), second.getExit());
  }

  /** Instance method identical to {@link #concat(NFA, NFA)},
   * but uses the current NFA instead */
  public final NFA concat(NFA second) {
    for (State s : this.getExit()) {
      s.setAccept(false);
      s.addEmptyEdge(second.getEntry());
    }
    return new NFA(this.getEntry(), second.getEntry());
  }

  /**
   * The union/or operation. Adds epsilon-transitions
   * between the two NFA's given as input.
   * @param choice1 the first NFA option
   * @param choice2 the second NFA option
   * @return        a new NFA with a new entry
   *                with epsilon-transitions
   *                to the two NFAs
   */
  public static final NFA union(NFA choice1, NFA choice2) {
    State entry = new State();

    entry.addEmptyEdge(choice1.getEntry());
    entry.addEmptyEdge(choice2.getEntry());

    ArrayList<State> exits = new ArrayList<>();
    exits.addAll(choice1.getExit());
    exits.addAll(choice2.getExit());

    return new NFA(entry, exits);
  }

  /** Same as method {@link #union(NFA, NFA)}, just
   * accepts an array of NFA machines instead
   */
  public static final NFA union(NFA ... choices) {
    State entry = new State();

    ArrayList<State> exits = new ArrayList<>();

    for(NFA machine : choices) {
      entry.addEmptyEdge(machine.getEntry());
      exits.addAll(machine.getExit());
    }
    return new NFA(entry, exits);
  }

  public static final NFA s(Value ... rexps) {
    NFA exp = e();

    return null;
  }

  public final void toDot() {
    Path file = createFile("NFA.dot");

    try(BufferedWriter writer = Files.newBufferedWriter(file, Charset.defaultCharset())) {
      writeLine("digraph NFA {", writer);

      writeLine("rankdir=LR;", writer);

      makeEdges(entry, 0, writer);

      writeLine("}", writer);
      writer.close();
    }
    catch(IOException ex) {
      System.out.println("Error writing to file");
    }
  }

  private void makeEdges(State s, Integer nodeNr, BufferedWriter writer) {
    int sID = s.hashCode();
    int eID;

    writeLine(sID + label(""+nodeNr) + ";", writer);

    // For every valued edge leaving the state, print dot language
    for(Map.Entry<Value, State> edge : s.getValueEdges().entrySet()) {
      eID = edge.getValue().hashCode();

      writeLine(sID + " -> " + eID + label(edge.getKey().toString()) + ";", writer);

      // Then for this state, make edges for its edges to other states recursively
      makeEdges(edge.getValue(), nodeNr + 1, writer);
    }

    // For every epsilon edge, do the same
    for(State eps : s.getEmptyEdges()) {
      eID = eps.hashCode();

      writeLine(sID + " -> " + eID + ";", writer);
      makeEdges(eps, nodeNr + 1, writer);
    }
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
}
