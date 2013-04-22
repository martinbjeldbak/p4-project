package dk.aau.cs.d402f13.utilities.types;

import java.util.List;

public abstract class Piece{
	private Player player;
	private Square square = null;
	
	public Piece( Player player ){
		this.player = player;
	}

	public abstract String getImgPath();
	public abstract List<Action> actions( Game g );
	
	public Player player(){
		return player;
	}
	
	public Square square(){
		return square;
	}

	public void setSquare( Square square ){
		this.square = square;
	}
}
