package dk.aau.cs.d402f13.evaluation;

import dk.aau.cs.d402f13.values.PatternValue;

import java.util.Arrays;

public class NFA {
  private State entry, exit;

  public void setEntry(State entry) {
    this.entry = entry;
  }

  public State getEntry() {
    return entry;
  }

  public State getExit() {
    return exit;
  }

  public void setExit(State exit) {
    this.exit = exit;
  }

  public NFA(State entry, State exit) {
    this.entry = entry;
    this.exit = exit;
  }

  public boolean matches(PatternValue pattern) {
    return entry.matches(pattern.getValues());
  }
}
