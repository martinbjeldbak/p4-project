package dk.aau.cs.d402f13.interpreter.stdenv.game.wrappers;

import java.util.ArrayList;
import java.util.List;

import dk.aau.cs.d402f13.interpreter.stdenv.game.GameEnvironment;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.errors.TypeError;
import dk.aau.cs.d402f13.utilities.gameapi.Game;
import dk.aau.cs.d402f13.utilities.gameapi.Player;
import dk.aau.cs.d402f13.utilities.types.Action;
import dk.aau.cs.d402f13.values.ListValue;
import dk.aau.cs.d402f13.values.ObjectValue;
import dk.aau.cs.d402f13.values.TypeValue;
import dk.aau.cs.d402f13.values.StrValue;
import dk.aau.cs.d402f13.values.Value;

public class GameWrapper implements Game {
  
  private String title;
  private ObjectValue object;
  private BoardWrapper board;
  
  private PlayerWrapper[] players;
  private PlayerWrapper[] turnOrder;
  
  private PlayerWrapper currentPlayer;
  
  public ObjectValue getObject() {
    return object;
  }

  public GameWrapper(GameEnvironment env, ObjectValue object) throws StandardError {
    title = ((StrValue)TypeValue.expect(object.getMember("title"), StrValue.type())).getValue();
    this.object = object;
    board = new BoardWrapper(env, (ObjectValue)object.getMember("board", env.boardType()));
    Value[] players = ((ListValue)object.getMember("players", ListValue.type())).getValues();
    if (players.length < 1) {
      throw new TypeError("Invalid length of players-list");
    }
    TypeValue.expect(env.playerType(), players);
    this.players = new PlayerWrapper[players.length];
    for (int i = 0; i < players.length; i++) {
      this.players[i] = new PlayerWrapper(env, (ObjectValue)players[i]);
    }
    
    Value[] turnOrder = ((ListValue)object.getMember("turnOrder", ListValue.type())).getValues();
    if (turnOrder.length < 1) {
      throw new TypeError("Invalid length of turnOrder-list");
    }
    TypeValue.expect(env.playerType(), turnOrder);
    this.turnOrder = new PlayerWrapper[players.length];
    for (int i = 0; i < players.length; i++) {
      this.players[i] = new PlayerWrapper(env, (ObjectValue)players[i]);
    }
    
    this.currentPlayer = new PlayerWrapper(env,
        (ObjectValue)object.getMember("currentPlayer", env.playerType()));
  }

  @Override
  public BoardWrapper getBoard() {
    return board;
  }

  @Override
  public PlayerWrapper[] getPlayers() {
    return players;
  }
  
  @Override
  public PlayerWrapper getCurrentPlayer() {
    return currentPlayer;
  }

  @Override
  public String getTitle() throws StandardError {
    return title;
  }

  @Override
  public Player[] getTurnOrder() throws StandardError {
    return turnOrder;
  }

  @Override
  public Game applyAction(Action action) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

}
