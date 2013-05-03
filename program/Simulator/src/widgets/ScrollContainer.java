package widgets;

import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

import dk.aau.cs.d402f13.simulator.ResourceHandler;

public class ScrollContainer extends SceneObject {
	private List<String> lines = null;
	private int y = 0;
	
	public ScrollContainer(){
		scaleHeight( 0 );
		scaleWidth( 0 );
	}
	
	public void setLines( List<String> lines ){
		this.lines = lines;
	}
	
	@Override
	public void draw( Graphics g ){
		TrueTypeFont font = ResourceHandler.getFont( "gtw", 16 );
		int linePos = 0;
		g.setFont( font );
		g.setColor( Color.black );
		g.drawRect( 0, 0, getWidth(), getHeight() );
		
		if( lines != null ){
			for( int i=y; i<lines.size(); i++ ){
				//Stop when we reach the bottom
				if( linePos + font.getLineHeight() > getHeight() )
					break;
				
				//Draw text
				g.drawString( lines.get(i), 0, linePos );
				linePos += font.getLineHeight();
			}
		}
		
		super.draw( g );
	}
}
