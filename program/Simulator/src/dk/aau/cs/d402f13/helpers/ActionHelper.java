package dk.aau.cs.d402f13.helpers;

import java.util.ArrayList;
import java.util.List;

import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.gameapi.Action;
import dk.aau.cs.d402f13.utilities.gameapi.ActionSequence;
import dk.aau.cs.d402f13.utilities.gameapi.AddAction;
import dk.aau.cs.d402f13.utilities.gameapi.Game;
import dk.aau.cs.d402f13.utilities.gameapi.MoveAction;
import dk.aau.cs.d402f13.utilities.gameapi.Piece;
import dk.aau.cs.d402f13.utilities.gameapi.RemoveAction;
import dk.aau.cs.d402f13.utilities.gameapi.Square;
import dk.aau.cs.d402f13.utilities.gameapi.UnitAction;

interface ActionTypeCheck{
	public Action isType( UnitAction a ); 
}
class ActionAddCheck implements ActionTypeCheck{
	public Action isType( UnitAction a ){
		return ( a instanceof AddAction ) ? a : null;
	}
}
class ActionMoveCheck implements ActionTypeCheck{
	public Action isType( UnitAction a ){
		return ( a instanceof MoveAction ) ? a : null;
	}
}
class ActionRemoveCheck implements ActionTypeCheck{
	public Action isType( UnitAction a ){
		return ( a instanceof RemoveAction ) ? a : null;
	}
}

public class ActionHelper{
	

	private static Action isAction( Action a, ActionTypeCheck atc ) throws StandardError{
		if( a instanceof ActionSequence ){
			for( UnitAction ua : ((ActionSequence)a).getActions() )
				return atc.isType( ua );
			return null;
		}
		else
			return atc.isType( (UnitAction)a );
	}
	
	

	static public AddAction isAddAction( Action a ) throws StandardError{
		Action aa = ActionHelper.isAction( a, new ActionAddCheck() );
		return aa != null ? (AddAction)aa : null;
	}
	static public MoveAction isMoveAction( Action a ) throws StandardError{
		Action aa = ActionHelper.isAction( a, new ActionMoveCheck() );
		return aa != null ? (MoveAction)aa : null;
	}
	static public RemoveAction isRemoveAction( Action a ) throws StandardError{
		Action aa = ActionHelper.isAction( a, new ActionRemoveCheck() );
		return aa != null ? (RemoveAction)aa : null;
	}
	
	static public AddAction isAddAction( Action a, Square s ) throws StandardError{
		AddAction aa = isAddAction( a );
		if( aa == null )
			return null;
		if( aa.getTo() == s )
			return aa;
		return null;
	}
	
	static public MoveAction isMoveAction( Action a, Piece p ) throws StandardError{
		MoveAction ma = isMoveAction( a );
		if( ma == null )
			return null;
		if( ma.getPiece() == p )
			return ma;
		return null;
	}
	
	static public MoveAction isMoveAction( Action a, Square s ) throws StandardError{
		MoveAction ma = isMoveAction( a );
		if( ma == null )
			return null;
		if( ma.getTo() == s )
			return ma;
		return null;
	}
	
	static public RemoveAction isRemoveAction( Action a, Square s ) throws StandardError{
		RemoveAction ra = isRemoveAction( a );
		if( ra == null )
			return null;
		if( ra.getPiece().getSquare() == s )
			return ra;
		return null;
	}
	
	/**
	 * Create a human readable textual representation of an Action
	 * @param g The Game the Action affects
	 * @param a The Action to describe
	 * @return The human readable textual representation
	 * @throws StandardError 
	 */
	static public String humanReadable( Game g, Action a ) throws StandardError{
		List<MoveAction> moves = new ArrayList<MoveAction>();
		List<AddAction> adds = new ArrayList<AddAction>();
		List<RemoveAction> removes = new ArrayList<RemoveAction>();
		
		if( a == null )
			return "No action";
		
		if( a instanceof ActionSequence ){
			for( UnitAction ua : ((ActionSequence)a).getActions() ){
				if( (Object)ua instanceof MoveAction )
					moves.add( (MoveAction)ua );
				else if( (Object)ua instanceof AddAction )
					adds.add( (AddAction)ua );
				else if( (Object)ua instanceof RemoveAction )
					removes.add( (RemoveAction)ua );
			}
		}
		else{
			if( (Object)a instanceof MoveAction )
				moves.add( (MoveAction)a );
			else if( (Object)a instanceof AddAction )
				adds.add( (AddAction)a );
			else if( (Object)a instanceof RemoveAction )
				removes.add( (RemoveAction)a );
		}
		
		String text = "";
		
		if( adds.size() > 0 ){
			text += "Adds ";
			for( AddAction aa : adds ){
				text += squarePosition( g, aa.getPiece().getSquare() );
				text += " & ";
				//TODO: write name of piece
			}
			text = text.substring( 0, text.length()-3 );
			text += "; ";
		}
		
		if( moves.size() > 0 ){
			text += "Moves ";
			for( MoveAction ma : moves ){
				text += squarePosition( g, ma.getPiece().getSquare() );
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
				text += squarePosition( g, ra.getPiece().getSquare() );
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
	 * @throws StandardError 
	 */
	static private String squarePosition( Game g, Square s ) throws StandardError{
		//if( (Object)g.getBoard() instanceof GridBoard ){
			//GridBoard b = (GridBoard)g.getBoard();
			//TODO: how to generalize positions?
			int x = s.getX();
			int y = s.getY();
			
			return (char)('A' + x) + "" + (y + 1);
		//}
		//return "board type not supported";
	}
}
