package dk.aau.cs.d402f13.simulator;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class BoardGame extends BasicGame {

  public BoardGame() {
    super("");
  }

  @Override
  public void render(GameContainer gc, Graphics g) throws SlickException {
    g.drawString("Hello World", 100, 100);
  }

  @Override
  public void init(GameContainer gc) throws SlickException {
  }

  @Override
  public void update(GameContainer gc, int delta) throws SlickException {
  }

}
