package dk.aau.cs.d402f13.simulator;

import java.util.ArrayList;
import java.util.List;

import dk.aau.cs.d402f13.utilities.types.Action;
import dk.aau.cs.d402f13.utilities.types.Board;
import dk.aau.cs.d402f13.utilities.types.Game;
import dk.aau.cs.d402f13.utilities.types.Gridboard;
import dk.aau.cs.d402f13.utilities.types.MoveAction;
import dk.aau.cs.d402f13.utilities.types.Piece;
import dk.aau.cs.d402f13.utilities.types.Player;
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
		return "img/black.png";
	}
}

class ChessBoard extends Gridboard{

	public ChessBoard() throws CloneNotSupportedException {
		super(8, 8);
		
	}
	
	@Override
	public List<Square> squareTypes(){
		List<Square> list = new ArrayList<Square>();
		list.add( new BlackSquare() );
		list.add( new WhiteSquare() );
		return list;
	}
	
}

abstract class ChessPiece extends Piece{
	private boolean color;
	private String name;
	public ChessPiece( Player owner, boolean color, String name ){
		super( owner );
		this.color = color;
		this.name = name;
	}
	
	public boolean getColor(){ return color; }
	
	@Override
	public String getImgPath(){
		String colorText = color ? "Black_" : "White_";
		return "img/" + colorText + name + ".png";
	}
	
	public ChessPiece setPosition( Gridboard b, int x, int y ){
		setSquare( b.getSquareAt(x, y) );
		return this;
	}
	
	protected List<Action> slide( Game g, int dx, int dy ){
		List<Action> list = new ArrayList<Action>();
		Gridboard b = (Gridboard)g.board();

		int x = b.squareCoordinateX( square() );
		int y = b.squareCoordinateY( square() );
		do{
			x += dx;
			y += dy;
			
			Square s = b.getSquareAt( x, y );
			if( s == null )
				break;
			
			Piece p = b.findPieceOnSquare( s );
			if( p == null )
				list.add( new MoveAction( this, s ) );
			else{
				break;
			}
			
		}while( true );
		
		return list;
	}
	
	protected Action relative( Game g, int dx, int dy ){
		Gridboard b = (Gridboard)g.board();

		int x = b.squareCoordinateX( square() ) + dx;
		int y = b.squareCoordinateY( square() ) + dy;
		
		Square s = b.getSquareAt( x, y );
		if( s == null )
			return null;
		
		Piece p = b.findPieceOnSquare( s );
		if( p == null )
			return new MoveAction( this, s );
		else{
			return null;
		}
		
	}
}

class Pawn extends ChessPiece{
	public Pawn( Player owner, boolean color ) {
		super( owner, color, "Pawn" );
	}
	
	@Override
	public List<Action> actions( Game g ){
		int dir = getColor() ? -1 : 1;
		List<Action> list = new ArrayList<Action>();
		list.add( relative( g, 0, dir ) );
		list.add( relative( g, 0, dir*2 ) );
		
		//Remove null actions
		List<Action> cleanList = new ArrayList<Action>();
		for( Action a: list )
			if( a != null )
				cleanList.add( a );
		return cleanList;
	}
}

class Rook extends ChessPiece{
	public Rook( Player owner, boolean color ) {
		super( owner, color, "Rook" );
	}
	
	@Override
	public List<Action> actions( Game g ){
		List<Action> list = new ArrayList<Action>();
		list.addAll( slide( g, 1, 0 ) );
		list.addAll( slide( g, -1, 0 ) );
		list.addAll( slide( g, 0, 1 ) );
		list.addAll( slide( g, 0, -1 ) );
		return list;
	}
	
}

class Knight extends ChessPiece{
	public Knight( Player owner, boolean color ) {
		super( owner, color, "Knight" );
	}
	
	@Override
	public List<Action> actions( Game g ){
		List<Action> list = new ArrayList<Action>();
		list.add( relative( g, 1, 2 ) );
		list.add( relative( g, 1, -2 ) );
		list.add( relative( g, -1, 2 ) );
		list.add( relative( g, -1, -2 ) );
		list.add( relative( g, 2, 1 ) );
		list.add( relative( g, -2, 1 ) );
		list.add( relative( g, 2, -1 ) );
		list.add( relative( g, -2, -1 ) );
		
		//Remove null actions
		List<Action> cleanList = new ArrayList<Action>();
		for( Action a: list )
			if( a != null )
				cleanList.add( a );
		return cleanList;
	}
}

class Bishop extends ChessPiece{
	public Bishop( Player owner, boolean color ) {
		super( owner, color, "Bishop" );
	}
	
	@Override
	public List<Action> actions( Game g ){
		List<Action> list = new ArrayList<Action>();
		list.addAll( slide( g, 1, 1 ) );
		list.addAll( slide( g, -1, -1 ) );
		list.addAll( slide( g, -1, 1 ) );
		list.addAll( slide( g, 1, -1 ) );
		return list;
	}
}

class Queen extends ChessPiece{
	public Queen( Player owner, boolean color ) {
		super( owner, color, "King" );
	}
	
	@Override
	public List<Action> actions( Game g ){
		List<Action> list = new ArrayList<Action>();
		list.addAll( slide( g, 1, 0 ) );
		list.addAll( slide( g, -1, 0 ) );
		list.addAll( slide( g, 0, 1 ) );
		list.addAll( slide( g, 0, -1 ) );
		list.addAll( slide( g, 1, 1 ) );
		list.addAll( slide( g, -1, -1 ) );
		list.addAll( slide( g, -1, 1 ) );
		list.addAll( slide( g, 1, -1 ) );
		return list;
	}
}

class King extends ChessPiece{
	public King( Player owner, boolean color ) {
		super( owner, color, "Queen" );
	}
	
	@Override
	public List<Action> actions( Game g ){
		List<Action> list = new ArrayList<Action>();
		list.add( relative( g, -1, 1 ) );
		list.add( relative( g, 0, 1 ) );
		list.add( relative( g, 1, 1 ) );
		
		list.add( relative( g, -1, 0 ) );
		list.add( relative( g, 1, 0 ) );
		
		list.add( relative( g, -1, -1 ) );
		list.add( relative( g, 0, -1 ) );
		list.add( relative( g, 1, -1 ) );
		
		//Remove null actions
		List<Action> cleanList = new ArrayList<Action>();
		for( Action a: list )
			if( a != null )
				cleanList.add( a );
		return cleanList;
	}
}

public class ChessGame extends Game {
	Gridboard b = null;
	List<Player> players = new ArrayList<Player>();
	
	private void addTeam( Gridboard b, Player p, boolean color, List<Piece> pieces ){
		int front = color ? 6 : 1;
		int back = color ? 7 : 0;
		
		for( int i=0; i<8; i++ )
			pieces.add( new Pawn( p, color ).setPosition( b, i, front) );

		pieces.add( new Rook( p, color ).setPosition(b, 0, back ) );
		pieces.add( new Rook( p, color ).setPosition(b, 7, back ) );
		
		pieces.add( new Knight( p, color ).setPosition(b, 1, back ) );
		pieces.add( new Knight( p, color ).setPosition(b, 6, back ) );

		pieces.add( new Bishop( p, color ).setPosition(b, 2, back ) );
		pieces.add( new Bishop( p, color ).setPosition(b, 5, back ) );

		pieces.add( new Queen( p, color ).setPosition(b, 3, back ) );
		pieces.add( new King( p, color ).setPosition(b, 4, back ) );
	}
	
	public ChessGame() throws CloneNotSupportedException{
		super( "Chess" );
		b = new ChessBoard();

		players.add( new Player() );
		players.add( new Player() );
		
		List<Piece> pieces = new ArrayList<Piece>();

		addTeam( b, players.get(0), false, pieces );
		addTeam( b, players.get(1), true, pieces );
		
		b.setPieces( pieces );
	}

	@Override
	public Board board(){ return b; }

	@Override
	public List<Player> players(){ return players; }
}
