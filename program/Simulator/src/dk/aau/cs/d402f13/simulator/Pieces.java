package dk.aau.cs.d402f13.simulator;

import org.newdawn.slick.Image;

public class Pieces {
	
	//List listOfMoves = new ArrayList<Moves>();
	private String name;
	//Moves moves = new Moves(listOfMoves);
	Image PieceImg = null;
	
	private float coordinatX;
	private float coordinatY;
	private float scale;

	public String getName() {
		return name;
	}

	public void setName(String value) {
		this.name = value;
	}

	public float getCoordinatX() {
		return coordinatX;
	}

	public void setCoordinatX(float value) {
		this.coordinatX = value;
	}

	public float getCoordinatY() {
		return coordinatY;
	}

	public void setCoordinatY(float value) {
		this.coordinatY = value;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float value) {
		this.scale = value;
	}
	
	public Pieces(float coordX, float coordY, float scale) {
		this.coordinatX = coordX;
		this.coordinatY = coordY;
		this.scale = scale;
	}
}
