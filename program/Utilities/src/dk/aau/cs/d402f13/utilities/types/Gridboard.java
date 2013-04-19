package dk.aau.cs.d402f13.utilities.types;

public class Gridboard extends Board {
	
	int height;
	int width;
	
	public Gridboard( int width, int height ){
		this.height = height;
		this.width = width;
		for( int i=0; i < width * height; i++ ){
			squares.add( new Square() );
		}
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int value) {
		height = value;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int value) {
		width = value;
	}
	
	public int squareCoordinateX( Square s ){
		int index = squares.indexOf(s);
		if( index == -1 )
			return -1;
		
		return index % getWidth();
	}
	
	public int squareCoordinateY( Square s ){
		int index = squares.indexOf(s);
		if( index == -1 )
			return -1;
		
		return index / getWidth();
	}
	
	public Square getSquareAt( int x, int y ){
		if( x >= width || y >= height || x < 0 || y < 0 )
			return null;
		return squares.get( x + y* width);
	}
}
