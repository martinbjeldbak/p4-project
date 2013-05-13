package dk.aau.cs.d402f13.utilities.gameapi;

import dk.aau.cs.d402f13.utilities.errors.StandardError;

public interface Game {
  public String getTitle() throws StandardError;
  public String getDescription() throws StandardError;
  public Board getBoard() throws StandardError;
  public Player getCurrentPlayer() throws StandardError;
  public Player[] getPlayers() throws StandardError;
  public Player[] getTurnOrder() throws StandardError;
  public Game applyAction(Action action) throws StandardError;
  public Game undoAction(Action action) throws StandardError;
  public Action[] getActions() throws StandardError;
  public Action[] getHistory() throws StandardError;
}
