package dk.aau.cs.d402f13.evaluation;

import dk.aau.cs.d402f13.values.PatternValue;
import dk.aau.cs.d402f13.values.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;

public class State {
  private boolean isAccept = false;
  private ArrayList<State> outEmpty = new ArrayList();
  private HashMap<Value, State> outValue = new HashMap();


  /**
   * Add a transition edge from this state to the next,
   * which consumes the Value v.
   * @param v    the value to label the edge
   * @param next the state to point to
   */
  public void addValEdge(Value v, State next) {
     outValue.put(v, next);
  }

  /**
   * Checks to see if this state is an
   * accept state
   * @return true if the current state
   *         is an accept state
   */
  public boolean isAccept() {
    return isAccept;
  }

  /**
   * Sets this state to be the accept state
   * @param accept the value of the accept state, true
   *               if it is, else false
   */
  public void setAccept(boolean accept) {
    isAccept = accept;
  }

  /**
   * Adds an epsilon-transition to a state
   * @param next the state to have an epsilon-transition
   *             to
   */
  public void addEmptyEdge(State next) {
    outEmpty.add(next);
  }

  public State() {  }

  public boolean matches(ArrayList<Value> vals) {
    return matches(vals, new ArrayList<State>());
  }

  public boolean matches(Value[] vals) {
    return matches(new ArrayList(Arrays.asList(vals)));
  }

  private boolean matches(ArrayList<Value> vals, ArrayList<State> visited) {
    /* We've found a path back to ourself through epsilon-edges
     *  stop, or we'll loop infinitely. */
    if (visited.contains(this))
      return false;


    /* In case we make an empty transition, we need to add this
     * state to the visited list. */
    visited.add(this);

    /* If there are no more values, we match the values only
     * if the current state is an accept state */
    if (vals.size() == 0) {
      if (isAccept)
        return true;

      /* Since this state is not final, we'll ask if any
       * neighboring states that we can reach on empty edges can
       * match the empty vals (an empty ArrayList of values). */
      for(State next : outEmpty) {
        if(next.matches(new ArrayList<Value>(), visited))
          return true;
      }
    }
    else {
      /* Here our values aren't empty, so let's remove
       * the first value and see if we get a match among
       * neighbors with that value as an edge
       */
      Value v = vals.get(0);

      for(State next : outValue.values()) {
         if(next.matches(new ArrayList<Value>(Arrays.asList(v))));
           return true;
      }

      /* It looks like we weren't able to match the string by
       * consuming a value, so we'll ask our
       * empty-transition neighbors if they can match the entire
       * string. */
      for(State next : outEmpty) {
        if(next.matches(vals, visited))
          return true;
      }
    }
    return false;
  }
}
