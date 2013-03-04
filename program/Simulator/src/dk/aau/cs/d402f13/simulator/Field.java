package dk.aau.cs.d402f13.simulator;

import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;




public class Field extends JPanel {
  
  private JLabel label;

  public Field(int y, int x) {
    
    setBorder(BorderFactory.createEtchedBorder());
    label = new JLabel(y + "," + x);
    //add(label);
    
    // Listen for mouse events
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

  public Field(LayoutManager layout) {
    super(layout);
    // TODO Auto-generated constructor stub
  }

  public Field(boolean isDoubleBuffered) {
    super(isDoubleBuffered);
    // TODO Auto-generated constructor stub
  }

  public Field(LayoutManager layout, boolean isDoubleBuffered) {
    super(layout, isDoubleBuffered);
    // TODO Auto-generated constructor stub
  }
  
  public void setPicture(String relativePath) {
    this.add(new IconPanel(relativePath));
  }

}
