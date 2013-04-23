package dk.aau.cs.d402f13.simulator;

import dk.aau.cs.d402f13.utilities.types.Action;
import dk.aau.cs.d402f13.utilities.types.AddAction;
import dk.aau.cs.d402f13.utilities.types.MoveAction;
import dk.aau.cs.d402f13.utilities.types.Piece;
import dk.aau.cs.d402f13.utilities.types.Square;

public class ActionHelper {

	static public AddAction isDropAction( Action a, Square s ){
		if( (Object)a instanceof AddAction ){
			//Check if piece match
			AddAction ma = (AddAction)a;
			if( ma.getSquare() == s )
				return ma;
			
			//Try next action
			a = ma.next();
			if( a != null )
				return isDropAction( a, s );
			else
				return null;
		}
		else
			return null;
	}
	
	static public MoveAction isMoveAction( Action a, Piece p ){
		if( (Object)a instanceof MoveAction ){
			//Check if piece match
			MoveAction ma = (MoveAction)a;
			if( ma.getPiece() == p )
				return ma;
			
			//Try next action
			a = ma.next();
			if( a != null )
				return isMoveAction( a, p );
			else
				return null;
		}
		else
			return null;
	}
	
	static public MoveAction isMoveAction( Action a, Square s ){
		if( (Object)a instanceof MoveAction ){
			//Check if squares match
			MoveAction ma = (MoveAction)a;
			if( ma.getTo() == s )
				return ma;
			
			//Try next action
			a = ma.next();
			if( a != null )
				return isMoveAction( a, s );
			else
				return null;
		}
		else
			return null;
	}
	
	static public void debugAction( Action a ){
		do{
			if( (Object)a instanceof MoveAction ){
				MoveAction ma = (MoveAction)a;
				System.out.print( "MoveAction " + ma.getPiece() + " - " + ma.getTo() + ", " );
			}
			if( (Object)a instanceof AddAction ){
				AddAction aa = (AddAction)a;
				System.out.print( "AddAction " + aa.getSquare() + ", " );
			}
			
		}while( (a = a.next()) != null );
		System.out.print( "\n" );
	}
}
