package dk.aau.cs.d402f13.simulator;

import java.util.ArrayList;
import java.util.List;

import dk.aau.cs.d402f13.utilities.types.Game;
import dk.aau.cs.d402f13.utilities.types.Gridboard;
import dk.aau.cs.d402f13.utilities.types.Piece;

public class ChessGame extends Game {
	public ChessGame(){
		setTitle( "Chess" );
		Gridboard b = new Gridboard( 8, 8 );
		
		List<Piece> pieces = new ArrayList<Piece>();

		for( int i=0; i<8; i++ ){
			Piece p = new Piece();
			p.setSquare( b.getSquareAt( i, 1 ));
			p.setImgPath( "img/chesspiece.png" );
			pieces.add( p );
		}
		for( int i=0; i<8; i++ ){
			Piece p = new Piece();
			p.setSquare( b.getSquareAt( i, 6 ));
			p.setImgPath( "img/chesspiece.png" );
			pieces.add( p );
		}
		b.setPieces( pieces );
		setBoard( b );
	}
}
