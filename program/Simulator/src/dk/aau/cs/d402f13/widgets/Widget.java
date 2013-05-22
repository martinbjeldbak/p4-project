package dk.aau.cs.d402f13.widgets;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Graphics;

import dk.aau.cs.d402f13.utilities.errors.SimulatorError;
import dk.aau.cs.d402f13.utilities.errors.StandardError;

/**
 * Base class for all objects which need to be shown. Widget handles
 * positioning, drawing, mouse input and event handling. It may contain
 * sub-Widgets, and propagates drawing and input so it is easy to handle
 * in sub-Widgets.
 * 
 * @author spiller
 *
 */
public class Widget {
	//Position and size of Widget
	private int x = 0;
	private int y = 0;
	private int width = 0;
	private int height = 0;
	
	//How the size can be changed
	//Override the getters to provide dynamic behavior
	private int minWidth = 0, maxWidth = Integer.MAX_VALUE;
	private int minHeight = 0, maxHeight = Integer.MAX_VALUE;
	
	//Whether dragging or not, and the position it started at
	protected boolean dragging = false;
	protected int dragStartX = 0;
	protected int dragStartY = 0;
	
	//Sub-Widgets which this Widget contains
	protected List<Widget> widgets = new ArrayList<Widget>();
	
	//List of observer Widgets which need to be notified of events
	private List<Widget> observers = new ArrayList<Widget>();

	//Getters to position and size
	public int getX(){ return x; }
	public int getY(){ return y; }
	public int getWidth(){ return width; }
	public int getHeight(){ return height; }
	
	//Getters to min/max size
	public int getMinWidth(){ return minWidth; }
	public int getMinHeight(){ return minHeight; }
	public int getMaxWidth(){ return maxWidth; }
	public int getMaxHeight(){ return maxHeight; }
	
	//Grouping of events Widgets can observe
	public enum Event{
		ACCEPT,
		REJECT,
		CANCEL,
		MOVE_GENERATED
	}
	
	/**
	 * Change current size
	 * @param newWidth New width
	 * @param newHeight New height
	 */
	public void setSize( int newWidth, int newHeight ){
		//Enforce min/max size
		width = Math.max( Math.min( newWidth, maxWidth ), minWidth );
		height = Math.max( Math.min( newHeight, maxHeight ), minHeight );
	}
	
	/**
	 * Change the current position of Widget
	 * @param newX New horizontal position, relative to parent
	 * @param newY New vertical position, relative to parent
	 */
	public void setPosition( int newX, int newY ){
		x = newX;
		y = newY;
	}
	
	/**
	 * Set size to be constant and update size
	 * @param width Wanted unchangeable width
	 * @param height Wanted unchangeable height
	 */
	public void setFixed( int width, int height ){
		minWidth = this.width = maxWidth = width;
		minHeight = this.height = maxHeight = height;
	}
	
	/**
	 * Set width to be variable
	 * @param minWidth Minimum width Widget is allowed to be
	 * @param maxWidth Maximum width Widget is allowed to be
	 */
	public void scaleWidth( int minWidth, int maxWidth ){
		this.minWidth = minWidth;
		this.maxWidth = maxWidth;
		if( width > maxWidth )
			width = maxWidth;
	}
	
	/**
	 * Set height to be variable
	 * @param minHeight Minimum height Widget is allowed to be
	 * @param maxHeight Maximum height Widget is allowed to be
	 */
	public void scaleHeight( int minHeight, int maxHeight ){
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
		if( height > maxHeight )
			height = maxHeight;
	}
	
	/**
	 * Set height to be variable and extend infinitely
	 * @param minWidth The minimum width Widget is allowed to be
	 */
	public void scaleWidth( int minWidth ){
		scaleWidth( minWidth, Integer.MAX_VALUE );
	}
	
	/**
	 * Set width to be variable and extend infinitely
	 * @param minHeight The mimumn height Widget is allowed to be
	 */
	public void scaleHeight( int minHeight ){
		scaleHeight( minHeight, Integer.MAX_VALUE );
	}
	
	/**
	 * Check if width is variable
	 * @return true if width is variable
	 */
	public Boolean isScalingWidth(){
		return getMinWidth() != getMaxWidth();
	}
	
	/**
	 * Check if height is variable
	 * @return true is height is variable
	 */
	public Boolean isScalingHeight(){
		return getMinHeight() != getMaxHeight();
	}
	
	/**
	 * Adjust the position and size of sub-Widgets, based on this
	 * Widget's size. Applies recursively to sub-Widgets.
	 */
	public void adjustSizes(){
		for( Widget o : widgets )
			o.adjustSizes();
	}
	
	/**
	 * Checks if a coordinate is within this object, relative to
	 * its parent object.
	 * @param x horizontal position
	 * @param y vertical position
	 * @return true if the coordinate is within this Widget
	 */
	public boolean containsPoint( int x, int y ){
		if( x < this.x || y < this.y )
			return false;
		if( x > this.x + width || y > this.y + height )
			return false;
		return true;
	}
	
	/**
	 * Accept a drag and setup state
	 * @param x Horizontal starting position in local coordinates
	 * @param y Vertical starting position in local coordinates
	 */
	protected void startDragging( int x, int y ){
		dragging = true;
		dragStartX = x;
		dragStartY = y;
	}
	
	/**
	 * Stop a drag
	 */
	protected void stopDragging(){
		dragging = false;
	}
	
	/**
	 * Add a sub-Widget
	 * @param o The Widget to add
	 */
	public void addObject( Widget o ){
		widgets.add( o );
	}
	
	/**
	 * Remove a sub-Widget
	 * @param o The Widget to remove
	 */
	public void removeObject( Widget o ){
		widgets.remove( o );
	}
	
	/**
	 * Remove all sub-Widget
	 */
	public void clearObjects(){
		widgets.clear();
	}
	
	/**
	 * Provide custom drawing of a Widget. All drawing must be done
	 * in local coordinates.
	 * @param g Graphics object to draw with
	 * @throws StandardError
	 * @throws SimulatorError
	 */
	protected void handleDraw( Graphics g ) throws StandardError, SimulatorError{ }
	
	/**
	 * Handle mouse button presses
	 * @param button Button which was pressed
	 * @param x Horizontal position (in local coordinates) where button was pressed
	 * @param y Vertical position (in local coordinates) where button was pressed
	 * @return true if the press was handled, false if not
	 * @throws StandardError
	 * @throws SimulatorError
	 */
	protected boolean handleMouseClicked( int button, int x, int y ) throws StandardError, SimulatorError{
		return false;
	}
	
	/**
	 * Handle mouse drags, is only called if starDragging() was called.
	 * @param oldX Previous x-position of mouse
	 * @param oldY Previous y-position of mouse
	 * @param newX Current x-position of mouse
	 * @param newY Current y-position of mouse
	 * @return true if the drag was handled, false if not
	 * @throws StandardError
	 */
	protected boolean handleMouseDragged( int oldX, int oldY, int newX, int newY ) throws StandardError{
		return false;
	}
	
	/**
	 * Handle mouse button releases
	 * @param button Button which was released
	 * @param x Horizontal position (in local coordinates) where button was released
	 * @param y Vertical position (in local coordinates) where button was released
	 * @return true if the release was handled, false if not
	 * @throws StandardError
	 * @throws SimulatorError
	 */
	protected boolean handleMouseReleased( int button, int x, int y ) throws StandardError, SimulatorError{
		return false;
	}
	
	/**
	 * Draw the Widget and recursively all sub-Widgets, at its current
	 * position.
	 * @param g Graphics object to draw with
	 * @throws StandardError
	 * @throws SimulatorError
	 */
	final public void draw( Graphics g ) throws StandardError, SimulatorError{
		draw( g, 0, 0 );
	}
	
	/**
	 * Draws, but keeps track of absolute coordinates 
	 * @param g Graphics object to draw with
	 * @param absX absolute x coordinate
	 * @param absY absolute y coordinate
	 * @throws StandardError
	 * @throws SimulatorError
	 */
	private void draw( Graphics g, int absX, int absY ) throws StandardError, SimulatorError{
		/* The thing to notice here is that there are two methods, draw()
		 * and handleDraw(). Specializations can override handleDraw() to
		 * provide custom drawing functionality. draw() handles setup,
		 * cleanup and provides the recursive behavior, so we don't need to
		 * worry about it when overriding handleDraw().
		 * Furthermore, draw() is final, so we can't change the behavior.
		 * 
		 * The same pattern is used for mouseClicked(), mousedReleased()
		 * and mouseDragged().
		 */
		for( Widget o : widgets ){
			//Transform the current view, so we can use local coordinates
			//when drawing. (Relative coordinates)
			g.translate( o.getX(), o.getY() );
			
			//Set clipping to avoid Widgets to draw outside its boundaries
			g.setClip( absX + o.getX(), absY + o.getY(), o.getWidth(), o.getHeight() );
			
			o.handleDraw( g );
			o.draw( g, absX + o.getX(), absY + o.getY() );
			
			
			//Reset clip and translate
			g.clearClip();
			g.translate( -o.getX(), -o.getY() );
		}
	}
	
	/**
	 * Tell the Widget and sub-Widgets that a mouse button was pressed.
	 * @param button Button which was pressed
	 * @param x x-coordinate relative to the parent
	 * @param y y-coordinate relative to the parent
	 * @return true if the press was handled
	 * @throws StandardError
	 * @throws SimulatorError
	 */
	final public boolean mouseClicked( int button, int x, int y ) throws StandardError, SimulatorError{
		for( Widget o : widgets ){
			//Only propagate press if it is inside the sub-Widget
			if( o.containsPoint( x, y ) ){
				//Try to handle it, if not handled, continue to next Widget
				if( o.mouseClicked( button, x - o.getX(), y - o.getY() ) )
					return true;
			}
		}
		
		//If the sub-Widgets did not handle it, let this Widget try
		return handleMouseClicked( button, x, y );
	}
	
	/**
	 * Tell the Widget and sub-Widgets that a drag was done
	 * @param oldX Previous x-position of mouse
	 * @param oldY Previous y-position of mouse
	 * @param newX Current x-position of mouse
	 * @param newY Current y-position of mouse
	 * @return true if the drag was handled, false if not
	 * @throws StandardError
	 */
	final public boolean mouseDragged( int oldX, int oldY, int newX, int newY ) throws StandardError{
		for( Widget o : widgets ){
			//We do not check if the drag is inside the sub-Widget!
			//It would cause dragging to freeze, so let the Widget handle
			//it in a manner which would feel natural for the user.
			if( o.mouseDragged( oldX - o.getX(), oldY - o.getY()
				,	newX - o.getX(), newY - o.getY() ) )
				return true;
		}
		
		//However we only handle the drag if dragStart() was called
		return dragging && handleMouseDragged( oldX, oldY, newX, newY );
	}
	
	/**
	 * Tell the Widget and sub-Widgets that a mouse button was released
	 * @param button Button which was released
	 * @param x x-coordinate relative to the parent
	 * @param y y-coordinate relative to the parent
	 * @return true if the release was handled
	 * @throws StandardError
	 * @throws SimulatorError
	 */
	final public boolean mouseReleased( int button, int x, int y ) throws StandardError, SimulatorError{
		for( Widget o : widgets ){
			//We want to ensure that stopDragging() is called, so we can't
			//return directly, and have to store the return value.
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
	
	/**
	 * Handle an event sent by obj
	 * @param obj The object which caused the event
	 * @param event The event type
	 * @throws StandardError
	 * @throws SimulatorError 
	 */
	public void accept( Widget obj, Event event ) throws StandardError, SimulatorError{
		//Nothing here, this is intended, as the default is no action
	}
	
	/**
	 * Notify all listeners that an event happened
	 * @param event
	 * @throws StandardError
	 * @throws SimulatorError 
	 */
	protected void notify( Event event ) throws StandardError, SimulatorError{
		for( Widget o : observers )
			o.accept( this, event );
	}
	
	/**
	 * Start observing this Widget
	 * @param obj The object which want to be notified of events
	 */
	public void startObserving( Widget obj ){
		observers.add( obj );
	}
	
	/**
	 * Stop observing this Widget
	 * @param obj The object which no longer should receive notifications
	 */
	public void stopObserving( Widget obj ){
		observers.remove( obj );
	}
}
