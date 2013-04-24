package dk.aau.cs.d402f13.simulator;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

public class TextHelper {
	static public int drawCenteredText(
			Graphics g, TrueTypeFont font, String text, int x, int y, int width ){

		int titleWidth = font.getWidth( text );
		int titleOffset = ( width - titleWidth ) / 2;
		
		//Draw text
		g.setColor( Color.black );
		g.setFont( font );
		g.drawString( text, x + titleOffset, y );
		
		return font.getLineHeight() + y;
	}
	
	static public List<String> wrapText( TrueTypeFont font, String text, int width ){
		text = text.trim();
		
		List<String> lines = new ArrayList<String>();
		String[] words = text.split( " " );
		
		String line = "";
		for( String word : words ){
			if( font.getWidth( line + " " + word ) < width )
				line += " " + word;
			else
				break;
		}
		
		//couldn't break up line
		if( line == "" )
			line.substring( 0, width );
		
		//Remove first space
		line = line.substring( 1 );
		
		//Add line and do the remaining recursively
		lines.add( line );
		String remainingText = text.substring( line.length() );
		
		if( remainingText.length() > 0 )
			lines.addAll( wrapText( font, remainingText, width ) );
		
		return lines;
	}
}
