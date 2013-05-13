package dk.aau.cs.d402f13.simulator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;


import dk.aau.cs.d402f13.gal.GameAbstractionLayer;
import dk.aau.cs.d402f13.helpers.ResourceHelper;
import dk.aau.cs.d402f13.utilities.errors.Error;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.gameapi.Game;
import dk.aau.cs.d402f13.utilities.gameapi.GridBoard;
import dk.aau.cs.d402f13.widgets.ScaleContainer;

public class SimulatedGame extends BasicGame {
	//Game mechanics stuff
	ScaleContainer sceneHandler = null;
	GridBoardWidget board = null;
	GameInfoWidget sidebar = null;
	Game game;
	
	
	public SimulatedGame( String path ) throws CloneNotSupportedException{
		super( "Junta Simulator" );
		
		GameAbstractionLayer gal;
		try {
			FileInputStream fis = new FileInputStream( path );
			gal = new GameAbstractionLayer( fis );
			game = gal.getGame();
			if( game == null )
				System.out.println( "fsdjafkldsjaklf" );
		} catch (Error e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try{
			Object obj = game.getBoard();
		//	if( obj instanceof GridBoard )
				board = new GridBoardWidget( this, (GridBoard)obj );
	//		else
				; //TODO:
			
			sidebar = new GameInfoWidget( this );
			
			sceneHandler = new ScaleContainer( false );
			if( board != null )
				sceneHandler.addObject( board );
			sceneHandler.addObject( sidebar );
		}
		catch( StandardError err ){
			handleStandardError( err );
		}
	}
	
	public Game getGame(){ return game; }
	
	private void handleStandardError( StandardError stdErr ){
		//TODO:
		System.out.println( "StandardError thrown, but not handled : \\" );
		stdErr.printStackTrace();
	}
	
	@Override
	public void render( GameContainer gc, Graphics g ){
		g.setAntiAlias( true );
		
		//Fill background
		g.setColor( Color.white );
		Shape shape = new Rectangle( 0,0, gc.getWidth(), gc.getHeight() );
		g.texture( shape, ResourceHelper.getImage( "img/wood.png" ) );
		
		try{
			//Draw board
			sceneHandler.startDraw( g );
		}
		catch( StandardError err ){
			handleStandardError( err );
		}
	}
	
	@Override
	public void mousePressed( int button, int x, int y ){
		try{
			sceneHandler.mouseClicked( button, x, y );
		}
		catch( StandardError err ){
			handleStandardError( err );
		}
    }
	
	@Override
	public void mouseDragged( int oldx, int oldy, int newx, int newy ){
		try{
			sceneHandler.mouseDragged( oldx, oldy, newx, newy );
		}
		catch( StandardError err ){
			handleStandardError( err );
		}
	}
	
	public void mouseReleased( int button, int x, int y ){
		try{
			sceneHandler.mouseReleased( button, x, y );
		}
		catch( StandardError err ){
			handleStandardError( err );
		}
	}
	
	@Override
	public String getTitle(){
		try {
			return "Junta Simulator - " + game.getTitle();
		} catch (StandardError e) {
			handleStandardError( e );
		}
		return "Junta Simulator";
	}

	public static void main( String[] args ) throws SlickException, CloneNotSupportedException {
		if( args.length != 1 )
			return;
		
		SimulatedGame simulator = new SimulatedGame( args[0] );
		AppGameContainer app = new AppGameContainer( simulator );
		//app.setDisplayMode( 1366, 768, true ); //full-screen
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

