final public void draw(){
for( Widget o : widgets ){
    g.translate( o.getX(), o.getY() );
    g.setClip( o.getX(), o.getY(), o.getWidth(), o.getHeight() );
    
    o.handleDraw();
    o.draw();
    
    g.clearClip();
    g.translate( -o.getX(), -o.getY() );
  }
}