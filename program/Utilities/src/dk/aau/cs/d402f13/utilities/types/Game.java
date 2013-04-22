package dk.aau.cs.d402f13.utilities.types;

import java.util.ArrayList;
import java.util.List;

public class Game {
	private Board board = null; 
	private List<Player> players = new ArrayList<Player>();
	private List<Action> history = new ArrayList<Action>();
	private String title;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

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
	
	public void applyAction( Action action ){
		history.add( action );
		action.applyAction( this );
	}
}
