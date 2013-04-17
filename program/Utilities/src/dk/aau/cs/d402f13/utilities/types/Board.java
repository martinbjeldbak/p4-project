package dk.aau.cs.d402f13.utilities.types;

import java.util.ArrayList;
import java.util.List;

public class Board {

	protected List<Piece> pieces = new ArrayList<Piece>();
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
}
	