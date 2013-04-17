package dk.aau.cs.d402f13.simulator;

import java.util.ArrayList;
import java.util.List;

import dk.aau.cs.d402f13.utilities.types.Game;
import dk.aau.cs.d402f13.utilities.types.Gridboard;
import dk.aau.cs.d402f13.utilities.types.Piece;

public class ChessGame extends Game {
	
	public Piece addPiece(int coordX, int coodY, String imgPath, Gridboard b) {
		Piece p = new Piece();
		p.setSquare(b.getSquareAt(coordX, coodY));
		p.setImgPath( imgPath );
		return p;
	}
	
	public ChessGame(){
		setTitle( "Chess" );
		Gridboard b = new Gridboard( 8, 8 );
		
		List<Piece> pieces = new ArrayList<Piece>();

		for( int i=0; i<8; i++ ){
			pieces.add( addPiece(i, 1, "img/Black_Pawn.png", b ) );
		}
		
		pieces.add( addPiece(0, 0, "img/Black_Rook.png", b ) );
		pieces.add( addPiece(7, 0, "img/Black_Rook.png", b ) );

		pieces.add( addPiece(1, 0, "img/Black_Knight.png", b ) );
		pieces.add( addPiece(6, 0, "img/Black_Knight.png", b ) );
		
		pieces.add( addPiece(2, 0, "img/Black_Bishop.png", b ) );
		pieces.add( addPiece(5, 0, "img/Black_Bishop.png", b ) );
		
		pieces.add( addPiece(4, 0, "img/Black_Queen.png", b ) );
		pieces.add( addPiece(3, 0, "img/Black_King.png", b ) );
		
		for( int i=0; i<8; i++ ){
			pieces.add( addPiece(i, 6, "img/White_Pawn.png", b ) );
		}
		
		pieces.add( addPiece(0, 7, "img/White_Rook.png", b ) );
		pieces.add( addPiece(7, 7, "img/White_Rook.png", b ) );

		pieces.add( addPiece(1, 7, "img/White_Knight.png", b ) );
		pieces.add( addPiece(6, 7, "img/White_Knight.png", b ) );
		
		pieces.add( addPiece(2, 7, "img/White_Bishop.png", b ) );
		pieces.add( addPiece(5, 7, "img/White_Bishop.png", b ) );
		
		pieces.add( addPiece(4, 7, "img/White_Queen.png", b ) );
		pieces.add( addPiece(3, 7, "img/White_King.png", b ) );
		
		
		b.setPieces( pieces );
		setBoard( b );
	}
}
