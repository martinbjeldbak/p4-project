package dk.aau.cs.d402f13.widgets;

import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.TrueTypeFont;

import dk.aau.cs.d402f13.helpers.ResourceHelper;
import dk.aau.cs.d402f13.utilities.errors.SimulatorError;

public class ScrollContainer extends Widget {
	private List<String> lines = null;
	private int y = 0;
	
	private int yStart = 0;
	private int barWidth = 19;
	private int barPadding = 14;
	
	public ScrollContainer(){
		scaleHeight( 0 );
		scaleWidth( 0 );
	}
	
	public void setLines( List<String> lines ){
		this.lines = lines;
	}
	
	private int amountShown(){
		TrueTypeFont font = ResourceHelper.getFont( "gtw", 16 );
		return getHeight() / font.getLineHeight();
	}
	
	private void drawScalable( Graphics g, int x, int y, int height , String name ) throws SimulatorError{
		Image topImg = ResourceHelper.getImage( "img/" + name + "_top.png" );
		Image middleImg = ResourceHelper.getImage( "img/" + name + "_middle.png" );
		Image bottomImg = ResourceHelper.getImage( "img/" + name + "_bottom.png" );
		
		g.drawImage( topImg, x, y );
		g.drawImage( bottomImg, x, y + height - bottomImg.getHeight() );

		//Draw middle part
		int yStart = y + topImg.getHeight();
		int yEnd = y + height - bottomImg.getHeight();
		int yStep = middleImg.getHeight();
		for( int iy = yStart; iy + yStep <= yEnd; iy += yStep )
			g.drawImage( middleImg, x, iy );
		
		//We might not be able to draw a full image at the end,
		//draw the remaining, but limit the height
		int rest = (yEnd - yStart) % yStep;
		if( rest > 0 )
			middleImg.draw( x, yEnd - rest, middleImg.getWidth(), rest );
	}
	
	@Override
	public void handleDraw( Graphics g ) throws SimulatorError{
		TrueTypeFont font = ResourceHelper.getFont( "gtw", 16 );
		int linePos = 0;
		g.setFont( font );
		g.setColor( Color.black );
		
		//Draw scroll bar
		int right = getWidth() - barWidth;
		int difference = lines.size() - amountShown();
		if( difference > 0 ){
			drawScalable( g, right, 0, getHeight(), "bar" );

			int height = getHeight() - barPadding * 2;
			float scale = height / (float)lines.size();
			drawScalable( g, right, (int)(y * scale) + barPadding
				,	(int)(amountShown() * scale )
				,	"handle"
				);
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
		if( x >= getWidth() - barWidth ){
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
