package dk.aau.cs.d402f13.simulator;

import java.util.ArrayList;
import java.util.List;

import dk.aau.cs.d402f13.utilities.types.Game;
import dk.aau.cs.d402f13.utilities.types.Gridboard;
import dk.aau.cs.d402f13.utilities.types.Piece;
import dk.aau.cs.d402f13.utilities.types.Square;

class WhiteSquare extends Square{
	@Override
	public String getImgPath(){
		return "img/white.png";
	}
}

class BlackSquare extends Square{
	@Override
	public String getImgPath(){
		return "img/Black.png";
	}
}

class ChessBoard extends Gridboard{

	public ChessBoard() throws CloneNotSupportedException {
		super(8, 8);
		
	}
	
	@Override
	public List<Square> squareTypes(){
		List<Square> list = new ArrayList<Square>();
		list.add( new WhiteSquare() );
		list.add( new BlackSquare() );
		return list;
	}
	
}

abstract class ChessPiece extends Piece{
	private boolean color;
	private String name;
	public ChessPiece( boolean color, String name ){
		this.color = color;
		this.name = name;
	}
	
	@Override
	public String getImgPath(){
		String colorText = color ? "Black_" : "White_";
		return "img/" + colorText + name + ".png";
	}
	
	public ChessPiece setPosition( Gridboard b, int x, int y ){
		setSquare( b.getSquareAt(x, y) );
		return this;
	}
}

class Pawn extends ChessPiece{
	public Pawn(boolean color ) {
		super(color, "Pawn");
	}
}

class Rook extends ChessPiece{
	public Rook(boolean color ) {
		super(color, "Rook");
	}
}

class Knight extends ChessPiece{
	public Knight(boolean color ) {
		super(color, "Knight");
	}
}

class Bishop extends ChessPiece{
	public Bishop(boolean color ) {
		super(color, "Bishop");
	}
}

class Queen extends ChessPiece{
	public Queen(boolean color ) {
		super(color, "King");
	}
}

class King extends ChessPiece{
	public King(boolean color ) {
		super(color, "Queen");
	}
}

public class ChessGame extends Game {
	private void addTeam( Gridboard b, boolean color, List<Piece> pieces ){
		int front = color ? 1 : 6;
		int back = color ? 0 : 7;
		
		for( int i=0; i<8; i++ )
			pieces.add( new Pawn( color ).setPosition( b, i, front) );

		pieces.add( new Rook( color ).setPosition(b, 0, back ) );
		pieces.add( new Rook( color ).setPosition(b, 7, back ) );
		
		pieces.add( new Knight( color ).setPosition(b, 1, back ) );
		pieces.add( new Knight( color ).setPosition(b, 6, back ) );

		pieces.add( new Bishop( color ).setPosition(b, 2, back ) );
		pieces.add( new Bishop( color ).setPosition(b, 5, back ) );

		pieces.add( new Queen( color ).setPosition(b, 3, back ) );
		pieces.add( new King( color ).setPosition(b, 4, back ) );
	}
	
	public ChessGame() throws CloneNotSupportedException{
		setTitle( "Chess" );
		Gridboard b = new ChessBoard();
		
		List<Piece> pieces = new ArrayList<Piece>();

		addTeam( b, false, pieces );
		addTeam( b, true, pieces );
		
		b.setPieces( pieces );
		setBoard( b );
	}
}
