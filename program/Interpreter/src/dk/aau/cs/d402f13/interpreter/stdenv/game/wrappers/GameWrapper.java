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

public class GameWrapper extends Wrapper implements Game {
  
  private String title;
  private BoardWrapper board;
  
  private PlayerWrapper[] players;
  private PlayerWrapper[] turnOrder;
  
  private PlayerWrapper currentPlayer;

  public GameWrapper(GameEnvironment env, Value object) throws StandardError {
    super(env, object);
    title = getMemberString("title");
    board = new BoardWrapper(env, getMember("currentBoard", env.boardType()));
    
    Value[] players = getMemberList("players", env.playerType(), 1);
    this.players = new PlayerWrapper[players.length];
    for (int i = 0; i < players.length; i++) {
      this.players[i] = new PlayerWrapper(env, players[i]);
    }
    Value[] turnOrder = getMemberList("turnOrder", env.playerType(), 1);
    this.turnOrder = new PlayerWrapper[players.length];
    for (int i = 0; i < players.length; i++) {
      this.players[i] = new PlayerWrapper(env, players[i]);
    }
    
    currentPlayer = new PlayerWrapper(env, getMember("currentPlayer", env.playerType()));
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

  @Override
  public Game undoAction(Action action) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

}
