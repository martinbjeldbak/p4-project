package dk.aau.cs.d402f13.utilities.types;

public class MoveAction extends Action {
	private Piece p;
	private Square to;
	private Square from;
	
	public MoveAction( Piece p, Square to ){
		this.p = p;
		this.from = p.square();
		this.to = to;
	}
	
	public Piece getPiece(){
		return p;
	}
	public Square getTo(){
		return to;
	}
	public Square getFrom(){
		return from;
	}
	
	void applyAction( Game g ){
		p.setSquare(to);
	}
}
