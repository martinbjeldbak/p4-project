final public boolean mouseClicked( int button, int x, int y ){
  for( Widget o : widgets )
    if( o.containsPoint( x, y ) )
      if( o.mouseClicked( button, x - o.getX(), y - o.getY() ) )
        return true;
  return handleMouseClicked( button, x, y );
}