package dk.aau.cs.d402f13.simulator;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import widgets.Label;
import widgets.ObjectContainer;
import widgets.ScrollContainer;

import dk.aau.cs.d402f13.utilities.types.Action;
import dk.aau.cs.d402f13.utilities.types.Game;

public class SimulatedGameInfo extends ObjectContainer {
	private SimulatedGame game;
	private ScrollContainer historyList;
	
	private Label title;
	private Label player;
	
	SimulatedGameInfo( SimulatedGame game ){
		super( true );
		//We want to use the image dimensions as the size, however,
		//as an Image in Slick can't be loaded before an OpenGl context
		//have been set up, the size is hard-coded here.
		//We could override the getMin/Max() methods, but that is ugly as well.
		setFixed( 386, 536 );
		
		setPadding( 50, 50, 50, 25 );
		
		this.game = game;
		
		//Add the objects, while keeping references to them
		addObject( title = new Label( "gtw", 32, game.getGame().title() ) );
		addObject( player = new Label( "gtw", 16, "Player" ) );
		addObject( historyList = new ScrollContainer() );
	}
	
	@Override
	public void draw( Graphics g ){
		Game game = this.game.getGame();
		//Draw side bar
		Image side = ResourceHandler.getImage( "img/book.png" );
		g.drawImage( side, 0, 0 );
		
		title.setString( game.title() );
		
		int position = game.players().indexOf( game.currentPlayer() );
		player.setString( "Player " + ( position + 1) );

		//Display Action history
		List<String> historyLines = new ArrayList<String>();
		for( Action a : game.history() )
			historyLines.add( ActionHelper.humanReadable( game, a ) );
		historyList.setLines( historyLines );
	}
}
