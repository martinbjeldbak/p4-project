package dk.aau.cs.d402f13.simulator;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class SimulatedPieces {
	
	//List listOfMoves = new ArrayList<Moves>();
	private String name;
	//Moves moves = new Moves(listOfMoves);
	Image PieceImg = null;
	
	private int coordinatX;
	private int coordinatY;

	public String getName() {
		return name;
	}

	public void setName(String value) {
		this.name = value;
	}

	public int getCoordinatX() {
		return coordinatX;
	}

	public void setCoordinatX(int value) {
		this.coordinatX = value;
	}

	public int getCoordinatY() {
		return coordinatY;
	}

	public void setCoordinatY(int value) {
		this.coordinatY = value;
	}

	
	public SimulatedPieces(int coordX, int coordY, Image img) throws SlickException {
		this.coordinatX = coordX;
		this.coordinatY = coordY;
		PieceImg = img;
	}
	
	public int imageSize(){
		if( PieceImg != null ){
			return Math.max(PieceImg.getHeight(), PieceImg.getWidth());
		}
		else
			return -1;
	}
}
