package dk.aau.cs.d402f13.simulator;

import java.util.ArrayList;
import java.util.List;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import dk.aau.cs.d402f13.utilities.types.Game;
import dk.aau.cs.d402f13.utilities.types.Gridboard;

public class SimulatedGame {
	List<SimulatedPieces> pieces;
	
	SimulatedGridboard board = null;
	Game game;
	
	
	public String getTitle() {
		return game.getTitle();
	}
	
	public void load_resources(){
		
	}
	
	public SimulatedGridboard getBoard(){
		return board;
	}
	
	public SimulatedGame (Game g){
		game = g;
		Object obj = g.getBoard();
		if( obj instanceof Gridboard )
			board = new SimulatedGridboard( (Gridboard)obj );
		else
			; //TODO:
		pieces = new ArrayList<SimulatedPieces>();
	}
	
	public List<SimulatedPieces> getPieces(){
		return pieces;
	}
}

