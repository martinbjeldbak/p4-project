package dk.aau.cs.d402f13.interpreter.stdenv.game.wrappers;

import java.util.ArrayList;
import java.util.List;

import dk.aau.cs.d402f13.interpreter.stdenv.game.GameEnvironment;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.errors.TypeError;
import dk.aau.cs.d402f13.utilities.types.Board;
import dk.aau.cs.d402f13.utilities.types.Game;
import dk.aau.cs.d402f13.utilities.types.Player;
import dk.aau.cs.d402f13.values.ListValue;
import dk.aau.cs.d402f13.values.ObjectValue;
import dk.aau.cs.d402f13.values.TypeValue;
import dk.aau.cs.d402f13.values.StrValue;
import dk.aau.cs.d402f13.values.Value;

public class GameWrapper extends Game {
  
  private ObjectValue object;
  private BoardWrapper board;
  
  private List<Player> players;
  
  private PlayerWrapper currentPlayer;
  
  public ObjectValue getObject() {
    return object;
  }

  public GameWrapper(GameEnvironment env, ObjectValue object) throws StandardError {
    super(((StrValue)TypeValue.expect(object.getMember("title"), StrValue.type())).getValue());
    this.object = object;
    board = new BoardWrapper(env, (ObjectValue)object.getMember("board", env.boardType()));
    Value[] players = ((ListValue)object.getMember("players", ListValue.type())).getValues();
    if (players.length < 1) {
      throw new TypeError("Invalid length of players-list");
    }
    TypeValue.expect(env.playerType(), players);
    this.players = new ArrayList<Player>();
    for (Value p : players) {
      this.players.add(new PlayerWrapper(env, (ObjectValue)p));
    }
    
    this.currentPlayer = new PlayerWrapper(env,
        (ObjectValue)object.getMember("currentPlayer", env.playerType()));
  }

  @Override
  public BoardWrapper board() {
    return board;
  }

  @Override
  public List<Player> players() {
    return players;
  }
  
  @Override
  public PlayerWrapper currentPlayer() {
    return currentPlayer;
  }

}
