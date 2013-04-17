package dk.aau.cs.d402f13.utilities.types;

import java.util.ArrayList;
import java.util.List;

public class Game {
	
	Board board = null; 
	List<Player> players = new ArrayList<Player>();
	
	public Board getBoard() {
		return board;
	}
	
	public void setBoard(Board b) {
		this.board = b;
	}
	
	public List<Player> getPlayers() {
		return players; 
	}
	
	public void setplayers(List<Player> players) {
		this.players = players;
	}
	
}
