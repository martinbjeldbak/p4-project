final public void draw(){
  g.translate( getX(), getY() );
  
  g.setClip( absX, absY, getWidth(), getHeight() );
  handleDraw( g );
  g.clearClip();

  for( Widget o : widgets )
    o.draw( g, absX + o.getX(), absY + o.getY() );
  
  g.translate( -getX(), -getY() );
}