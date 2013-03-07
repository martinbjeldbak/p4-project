package dk.aau.cs.d402f13.simulator;

import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

public class Board extends JPanel {

  public Board() {
    // TODO Auto-generated constructor stub
    addMouseListener(new MouseListener() {
      
      @Override
      public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        
      }
      
      @Override
      public void mousePressed(MouseEvent e) {
        System.out.println(e);
        // TODO Auto-generated method stub
      }
      
      @Override
      public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
      }
      
      @Override
      public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
      }
      
      @Override
      public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        
      }
    });
  }

  public Board(LayoutManager layout) {
    super(layout);
    // TODO Auto-generated constructor stub
  }

  public Board(boolean isDoubleBuffered) {
    super(isDoubleBuffered);
    // TODO Auto-generated constructor stub
  }

  public Board(LayoutManager layout, boolean isDoubleBuffered) {
    super(layout, isDoubleBuffered);
    // TODO Auto-generated constructor stub
  }

  
  @Override
  protected void paintComponent(Graphics g) {
  }

}
