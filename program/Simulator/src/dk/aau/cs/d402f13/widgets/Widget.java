package dk.aau.cs.d402f13.widgets;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Graphics;

import dk.aau.cs.d402f13.utilities.errors.SimulatorError;
import dk.aau.cs.d402f13.utilities.errors.StandardError;

public class Widget {
	private int x = 0;
	private int y = 0;
	private int width = 0;
	private int height = 0;
	
	private int minWidth = 0, minHeight = 0;
	private int maxWidth = 0, maxHeight = 0;
	
	protected boolean dragging = false;
	protected int dragStartX = 0;
	protected int dragStartY = 0;
	
	protected List<Widget> widgets = new ArrayList<Widget>();
	private List<Widget> listeners = new ArrayList<Widget>();

	public int getX(){ return x; }
	public int getY(){ return y; }
	public int getWidth(){ return width; }
	public int getHeight(){ return height; }
	
	public int getMinWidth(){ return minWidth; }
	public int getMinHeight(){ return minHeight; }
	public int getMaxWidth(){ return maxWidth; }
	
	public void adjustSizes(){
		for( Widget o : widgets )
			o.adjustSizes();
	}
	
	//Set type of sizing
	public void setFixed( int width, int height ){
		minWidth = this.width = maxWidth = width;
		minHeight = this.height = maxHeight = height;
	}
	public void scaleWidth( int minWidth, int maxWidth ){
		this.minWidth = minWidth;
		this.maxWidth = maxWidth;
		if( width > maxWidth )
			width = maxWidth;
	}
	public void scaleHeight( int minHeight, int maxHeight ){
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
		if( height > maxHeight )
			height = maxHeight;
	}
	public void scaleWidth( int minWidth ){
		scaleWidth( minWidth, Integer.MAX_VALUE );
	}
	public void scaleHeight( int minHeight ){
		scaleHeight( minHeight, Integer.MAX_VALUE );
	}
	
	public Boolean isScalingWidth(){
		return getMinWidth() != getMaxWidth();
	}
	public Boolean isScalingHeight(){
		return getMinHeight() != getMaxHeight();
	}
	
	public boolean containsPoint( int x, int y ){
		if( x < this.x || y < this.y )
			return false;
		if( x > this.x + width || y > this.y + height )
			return false;
		return true;
	}
	
	public int getMaxHeight(){ return maxHeight; }
	
	protected void startDragging( int x, int y ){
		dragging = true;
		dragStartX = x;
		dragStartY = y;
	}
	protected void stopDragging(){
		dragging = false;
	}
	protected boolean childrenDragging(){
		for( Widget o : widgets ){
			if( o.dragging )
				return true;
			else
				if( o.childrenDragging() )
					return true;
		}
		return false;
	}
	
	public void addObject( Widget o ){
		widgets.add( o );
	}
	public void removeObject( Widget o ){
		widgets.remove( o );
	}
	public void clearObjects(){
		widgets.clear();
	}
	
	final public void startDraw( Graphics g ) throws StandardError, SimulatorError{
		for( Widget o : widgets ){
			g.translate( o.getX(), o.getY() );
		//TODO: clip appears to use absolute coordinates
		//	g.setClip( o.getX(), o.getY(), o.getWidth(), o.getHeight() );
			
			o.draw( g );
			o.startDraw( g );
			
		//	g.clearClip();
			g.translate( -o.getX(), -o.getY() );
		}
	}
	
	protected void draw( Graphics g ) throws StandardError, SimulatorError{ }
	
	public void setSize( int newWidth, int newHeight ){
		width = newWidth;
		height = newHeight;
	}
	
	public void setPosition( int newX, int newY ){
		x = newX;
		y = newY;
	}
	
	
	protected boolean handleMouseClicked( int button, int x, int y ) throws StandardError, SimulatorError{
		return false;
	}
	protected boolean handleMouseDragged( int oldX, int oldY, int newX, int newY ) throws StandardError{
		return false;
	}
	protected boolean handleMouseReleased( int button, int x, int y ) throws StandardError, SimulatorError{
		return false;
	}
	
	final public boolean mouseClicked( int button, int x, int y ) throws StandardError, SimulatorError{
		for( Widget o : widgets )
			if( o.containsPoint( x, y ) ){
				if( o.mouseClicked( button, x - o.getX(), y - o.getY() ) )
					return true;
				else
					if( o.handleMouseClicked( button, x - o.getX(), y - o.getY() ) )
						return true;
			}
		return false;
	}
	
	final public boolean mouseDragged( int oldX, int oldY, int newX, int newY ) throws StandardError{
		for( Widget o : widgets ){
			int x1 = oldX - o.getX();
			int y1 = oldY - o.getY();
			int x2 = newX - o.getX();
			int y2 = newY - o.getY();
			
			if( o.mouseDragged( x1, y1, x2, y2 ) )
				return true;
			else
				if( o.dragging && o.handleMouseDragged( x1, y1, x2, y2 ) )
					return true;
		}
		
		return false;
	}
	
	final public boolean mouseReleased( int button, int x, int y ) throws StandardError, SimulatorError{
		for( Widget o : widgets ){
			boolean handled = false;
			if( o.mouseReleased( button, x - o.getX(), y - o.getY() ) )
				handled = true;
			else
				if( o.containsPoint( x, y ) )
					if( o.handleMouseReleased( button, x - o.getX(), y - o.getY() ) )
						handled = true;
			
			o.stopDragging();
			if( handled )
				return true;
		}
		return false;
	}
	
	public enum Event{
		ACCEPT,
		REJECT,
		CANCEL,
		MOVE_GENERATED
	}
	
	public void acceptEvent( Widget obj, Event event ) throws StandardError{
		//Nothing here, this is intended, as the default is no action
	}
	
	protected void createEvent( Event event ) throws StandardError{
		for( Widget o : listeners )
			o.acceptEvent( this, event );
	}
	
	public void startListening( Widget obj ){
		listeners.add( obj );
	}
	public void stopListening( Widget obj ){
		listeners.remove( obj );
	}
}
