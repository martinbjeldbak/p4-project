package dk.aau.cs.d402f13.simulator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;


import dk.aau.cs.d402f13.gal.GameAbstractionLayer;
import dk.aau.cs.d402f13.helpers.ResourceHelper;
import dk.aau.cs.d402f13.utilities.errors.Error;
import dk.aau.cs.d402f13.utilities.errors.SimulatorError;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.gameapi.Action;
import dk.aau.cs.d402f13.utilities.gameapi.Game;
import dk.aau.cs.d402f13.utilities.gameapi.GridBoard;
import dk.aau.cs.d402f13.widgets.CenterContainer;
import dk.aau.cs.d402f13.widgets.Message;
import dk.aau.cs.d402f13.widgets.ScaleContainer;

/**
 * SimulatedGame is the glue which binds GAL, slick2d and widgets together.
 * It has the following responsibilities:
 * -  Load the game file
 * -  Make sure all get the correct state of the game
 * -  Glue slick2d drawing with widgets drawing
 * -  Glue slick2d input with widgets input
 * -  Handle exceptions thrown by GAL or widgets
 * 
 * @author spiller
 *
 */
public class SimulatedGame extends BasicGame {
	//Game mechanics stuff
	ScaleContainer sceneHandler = null;
	GridBoardWidget board = null;
	GameInfoWidget sidebar = null;
	GameAbstractionLayer gal;
	Game game;
	String gameFolder;
	
	
	public SimulatedGame( String path ){
		super( "Junta Simulator" );
		sceneHandler = new ScaleContainer( false );
		
		//Load the game from the file system
		try {
			gal = new GameAbstractionLayer( new FileInputStream( path ) );
			game = gal.getGame();
			if( game == null )
				handleSimulatorError( new SimulatorError( "GAL did not give us a game..." ) );
		} catch (Error e) {
			handleSimulatorError( new SimulatorError( e.getMessage() ) );
		} catch (FileNotFoundException e1) {
			String errorMsg = "Could not find game file: " + path;
			handleSimulatorError( new SimulatorError( errorMsg ) );
		}
		
		//Get the folder the game resides in
		Path filePath = Paths.get( path );
		gameFolder = filePath.getParent().toString() + "/";
		
		if( game != null ){
			//Setup widgets
			try{
				Object obj = game.getBoard();
				board = new GridBoardWidget( this, (GridBoard)obj );
				
				sidebar = new GameInfoWidget( this );
				
				if( board != null )
					sceneHandler.addObject( board );
				sceneHandler.addObject( sidebar );
			}
			catch( StandardError err ){
				handleStandardError( err );
			}
		}
	}
	
	public String getGameFolder(){ return gameFolder; }
	
	/**
	 * Access to the Game object, all must use this, without caching it
	 * @return The current Game object
	 */
	public Game getGame(){ return game; }
	
	/**
	 * Apply an Action to the game and update the Game reference
	 * @param a The Action to apply
	 * @throws StandardError
	 */
	public void applyAction( Action a ) throws StandardError{
		game = game.applyAction( a );
	}
	
	public void nextTurn() throws StandardError{
		game = game.nextTurn();
	}
	
	private void showError( String title, String error ){
		//Clear objects, as they might be the culprit
		sceneHandler.clearObjects();
		
		sceneHandler.addObject(
				new CenterContainer( new Message( title, error, 0,0, 335,318 ) )
			); //TODO: awfully off-center
	}
	
	private void handleStandardError( StandardError stdErr ){
		String msg = "At: " + stdErr.getLine() + ":" + stdErr.getColumn();
		msg += "\n" + stdErr.getMessage();
		
		
		showError( "Fatal fault in game", msg );
		stdErr.printStackTrace();
	}
	
	private void handleSimulatorError( SimulatorError stdErr ){
		showError( "Fatal fault in simulator", stdErr.getMessage() );
		stdErr.printStackTrace();
	}
	
	@Override
	public void render( GameContainer gc, Graphics g ){
		g.setAntiAlias( true );
		
		//Fill background
		g.setColor( Color.white );
		Shape shape = new Rectangle( 0,0, gc.getWidth(), gc.getHeight() );
		try {
			g.texture( shape, ResourceHelper.getImage( "img/wood.png" ) );
		} catch (SimulatorError e) {
			handleSimulatorError( e );
		}
		
		try{
			//Draw board
			sceneHandler.draw( g );
		}
		catch( StandardError err ){
			handleStandardError( err );
		} catch (SimulatorError e) {
			handleSimulatorError( e );
		}
	}
	
	@Override
	public void mousePressed( int button, int x, int y ){
		try{
			sceneHandler.mouseClicked( button, x, y );
		}
		catch( StandardError err ){
			handleStandardError( err );
		} catch (SimulatorError e) {
			handleSimulatorError( e );
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
		} catch (SimulatorError e) {
			handleSimulatorError( e );
		}
	}
	
	@Override
	public String getTitle(){
		try {
			if( game != null )
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
		//Update the sizes of ScaleContainers
		sceneHandler.setPosition( 0, 0 ); //OCD
		sceneHandler.setSize( gc.getWidth(), gc.getHeight() );
		sceneHandler.adjustSizes();
	}

	@Override
	public void update( GameContainer gc, int arg1 ) throws SlickException {
		Input in = new Input(arg1); //TODO: what in constructor?
		if( in.isKeyDown(Input.KEY_F5) )
			try {
				restartGame();
			} catch (StandardError e) {
				handleStandardError( e );
			}
	}

	/**
	 * Remove all progress and start at the beginning of the game
	 * @throws StandardError
	 */
	public void restartGame() throws StandardError {
		game = gal.getGame();
	}
}

