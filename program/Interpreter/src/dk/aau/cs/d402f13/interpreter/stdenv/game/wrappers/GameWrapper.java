package dk.aau.cs.d402f13.interpreter.stdenv.game.wrappers;

import java.util.ArrayList;
import java.util.List;

import dk.aau.cs.d402f13.interpreter.stdenv.game.GameEnvironment;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.errors.TypeError;
import dk.aau.cs.d402f13.utilities.gameapi.Action;
import dk.aau.cs.d402f13.utilities.gameapi.ActionSequence;
import dk.aau.cs.d402f13.utilities.gameapi.AddAction;
import dk.aau.cs.d402f13.utilities.gameapi.Game;
import dk.aau.cs.d402f13.utilities.gameapi.MoveAction;
import dk.aau.cs.d402f13.utilities.gameapi.Piece;
import dk.aau.cs.d402f13.utilities.gameapi.Player;
import dk.aau.cs.d402f13.utilities.gameapi.RemoveAction;
import dk.aau.cs.d402f13.utilities.gameapi.Square;
import dk.aau.cs.d402f13.utilities.gameapi.UnitAction;
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
    for (int i = 0; i < turnOrder.length; i++) {
      this.players[i] = new PlayerWrapper(env, turnOrder[i]);
    }
    
    currentPlayer = new PlayerWrapper(env, getMember("currentPlayer", env.playerType()));
  }

  @Override
  public BoardWrapper getBoard() {
    return board;
  }
  
  public GameWrapper setBoard(BoardWrapper board) throws StandardError {
    return new GameWrapper(env, object.setAttribute("currentBoard", board.object));
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
  public GameWrapper applyAction(Action action) throws StandardError {
    if (action instanceof ActionSequenceWrapper) {
      GameWrapper state = this;
      for (UnitActionWrapper a: ((ActionSequenceWrapper)action).getActions()) {
        state = state.applyAction(a);
      }
      return state;
    }
    else if (action instanceof AddActionWrapper) {
      PieceWrapper piece = ((AddActionWrapper)action).getPiece();
      SquareWrapper to = ((AddActionWrapper)action).getTo();
      GridBoardWrapper boardState = (GridBoardWrapper)board;
      to = to.addPiece(piece); 
      GameWrapper state = this; 
      return null;
    }
    else if (action instanceof RemoveActionWrapper) {
      PieceWrapper piece = ((RemoveActionWrapper)action).getPiece();
      return null;
    }
    else if (action instanceof MoveActionWrapper) {
      PieceWrapper piece = ((MoveActionWrapper)action).getPiece();
      SquareWrapper to = ((MoveActionWrapper)action).getTo();
      return null;
    }
    else {
      throw new InternalError("Invalid action class: " + action.getClass());
    }
  }

  @Override
  public GameWrapper undoAction(Action action) throws StandardError {
    if (action instanceof ActionSequenceWrapper) {
      GameWrapper state = this;
      UnitActionWrapper[] list = ((ActionSequenceWrapper)action).getActions();
      for (int i = list.length - 1; i >= 0; i--) {
        state = state.undoAction(list[i]);
      }
      return state;
    }
    else if (action instanceof AddActionWrapper) {
      PieceWrapper piece = ((AddActionWrapper)action).getPiece();
      SquareWrapper to = ((AddActionWrapper)action).getTo(); 
      return null;
    }
    else if (action instanceof RemoveActionWrapper) {
      PieceWrapper piece = ((RemoveActionWrapper)action).getPiece();
      return null;
    }
    else if (action instanceof MoveActionWrapper) {
      PieceWrapper piece = ((MoveActionWrapper)action).getPiece();
      SquareWrapper to = ((MoveActionWrapper)action).getTo(); 
      return null;
    }
    else {
      throw new InternalError("Invalid action class: " + action.getClass());
    }
  }

}
