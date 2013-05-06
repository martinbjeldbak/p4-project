package widgets;

import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

import dk.aau.cs.d402f13.simulator.ResourceHandler;

public class ScrollContainer extends SceneObject {
	private List<String> lines = null;
	private int y = 0;
	
	private int yStart = 0;
	
	public ScrollContainer(){
		scaleHeight( 0 );
		scaleWidth( 0 );
	}
	
	public void setLines( List<String> lines ){
		this.lines = lines;
	}
	
	private int amountShown(){
		TrueTypeFont font = ResourceHandler.getFont( "gtw", 16 );
		return getHeight() / font.getLineHeight();
	}
	
	@Override
	public void draw( Graphics g ){
		TrueTypeFont font = ResourceHandler.getFont( "gtw", 16 );
		int linePos = 0;
		g.setFont( font );
		g.setColor( Color.black );
		g.drawRect( 0, 0, getWidth(), getHeight() );
		
		//Draw scroll bar
		int right = getWidth() - 8;
		int difference = lines.size() - amountShown();
		if( difference > 0 ){
			float scale = getHeight() / (float)lines.size();
			g.drawLine( (float)right, y * scale, (float)right, (y + amountShown()) * scale );
		}
		
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
	}
	
	@Override
	protected boolean handleMouseClicked( int button, int x, int y ){
		if( x >= getWidth() - 8 ){
			startDragging( x, y );
			yStart = this.y;
			return true;
		}
		
		return false;
	}

	@Override
	protected boolean handleMouseDragged( int oldX, int oldY, int newX, int newY ){
		int diff = newY - dragStartY;
		y = yStart + (int)(diff / (getHeight() / (float)lines.size()));
		
		y = Math.max( 0, y );
		y = Math.min( y, Math.max( 0, lines.size() - amountShown() ) );
		
		return true;
	}
}
