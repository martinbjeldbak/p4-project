package dk.aau.cs.d402f13.simulator;

import java.util.Hashtable;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import dk.aau.cs.d402f13.utilities.types.Game;
import dk.aau.cs.d402f13.utilities.types.Gridboard;

public class SimulatedGame {
	Hashtable<String,Image> imgCache = new Hashtable<String,Image>();
	
	SimulatedGridboard board = null;
	Game game;
	
	public Image getImage( String path ) throws SlickException{
		if( imgCache.containsKey(path) ){
			return imgCache.get( path );
		}
		else{
			Image img = new Image( path );
			imgCache.put(path, img);
			return img;
		}
	}
	
	
	public String getTitle() {
		return game.getTitle();
	}
	
	public SimulatedGridboard getBoard(){
		return board;
	}
	
	public SimulatedGame (Game g){
		game = g;
		Object obj = g.getBoard();
		if( obj instanceof Gridboard )
			board = new SimulatedGridboard( this, (Gridboard)obj );
		else
			; //TODO:
	}
	
	public Game getGame(){
		return game;
	}
}

