package dk.aau.cs.d402f13.simulator;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import widgets.ObjectContainer;

import dk.aau.cs.d402f13.utilities.types.Game;
import dk.aau.cs.d402f13.utilities.types.Gridboard;

public class SimulatedGame extends BasicGame {
	//Game mechanics stuff
	ObjectContainer sceneHandler = null;
	SimulatedGridboard board = null;
	SimulatedGameInfo sidebar = null;
	Game game;
	
	
	public SimulatedGame( String path ) throws CloneNotSupportedException {
		super( "Junta Simulator" );
		
		//TODO: load from file system
		game = new ChessGame();
		Object obj = game.board();
		if( obj instanceof Gridboard )
			board = new SimulatedGridboard( this, (Gridboard)obj );
		else
			; //TODO:
		
		sidebar = new SimulatedGameInfo( this );
		
		sceneHandler = new ObjectContainer( false );
		sceneHandler.addObject( board );
		sceneHandler.addObject( sidebar );
	}
	
	public Game getGame(){ return game; }
	
	@Override
	public void render( GameContainer gc, Graphics g ){
		g.setAntiAlias( true );
		
		//Fill background
		g.setColor( Color.white );
		Shape shape = new Rectangle( 0,0, gc.getWidth(), gc.getHeight() );
		g.texture( shape, ResourceHandler.getImage( "img/wood.png" ) );
		
		//Draw board
		sceneHandler.startDraw( g );
	}
	
	@Override
	public void mousePressed( int button, int x, int y ){
		sceneHandler.mouseClicked( button, x, y );
    }
	
	@Override
	public void mouseDragged( int oldx, int oldy, int newx, int newy ){
		sceneHandler.mouseDragged( oldx, oldy, newx, newy );
	}
	
	public void mouseReleased( int button, int x, int y ){
		sceneHandler.mouseReleased( button, x, y );
	}
	
	@Override
	public String getTitle(){
		return "Junta Simulator - " + game.title();
	}

	public static void main( String[] args ) throws SlickException, CloneNotSupportedException {
		SimulatedGame simulator = new SimulatedGame( "chess.junta" );
		AppGameContainer app = new AppGameContainer( simulator );
		//app.setDisplayMode( 1366, 768, true ); //fullscreen
		app.setDisplayMode( 1024, 600, false );
		app.setTargetFrameRate( 60 );
		app.setVSync( true );
		app.start();
		
	}

	@Override
	public void init( GameContainer gc ) throws SlickException {
		sceneHandler.setPosition( 0, 0 ); //OCD
		sceneHandler.setSize( gc.getWidth(), gc.getHeight() );
		sceneHandler.adjustSizes();
	}

	@Override
	public void update( GameContainer arg0, int arg1 ) throws SlickException { }
}

