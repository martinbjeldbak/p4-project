package dk.aau.cs.d402f13.simulator;

import java.util.ArrayList;
import java.util.List;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class SimulatedGame {
	List<SimulatedPieces> pieces;
	
	SimulatedBoard board = null;
	
	private String title;
	
	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(String value) {
		this.title = value;
	}
	
	public void addPiece( SimulatedPieces p ){
		pieces.add( p );
	}
	
	public void load_resources(){
		
	}
	
	public SimulatedGame (Game g){
		pieces = new ArrayList<SimulatedPieces>();
	}
	
	public List<SimulatedPieces> getPieces(){
		return pieces;
	}
}

