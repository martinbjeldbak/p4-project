package dk.aau.cs.d402f13.evaluation;

import dk.aau.cs.d402f13.values.PatternValue;
import dk.aau.cs.d402f13.values.Value;

import java.util.Arrays;
import java.util.ArrayList;

public class NFA {
  private State entry;
  private ArrayList<State> exit;

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
  public void setExit(State exit) {
    exit.setAccept(true);
    this.exit.add(exit);
  }

  /**
   * Sets multiple exit states in this NFA,
   * marking them all as accept states.
   * @param exit the array of states to be added
   */
  public void setExit(State ... exits) {
    for(State s : exits) {
      s.setAccept(true);
      this.exit.add(s);
    }
  }

  /** Same as the method {@link #setExit(State...)} */
  public void setExit(ArrayList<State> exits) {
    for(State s : exits) {
      s.setAccept(true);
      this.exit.add(s);
    }
  }

  public NFA(State entry, State exit) {
    this.entry = entry;
    this.setExit(exit);
  }

  public NFA(State entry, ArrayList<State> exits) {
    this.entry = entry;
    this.setExit(exits);
  }

  public boolean matches(PatternValue pattern) {

    /* Fold out pattern and do something depending on the type
     * of pattern value
     */

    return entry.matches(pattern.getValues());
  }

  // ----- Build up the smallest expressions to larger expressions

  /**
   * A single transition from a new entry state to a new exit state
   * with the edge v going from the entry state to the exit state.
   * @param v the edge value
   * @return  a construction of a new NFA
   *          with 'v' as the edge between an
   *          entry and exit state.
   */
  private NFA v(Value v) {
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
  private NFA e() {
    State entry = new State();
    State exit = new State();
    exit.setAccept(true);
    return new NFA(entry, exit);
  }

  /**
   * Creates an NFA which which matches zero-or-more
   * repetitions of the given NFA. Also known as the
   * kleene star operation ('*').
   * @param nfa the NFA to add a kleene star to
   * @return    the NFA with the kleene star operator
   */
  private NFA kleeneStar(NFA nfa) {
    for(State exit : nfa.getExit()) {
      exit.addEmptyEdge(nfa.getEntry());
    }

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
  private NFA concat(NFA first, NFA second) {
    for(State s : first.getExit()) {
      s.setAccept(false);
      s.addEmptyEdge(second.getEntry());
    }

    return new NFA(first.getEntry(), second.getExit());
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
  private NFA union(NFA choice1, NFA choice2) {
    State entry = new State();

    entry.addEmptyEdge(choice1.getEntry());
    entry.addEmptyEdge(choice2.getEntry());

    ArrayList<State> exits = new ArrayList<>();
    exits.addAll(choice1.getExit());
    exits.addAll(choice2.getExit());

    return new NFA(entry, exits);
  }
}
