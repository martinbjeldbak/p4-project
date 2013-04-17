package dk.aau.cs.d402f13.utilities.types;

import java.util.ArrayList;
import java.util.List;

public class Board {

	List<Piece> pieces = new ArrayList<Piece>();
	
	public List<Piece> getPieces() {
		return pieces; 
	}
	
	public void setPieces(List<Piece> pieces) {
		this.pieces = pieces;
	}
	
	public void addPiece(Piece p) {
		this.pieces.add(p);
	}
}
	