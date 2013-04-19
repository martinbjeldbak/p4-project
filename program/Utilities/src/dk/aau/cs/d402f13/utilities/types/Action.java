package dk.aau.cs.d402f13.utilities.types;

public class Action {
	private Piece p = null;
	private Square to = null;
	
	public Piece getPiece() {
		return p;
	}
	public void setPiece(Piece p) {
		this.p = p;
	}
	public Square getTo() {
		return to;
	}
	public void setTo(Square to) {
		this.to = to;
	}
	
	
	public boolean isMove(){
		//p must be on a square, and 'to' must be set
		if( p.getSquare() == null )
			return false;
		if( to == null )
			return false;
		return true;
	}
	public boolean isAdd(){
		// 'to' must be set and p most not be on a square already
		if( to == null )
			return false;
		if( p.getSquare() != null )
			return false;
		else
			return true;
	}
	public boolean isRemove(){
		//p has to be on a square, and 'to' must not be set
		if( to != null )
			return false;
		if( p.getSquare() != null )
			return false;
		else
			return true;
	}
	public boolean isInvalid(){
		if( isMove() == false && isAdd() == false && isRemove() == false )
			return true;
		else
			return false;
	}
}
