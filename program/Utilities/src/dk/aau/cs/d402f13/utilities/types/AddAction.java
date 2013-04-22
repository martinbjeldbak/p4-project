package dk.aau.cs.d402f13.utilities.types;

public class AddAction extends Action {
	private Piece p;
	private Square s;
	
	public AddAction( Piece p, Square s ){
		this.p = p;
		this.s = s;
	}
	
	public Piece getPiece(){
		return p;
	}
	public Square getSquare(){
		return s;
	}
	
	void applyAction( Game g ){
		g.board().addPiece(p);
		p.setSquare(s);
	}
}
