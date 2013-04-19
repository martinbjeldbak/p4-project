package dk.aau.cs.d402f13.utilities.types;

import java.util.ArrayList;
import java.util.List;

public abstract class Piece {
	private Player player;
	private Square square = null;
	
	public Square getSquare() {
		return square;
	}

	public void setSquare(Square square) {
		this.square = square;
	}

	List<Action> actions = new ArrayList<Action>();
	
	public List<Action> playerActions(Game g) {
		return actions;
	}
	
	public void setPlayerActions(List<Action> actions) {
		this.actions = actions;
	}
	
	public abstract String getImgPath();

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
	
	
	
}
