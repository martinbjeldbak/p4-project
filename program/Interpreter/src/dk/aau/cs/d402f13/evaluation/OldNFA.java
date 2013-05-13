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

public class OldNFA {
  private OldState entry;
  private ArrayList<OldState> exit = new ArrayList<>();

  /**
   * Sets the entry point in this OldNFA.
   * @param entry the new entry state to be
   *              set in this OldNFA
   */
  public void setEntry(OldState entry) {
    this.entry = entry;
  }

  /**
   * Gets the entry state for this OldNFA.
   * @return the entry state
   */
  public OldState getEntry() {
    return entry;
  }

  /**
   * Gets the exit state(s) for this OldNFA.
   * @return the exit state(s)
   */
  public ArrayList<OldState> getExit() {
    return exit;
  }

  /**
   * Sets a singular state as the exit for
   * this OldNFA. It also marks the state as
   * an accept state.
   * @param exit the state to be marked
   *             as an exit
   */
  public void addExit(OldState exit) {
    exit.setAccept(true);
    this.exit.add(exit);
  }

  /**
   * Sets multiple exit states in this OldNFA,
   * marking them all as accept states.
   * @param exits the array of states to be added
   */
  public void addExit(OldState... exits) {
    for(OldState s : exits) {
      s.setAccept(true);
      this.exit.add(s);
    }
  }

  /** Same as the method {@link #addExit(OldState...)} */
  public void addExit(ArrayList<OldState> exits) {
    for(OldState s : exits) {
      s.setAccept(true);
      this.exit.add(s);
    }
  }

  private OldNFA(OldState entry, OldState exit) {
    this.entry = entry;
    addExit(exit);
  }

  private OldNFA(OldState entry, ArrayList<OldState> exits) {
    this.entry = entry;
    this.addExit(exits);
  }

  public OldNFA() {}

  // ----- Methods to build up small expressions to larger expressions

  /**
   * A single transition from a new entry state to a new exit state
   * with the edge v going from the entry state to the exit state.
   * @param v the edge value
   * @return  a construction of a new OldNFA
   *          with 'v' as the edge between an
   *          entry and exit state.
   */
  public static final OldNFA v(Value v) {
    OldState entry = new OldState();
    OldState exit = new OldState();
    exit.setAccept(true);
    entry.addValEdge(v, exit);
    return new OldNFA(entry, exit);
  }

  /**
   * Add an epsilon edge between two new states
   * @return a construction of a new OldNFA with
   *         an epsilon edge between the two
   *         states
   */
  public static final OldNFA e() {
    OldState entry = new OldState();
    OldState exit = new OldState();
    entry.addEmptyEdge(exit);
    exit.setAccept(true);
    return new OldNFA(entry, exit);
  }

  /**
   * Creates an OldNFA which which matches zero-or-more
   * repetitions of the given OldNFA. Also known as the
   * kleene star regular expression operation ('*').
   * @param nfa the OldNFA to add a kleene star to
   * @return    the OldNFA with the kleene star operator
   */
  public static final OldNFA kleeneStar(OldNFA nfa) {
    for(OldState exit : nfa.getExit())
      exit.addEmptyEdge(nfa.getEntry());

    OldState entry = new OldState();
    entry.setAccept(true);
    entry.addEmptyEdge(nfa.getEntry());

    return new OldNFA(entry, nfa.getExit());
  }

  /**
   * Creates an OldNFA which matches one-to-many
   * repetitions of the given OldNFA. The
   * ('+') regular expression operation.
   * @param nfa the OldNFA to add a plus operation
   *            on
   * @return    the OldNFA with the plus operation
   */
  public static final OldNFA plus(OldNFA nfa) {
    for(OldState exit : nfa.getExit())
      exit.addEmptyEdge(nfa.getEntry());

    OldState entry = new OldState();
    entry.addEmptyEdge(nfa.getEntry());

    return new OldNFA(entry, nfa.getExit());
  }

  /**
   * The concatenate operation. Adds the second
   * OldNFA to the first OldNFA, concatenating the two.
   * @param first  the OldNFA to be prepended to the second
   * @param second the second OldNFA
   * @return       a new OldNFA with the two NFAs
   *               concatenated
   */
  public static final OldNFA concat(OldNFA first, OldNFA second) {
    for(OldState s : first.getExit()) {
      s.setAccept(false);
      s.addEmptyEdge(second.getEntry());
    }

    return new OldNFA(first.getEntry(), second.getExit());
  }

  /** Instance method identical to {@link #concat(OldNFA, OldNFA)},
   * but uses the current OldNFA instead */
  public final OldNFA concat(OldNFA second) {
    for (OldState s : this.getExit()) {
      s.setAccept(false);
      s.addEmptyEdge(second.getEntry());
    }

    return new OldNFA(this.getEntry(), second.getExit());
  }

  /**
   * The union/or operation. Adds epsilon-transitions
   * between the two OldNFA's given as input.
   * @param choice1 the first OldNFA option
   * @param choice2 the second OldNFA option
   * @return        a new OldNFA with a new entry
   *                with epsilon-transitions
   *                to the two NFAs
   */
  public static final OldNFA union(OldNFA choice1, OldNFA choice2) {
    OldState entry = new OldState();

    entry.addEmptyEdge(choice1.getEntry());
    entry.addEmptyEdge(choice2.getEntry());

    ArrayList<OldState> exits = new ArrayList<>();
    exits.addAll(choice1.getExit());
    exits.addAll(choice2.getExit());

    return new OldNFA(entry, exits);
  }

  /** Same as method {@link #union(OldNFA, OldNFA)}, just
   * accepts an array of OldNFA machines instead
   */
  public static final OldNFA union(OldNFA... choices) {
    OldState entry = new OldState();

    ArrayList<OldState> exits = new ArrayList<>();

    for(OldNFA machine : choices) {
      entry.addEmptyEdge(machine.getEntry());
      exits.addAll(machine.getExit());
    }
    return new OldNFA(entry, exits);
  }

  public static final OldNFA s(Value ... rexps) {
    OldNFA exp = e();

    return null;
  }

  public final void toDot() {
    Path file = createFile("OldNFA.dot");

    try(BufferedWriter writer = Files.newBufferedWriter(file, Charset.defaultCharset())) {
      writeLine("digraph OldNFA {", writer);

      //writeLine("rankdir=LR;", writer);
      writeLine("  node[shape = circle];", writer);

      makeEdges(entry, 0, writer);

      writeLine("}", writer);
      writer.close();
    }
    catch(IOException ex) {
      System.out.println("Error writing to file");
    }
  }

  private void makeEdges(OldState s, Integer nodeNr, BufferedWriter writer) {
    int sID = s.hashCode();
    int eID;

    s.setVisited(true);

    if(s.isAccept())
      writeLine("  " + sID + label("" + nodeNr) + " [shape = doublecircle]" + ";", writer);
    else
      writeLine("  " + sID + label("" + nodeNr) + ";", writer);

    // For every valued edge leaving the state, print dot language
    for(Map.Entry<Value, OldState> edge : s.getValueEdges().entrySet()) {
      eID = edge.getValue().hashCode();

      writeLine("  " + sID + " -> " + eID + label(edge.getKey().toString()) + ";", writer);

      // Then for this state, make edges for its edges to other states recursively
      if(edge.getValue().visited() == false)
        makeEdges(edge.getValue(), nodeNr + 1, writer);
    }

    // For every epsilon edge, do the same
    for(OldState eps : s.getEmptyEdges()) {
      eID = eps.hashCode();

      writeLine("  " + sID + " -> " + eID + label("&#949;") + ";", writer);

      if(eps.visited() == false)
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
