package dk.aau.cs.d402f13.utilities.types;

import java.util.ArrayList;
import java.util.List;

public class Piece {
	
	private String imgPath = null;
	
	private Player player;
	
	List<Action> actions = new ArrayList<Action>();
	
	public List<Action> playerActions(Game g) {
		return actions;
	}
	
	public void setPlayerActions(List<Action> actions) {
		this.actions = actions;
	}
	
	public String getImgPath() {
		return imgPath;
	}
	
	public void setImgPath(String value) {
		imgPath = value;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
	
	
	
}
