package dk.aau.cs.d402f13.simulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.beans.Transient;


import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.kitfox.svg.app.beans.SVGIcon;
import com.kitfox.svg.app.beans.SVGPanel;

public class IconPanel extends JPanel {
  private SVGIcon icon;

  public IconPanel(String relativePath) {
    URI imgURI;
    try {
      imgURI = new URI("file://" + System.getProperty("user.dir") + "/" + relativePath);

      icon = new SVGIcon();
      icon.setSvgURI(imgURI);
      icon.setUseAntiAlias(true);
      
    } catch (URISyntaxException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  @Override
  @Transient
  public Dimension getPreferredSize() {
    return this.getParent().getSize();
  }
  
  public void paintComponent(Graphics g) {
    g.fillRect(0, 0, 200, 200);
    icon.paintIcon(this, g, 0, 0);
   }
}
