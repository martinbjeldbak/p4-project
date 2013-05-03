package dk.aau.cs.d402f13.simulator;

import java.util.ArrayList;
import java.util.List;

import dk.aau.cs.d402f13.utilities.types.Action;
import dk.aau.cs.d402f13.utilities.types.AddAction;
import dk.aau.cs.d402f13.utilities.types.Game;
import dk.aau.cs.d402f13.utilities.types.Gridboard;
import dk.aau.cs.d402f13.utilities.types.MoveAction;
import dk.aau.cs.d402f13.utilities.types.Piece;
import dk.aau.cs.d402f13.utilities.types.RemoveAction;
import dk.aau.cs.d402f13.utilities.types.Square;

public class ActionHelper{

	/**
	 * Checks if an Action contains a AddAction
	 * @param a The Action to check
	 * @param s The Square the Action must Add to
	 * @return The actual Action which contains the matching AddAction
	 */
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
	
	/**
	 * Checks if an Action contains a MoveAction
	 * @param a The Action to check
	 * @param p The piece the MoveAction affects
	 * @return The actual Action which contains the matching MoveAction
	 */
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
	
	/**
	 * Checks if an Action contains a MoveAction
	 * @param a The Action to check
	 * @param s The Square the Action must Move to
	 * @return The actual Action which contains the matching MoveAction
	 */
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
	
	/**
	 * Print information about an Action to System.out
	 * @param a The Action to print
	 */
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
	
	/**
	 * Create a human readable textual representation of an Action
	 * @param g The Game the Action affects
	 * @param a The Action to describe
	 * @return The human readable textual representation
	 */
	static public String humanReadable( Game g, Action a ){
		List<MoveAction> moves = new ArrayList<MoveAction>();
		List<AddAction> adds = new ArrayList<AddAction>();
		List<RemoveAction> removes = new ArrayList<RemoveAction>();
		
		if( a == null )
			return "No action";
		
		do{
			if( (Object)a instanceof MoveAction )
				moves.add( (MoveAction)a );
			else if( (Object)a instanceof AddAction )
				adds.add( (AddAction)a );
			else if( (Object)a instanceof RemoveAction )
				removes.add( (RemoveAction)a );
		}while( (a = a.next()) != null );
		
		String text = "";
		
		if( adds.size() > 0 ){
			text += "Adds ";
			for( AddAction aa : adds ){
				text += squarePosition( g, aa.getSquare() );
				text += " & ";
				//TODO: write name of piece
			}
			text = text.substring( 0, text.length()-3 );
			text += "; ";
		}
		
		if( moves.size() > 0 ){
			text += "Moves ";
			for( MoveAction ma : moves ){
				text += squarePosition( g, ma.getFrom() );
				text += "->";
				text += squarePosition( g, ma.getTo() );
				text += " & ";
			}
			text = text.substring( 0, text.length()-3 );
			text += "; ";
		}
		
		if( removes.size() > 0 ){
			text += "Removes ";
			for( RemoveAction ra : removes ){
				text += squarePosition( g, ra.getFrom() );
				text += " & ";
			}
			text = text.substring( 0, text.length()-3 );
			text += "; ";
		}
		
		return text.trim();
	}
	
	/**
	 * A human readable textual representation of a Squares position
	 * @param g The Game the Square is a part of
	 * @param s The Square to describe
	 * @return The textual representation
	 */
	static private String squarePosition( Game g, Square s ){
		if( (Object)g.board() instanceof Gridboard ){
			Gridboard b = (Gridboard)g.board();

			int x = b.squareCoordinateX( s );
			int y = b.squareCoordinateY( s );
			
			return (char)('A' + x) + "" + (y + 1);
		}
		return "board type not supported";
	}
}
