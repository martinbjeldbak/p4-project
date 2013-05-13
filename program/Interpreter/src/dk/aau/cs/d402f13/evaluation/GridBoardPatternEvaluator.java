package dk.aau.cs.d402f13.evaluation;

import dk.aau.cs.d402f13.utilities.types.Game;
import dk.aau.cs.d402f13.values.*;

import java.util.List;

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
    NFA nfa = createNFA(NFA.e(), pattern);
    nfa.toDot();

    //System.out.println(nfa.getEntry());

    return false;
  }

  private NFA createNFA(NFA nfa, Value v) {

    if(v instanceof PatternOrValue) {
      Value left = ((PatternOrValue) v).getLeft();
      Value right = ((PatternOrValue) v).getRight();

      return NFA.union(createNFA(nfa, left), createNFA(nfa, right));
    }
    else if(v instanceof PatternMultValue) {
      return NFA.kleeneStar(createNFA(nfa, ((PatternMultValue) v).getValue()));
    }
    else if(v instanceof PatternPlusValue) {
      return NFA.plus(createNFA(nfa, ((PatternPlusValue) v).getValue()));
    }
    else if(v instanceof PatternKeyValue) {
      return NFA.v(v);
    }
    else if(v instanceof PatternNotValue) {
      // TODO
    }
    else if(v instanceof PatternOptValue) {
      // TODO
    }
    else if(v instanceof PatternValue) {

      NFA cur = nfa;

      for(Value val : ((PatternValue) v).getValues()) {
        cur = NFA.concat(cur, createNFA(cur, val));
      }

      return cur;
    }
    // If it's just a value, return an NFA
    // with the value as a label
    //System.out.println("Concatenating NFA \n" + nfa.getEntry() + "--with NFA\n" + NFA.v(v).getEntry());
    return NFA.v(v);
  }
}
