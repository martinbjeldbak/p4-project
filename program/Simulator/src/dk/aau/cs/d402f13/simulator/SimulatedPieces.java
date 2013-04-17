package dk.aau.cs.d402f13.simulator;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import dk.aau.cs.d402f13.utilities.types.Piece;
import dk.aau.cs.d402f13.utilities.types.Square;

public class SimulatedPieces {
	Piece piece;
	
	//List listOfMoves = new ArrayList<Moves>();
	private String name;
	//Moves moves = new Moves(listOfMoves);
	Image PieceImg = null;
	
	public void setImage( Image img ){
		PieceImg = img;
	}

	public String getName() {
		return name;
	}

	public void setName(String value) {
		this.name = value;
	}
	
	public Square getSquare(){
		return piece.getSquare();
	}

	public String getImgPath() {
		return piece.getImgPath();
	}

	
	public SimulatedPieces( Piece p ) {
		piece = p;
		//PieceImg = img;
	}
	
	public int imageSize(){
		if( PieceImg != null ){
			return Math.max(PieceImg.getHeight(), PieceImg.getWidth());
		}
		else
			return -1;
	}
}
