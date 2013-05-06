package dk.aau.cs.d402f13.evaluation;

import dk.aau.cs.d402f13.utilities.types.Board;
import dk.aau.cs.d402f13.utilities.types.Game;
import dk.aau.cs.d402f13.utilities.types.Piece;
import dk.aau.cs.d402f13.utilities.types.Player;
import dk.aau.cs.d402f13.values.CoordValue;
import dk.aau.cs.d402f13.values.PatternValue;

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

  public boolean evaluate() {

    // Move this stuff in separate, private method
    List<Piece> pieces = game.board().getPieces();

    for(Piece piece : pieces) {
      if(piece.player() == game.currentPlayer()) {
        // Check if the piece's coordinate is the same as the supplied coordinate
      }
    }



    return false;
  }
}
