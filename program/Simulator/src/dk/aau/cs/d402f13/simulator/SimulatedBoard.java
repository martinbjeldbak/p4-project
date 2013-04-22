package dk.aau.cs.d402f13.simulator;

import java.util.ArrayList;
import java.util.List;

import dk.aau.cs.d402f13.utilities.types.Square;

public abstract class SimulatedBoard {
	protected SimulatedGame game;
	protected List<SimulatedPieces> pieces = new ArrayList<SimulatedPieces>();
	private List<Square> hintSquares = new ArrayList<Square>();
	private Square selected;

	public SimulatedBoard(SimulatedGame game) {
		this.game = game;
	}
	
	public abstract Square findSquare(int x, int y);

	public Square getSelected() {
		return selected;
	}

	public void setSelected(Square selected) {
		this.selected = selected;
	}

	public boolean squareIsHinted( Square s ){
		return hintSquares.indexOf( s ) != -1;
	}
	
	public void clearHints(){
		hintSquares.clear();
	}
	public void addHint( Square s ){
		hintSquares.add( s );
	}
}
