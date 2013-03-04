package dk.aau.cs.d402f13.simulator;

import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.FieldPosition;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import dk.aau.cs.d402f13.simulator.actions.FileBrowserAction;

public class Board extends JPanel {
  Field[][] field;

  public Board() {
  }
  
  public void setDimensions(int height, int width) {
    field = new Field[height][width];
    
    for(int i = 0; i < height; i++) {
      for(int j = 0; j < width; j++) {
        field[i][j] = new Field(i, j);
        
        // Add the field to game board
        this.add(field[i][j]);
      }
    }
    
    this.setLayout(new GridLayout(height, width, 2, 2));
  }
  
  public Field fieldAt(int x, int y) {
    return field[x][y];
  }
}
