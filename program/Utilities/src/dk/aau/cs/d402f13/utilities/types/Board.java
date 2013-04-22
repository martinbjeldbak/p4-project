package dk.aau.cs.d402f13.utilities.types;

import java.util.ArrayList;
import java.util.List;

public class Board {

	private List<Piece> pieces = new ArrayList<Piece>();
	protected List<Square> squares = new ArrayList<Square>();
	
	public List<Piece> getPieces() {
		return pieces; 
	}
	
	public void setPieces(List<Piece> pieces) {
		this.pieces = pieces;
	}
	
	public void addPiece(Piece p) {
		this.pieces.add(p);
	}
	
	public void removePiece(Piece p ){
		if( !pieces.remove(p)){
			//TODO: throw exception
		}
	}
	
	public Piece findPieceOnSquare( Square s ){
		for( Piece p : pieces ){
			if( p.square() == s)
				return p;
		}
		return null;
	}
}
	