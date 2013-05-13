package dk.aau.cs.d402f13.evaluation;

import dk.aau.cs.d402f13.utilities.gameapi.Game;
import dk.aau.cs.d402f13.values.*;

public class GridBoardPatternEvaluator {
  private final CoordValue coord;
  private final PatternValue pattern;
  private final Game game;

  public GridBoardPatternEvaluator(CoordValue coord, PatternValue pattern, Game game) {
    this.coord = coord;
    this.pattern = pattern;
    this.game = game;
  }

  public GridBoardPatternEvaluator(PatternValue pattern) {
    this.pattern = pattern;
    this.coord = null;
    this.game = null;
  }

  public boolean evaluate() {
    OldNFA nfa = createNFA(OldNFA.e(), pattern);
    nfa.toDot();

    //System.out.println(nfa.getEntry());

    return false;
  }

  private OldNFA createNFA(OldNFA nfa, Value v) {

    if(v instanceof PatternOrValue) {
      Value left = ((PatternOrValue) v).getLeft();
      Value right = ((PatternOrValue) v).getRight();

      return OldNFA.union(createNFA(nfa, left), createNFA(nfa, right));
    }
    else if(v instanceof PatternMultValue) {
      return OldNFA.kleeneStar(createNFA(nfa, ((PatternMultValue) v).getValue()));
    }
    else if(v instanceof PatternPlusValue) {
      return OldNFA.plus(createNFA(nfa, ((PatternPlusValue) v).getValue()));
    }
    else if(v instanceof PatternKeyValue) {
      return OldNFA.v(v);
    }
    else if(v instanceof PatternNotValue) {
      // TODO
    }
    else if(v instanceof PatternOptValue) {
      // TODO
    }
    else if(v instanceof PatternValue) {

      OldNFA cur = nfa;

      for(Value val : ((PatternValue) v).getValues()) {
        cur = OldNFA.concat(cur, createNFA(cur, val));
      }

      return cur;
    }
    // If it's just a value, return an OldNFA
    // with the value as a label
    //System.out.println("Concatenating OldNFA \n" + nfa.getEntry() + "--with OldNFA\n" + OldNFA.v(v).getEntry());
    return OldNFA.v(v);
  }
}
