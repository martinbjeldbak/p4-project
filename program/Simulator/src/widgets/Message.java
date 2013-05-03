package widgets;

import java.util.List;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.TrueTypeFont;

import dk.aau.cs.d402f13.simulator.ResourceHandler;
import dk.aau.cs.d402f13.simulator.TextHelper;


public class Message extends SceneObject {
	private String title;
	private String text;
	
	public Message( String title, String text, int x, int y, int width, int height ){
		setPosition( x, y );
		setFixed( width, height );
		this.title = title;
		this.text = text;
	}
	
	public void draw( Graphics g ){
		Image paper = ResourceHandler.getImage( "img/message.png" );
		TrueTypeFont big = ResourceHandler.getFont( "gtw", 32 );
		TrueTypeFont small = ResourceHandler.getFont( "gtw", 16 );

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
	public boolean mouseClicked( int button, int x, int y ){
		createEvent( Event.ACCEPT );
		return true;
	}
	@Override
	public boolean mouseReleased( int button, int x, int y ){
		return true;
	}
}
