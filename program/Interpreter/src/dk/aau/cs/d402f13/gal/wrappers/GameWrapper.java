package dk.aau.cs.d402f13.gal.wrappers;

import java.util.ArrayList;
import dk.aau.cs.d402f13.interpreter.stdenv.game.GameEnvironment;
import dk.aau.cs.d402f13.utilities.errors.InternalError;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.gameapi.Action;
import dk.aau.cs.d402f13.utilities.gameapi.Game;
import dk.aau.cs.d402f13.utilities.gameapi.Player;
import dk.aau.cs.d402f13.values.Value;

public class GameWrapper extends Wrapper implements Game {
  private String title;
  private String description;
  private GridBoardWrapper board;

  private PlayerWrapper[] players;
  private PlayerWrapper[] turnOrder;

  private PlayerWrapper currentPlayer;

  private Action[] actions;

  public GameWrapper(GameEnvironment env, Value object) throws StandardError {
    super(env, object);
    title = getMemberString("title");
    description = getMemberString("description");
    board = new GridBoardWrapper(env, getMember("board", env.gridBoardType()));

    Value[] players = getMemberList("players", env.playerType(), 1);
    this.players = new PlayerWrapper[players.length];
    for (int i = 0; i < players.length; i++) {
      this.players[i] = new PlayerWrapper(env, players[i]);
    }
    Value[] turnOrder = getMemberList("turnOrder", env.playerType(), 1);
    this.turnOrder = new PlayerWrapper[turnOrder.length];
    for (int i = 0; i < turnOrder.length; i++) {
      this.turnOrder[i] = new PlayerWrapper(env, turnOrder[i]);
    }

    currentPlayer = new PlayerWrapper(env, getMember("currentPlayer", env.playerType()));
  }

  @Override
  public GridBoardWrapper getBoard() {
    return board;
  }
  
  public GameWrapper setBoard(GridBoardWrapper board) throws StandardError {
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
  public GameWrapper nextTurn() throws StandardError {
    return new GameWrapper(env, callMember("nextTurn", env.gameType()));
  }

  @Override
  public GameWrapper applyAction(Action action) throws StandardError {
    Wrapper actionWrapper;
    if (action instanceof UnitActionWrapper) {
      actionWrapper = (UnitActionWrapper)action;
    }
    else if (action instanceof ActionSequenceWrapper) {
      actionWrapper = (ActionSequenceWrapper)action;
    }
    else {
      throw new InternalError("Invalid action class: " + action.getClass());
    }
    return new GameWrapper(env, callMember("applyAction", env.gameType(), actionWrapper.object));
  }

  @Override
  public GameWrapper undoAction() throws StandardError {
    return new GameWrapper(env, callMember("undoAction", env.gameType()));
  }

  @Override
  public Action[] getActions() throws StandardError {
    if (actions != null) {
      return actions;
    }
    ArrayList<Action> actionList = new ArrayList<Action>();
    Action[] playerActions = currentPlayer.getActions(this);
    for (Action a : playerActions) {
      actionList.add(a);
    }
    for (PieceWrapper p : board.getPieces()) {
      if (p.getOwner().object.equals(currentPlayer.object)) {
        Action[] pieceActions = p.getActions(this);
        for (Action a : pieceActions) {
          actionList.add(a);
        }
      }
    }
    Action[] actions = new Action[actionList.size()];
    for (int i = 0; i < actions.length; i++) {
      actions[i] = actionList.get(i);
    }
    this.actions = actions;
    return actions;
  }

  @Override
  public String getDescription() throws StandardError {
    return description;
  }

  @Override
  public Action[] getHistory() throws StandardError {
    return getMemberActions("history");
  }
}
