package dk.aau.cs.d402f13.widgets;

import java.util.List;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.TrueTypeFont;

import dk.aau.cs.d402f13.helpers.ResourceHelper;
import dk.aau.cs.d402f13.helpers.TextHelper;
import dk.aau.cs.d402f13.utilities.errors.SimulatorError;
import dk.aau.cs.d402f13.utilities.errors.StandardError;


public class Message extends Widget {
	private String title;
	private String text;
	
	public Message( String title, String text, int x, int y, int width, int height ){
		setPosition( x, y );
		setFixed( width, height ); //TODO: evaluate this
		this.title = title;
		this.text = text;
	}

	public void setTitle( String title ){
		this.title = title;
	}
	public void setText( String text ){
		this.text = text;
	}
	
	public void handleDraw( Graphics g ) throws SimulatorError{
		Image paper = ResourceHelper.getImage( "img/message.png" );
		TrueTypeFont big = ResourceHelper.getFont( "gtw", 26 );
		TrueTypeFont small = ResourceHelper.getFont( "gtw", 16 );

		int posX = ( (getWidth() - getX()) - paper.getWidth() ) / 2 + getX();
		int posY = ( (getHeight() - getY()) - paper.getHeight() ) / 2 + getY();
		
		g.drawImage( paper, posX, posY );
		
		posX += 18;
		posY += 18;
		int width = 292;
		//int height = 266;

		//Draw title
		int nextLine = TextHelper.drawCenteredText( g, big, title, posX, posY, width );
		

		g.setFont( small );
		List<String> lines = TextHelper.wrapText( small, text, width );
		for( String line : lines ){
			g.drawString( line, posX, nextLine );
			nextLine += small.getLineHeight();
		}
	}

	@Override
	protected boolean handleMouseClicked( int button, int x, int y ) throws StandardError, SimulatorError{
		notify( Event.ACCEPT );
		return true;
	}
	@Override
	protected boolean handleMouseReleased( int button, int x, int y ){
		return true;
	}
}
