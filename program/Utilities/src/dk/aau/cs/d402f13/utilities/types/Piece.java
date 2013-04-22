package dk.aau.cs.d402f13.utilities.types;

import java.util.List;

public abstract class Piece {
	private Player player;
	private Square square = null;

	public abstract String getImgPath();
	public abstract List<Action> actions( Game g );
	
	public Square getSquare() {
		return square;
	}

	public void setSquare(Square square) {
		this.square = square;
	}


	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
	
	
	
}
