package dk.aau.cs.d402f13.evaluation;

import dk.aau.cs.d402f13.values.Value;

import java.util.*;
import java.util.ArrayList;

public class OldState {
  private boolean isAccept = false;
  private ArrayList<OldState> outEmpty = new ArrayList();
  private HashMap<Value, OldState> outValue = new HashMap();
  private boolean visited = false;


  public ArrayList<OldState> getEmptyEdges() {
    return outEmpty;
  }

  public HashMap<Value, OldState> getValueEdges() {
    return outValue;
  }

  public boolean visited() {
    return visited;
  }

  public void setVisited(boolean val) {
    visited = val;
  }

  /**
   * Add a transition edge from this state to the next,
   * which consumes the Value v.
   * @param v    the value to label the edge
   * @param next the state to point to
   */
  public void addValEdge(Value v, OldState next) {
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
  public void addEmptyEdge(OldState next) {
    outEmpty.add(next);
  }

  public OldState() {  }

  @Override
  public String toString() {
    StringBuilder s = new StringBuilder("OldState: (edges: " + (outEmpty.size() + outValue.size()) + ")\n");

    if(outEmpty.size() + outValue.size() == 0)
      return s.toString();;

    for(OldState state : outEmpty) {
      s.append("  " + state + "\n");
    }

    for(Map.Entry<Value, OldState> edge : outValue.entrySet()) {
     s.append("  " + edge.getValue() + "(value: " + edge.getKey() + ")\n");
    }

    return s.toString();
  }

}
