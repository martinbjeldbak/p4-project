package dk.aau.cs.d402f13.utilities.types;

public class RemoveAction extends Action {
	private Piece p;
	private Square from;
	
	public RemoveAction( Piece p ){
		this.p = p;
		this.from = p.square();
	}
	
	public Piece getPiece(){
		return p;
	}
	
	public Square getFrom(){
		return from;
	}
	
	void applyAction( Game g ){
		g.board().removePiece(p);
	}
}
