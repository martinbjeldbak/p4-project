package dk.aau.cs.d402f13.utilities.types;

public class RemoveAction extends Action {
	private Piece p;
	
	public RemoveAction( Piece p ){
		this.p = p;
	}
	
	public Piece getPiece(){
		return p;
	}
	
	void applyAction( Game g ){
		g.board().removePiece(p);
	}
}
