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
    //OldNFA nfa = createOldNFA(OldNFA.e(), pattern);
    //nfa.toDot();

    NFA nfa = new NFA();

    createNFA(nfa, pattern);
    nfa.toDot();
    new DFA(nfa).toDot();

    return false;
  }

  private void createNFA(NFA n, Value v) {
    if(v instanceof PatternKeyValue) {
      PatternKeyValue val = (PatternKeyValue)v;

      n.concat(new NFA(val));
    }
    else if(v instanceof PatternNotValue) {
      PatternNotValue val = (PatternNotValue)v;

      // Create the NFA for the value, then not the NFA
      NFA not = new NFA();
      createNFA(not, val.getValue());
      not.not();

      // Finally, add this NFA to the current NFA
      n.concat(not);
    }
    else if(v instanceof PatternOrValue) {
      PatternOrValue val = (PatternOrValue)v;

      NFA opt1 = new NFA();
      NFA opt2 = new NFA();

      createNFA(opt1, val.getLeft());
      createNFA(opt2, val.getRight());

      opt1.union(opt2);

      n.concat(opt1);
    }
    else if(v instanceof PatternOptValue) {
      PatternOptValue val = (PatternOptValue)v;

      NFA optionalNFA = new NFA();

      createNFA(optionalNFA, val.getValue());
      optionalNFA.optional();

      n.concat(optionalNFA);
    }
    else if(v instanceof PatternMultValue) {
      PatternMultValue val = (PatternMultValue)v;

      NFA mult = new NFA();
      createNFA(mult, val.getValue());
      mult.kleeneStar();

      n.concat(mult);
    }
    else if(v instanceof PatternPlusValue) {
      PatternPlusValue val = (PatternPlusValue)v;

      NFA plus = new NFA();
      createNFA(plus, val.getValue());
      plus.plus();

      n.concat(plus);
    }
    else if(v instanceof PatternValue) {
       for(Value val : ((PatternValue) v).getValues()) {
         createNFA(n, val);
       }
    }
    else
      n.concat(new NFA(v));
  }

  private OldNFA createOldNFA(OldNFA nfa, Value v) {

    if(v instanceof PatternOrValue) {
      Value left = ((PatternOrValue) v).getLeft();
      Value right = ((PatternOrValue) v).getRight();

      return OldNFA.union(createOldNFA(nfa, left), createOldNFA(nfa, right));
    }
    else if(v instanceof PatternMultValue) {
      return OldNFA.kleeneStar(createOldNFA(nfa, ((PatternMultValue) v).getValue()));
    }
    else if(v instanceof PatternPlusValue) {
      return OldNFA.plus(createOldNFA(nfa, ((PatternPlusValue) v).getValue()));
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
        cur = OldNFA.concat(cur, createOldNFA(cur, val));
      }

      return cur;
    }
    // If it's just a value, return an OldNFA
    // with the value as a label
    //System.out.println("Concatenating OldNFA \n" + nfa.getEntry() + "--with OldNFA\n" + OldNFA.v(v).getEntry());
    return OldNFA.v(v);
  }
}
