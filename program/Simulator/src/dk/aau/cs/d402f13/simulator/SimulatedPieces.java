package dk.aau.cs.d402f13.simulator;

import org.newdawn.slick.Image;

import dk.aau.cs.d402f13.utilities.types.Piece;
import dk.aau.cs.d402f13.utilities.types.Square;

public class SimulatedPieces {
	private Piece piece;
	private String name;
	Image PieceImg = null;
	
	public void setImage( Image img ){
		PieceImg = img;
	}

	public String getName() {
		return name;
	}
	
	public Square getSquare(){
		return piece.getSquare();
	}

	public String getImgPath() {
		return piece.getImgPath();
	}

	
	public SimulatedPieces( Piece p ) {
		piece = p;
	}
	
	public int imageSize(){
		if( PieceImg != null ){
			return Math.max(PieceImg.getHeight(), PieceImg.getWidth());
		}
		else
			return -1;
	}
}
