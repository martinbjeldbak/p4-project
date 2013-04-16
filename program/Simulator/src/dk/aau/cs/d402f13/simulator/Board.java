package dk.aau.cs.d402f13.simulator;

import org.newdawn.slick.Image;

public class Board {
	
	private int width;
	private int height;
	Image gridImg = null;
	
	public int getWidth() {
		return this.width;
	}
	
	public void setWidth(int value) {
		this.width = value;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public void setHeight(int value) {
		this.height = value;
	}
	
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	
}
