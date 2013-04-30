package dk.aau.cs.d402f13.simulator;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

public class TextHelper {
	/**
	 * Draw a String centered
	 * @param g Graphics to draw with
	 * @param font Font to draw the text
	 * @param text The text to center
	 * @param x Left-most position  
	 * @param y Vertical position of the text
	 * @param width The available horizontal space to center on
	 * @return The vertical position right below the drawn text
	 */
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
	
	/**
	 * Wrap a long String into smaller Strings, so that each one does not
	 * exceed a specified width. Breaks the String on spaces.
	 * @param font The font which specifies the width of the glyphs
	 * @param text The String to wrap
	 * @param width The available width
	 * @return A List containing each line
	 */
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
