package restaurant.restaurant_cwagoner.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class CwagonerAnimationPanel extends JPanel implements ActionListener {

    private int ANIMATIONX = 1000;
    private int ANIMATIONY = 1000;

    private List<CwagonerGui> guis = new ArrayList<CwagonerGui>();
    
    private ArrayList<Dimension> tableLocations = new ArrayList<Dimension>();

    public CwagonerAnimationPanel() {
    	super();
    	setSize(ANIMATIONX, ANIMATIONY);

    	Timer timer = new Timer(12, this);
    	timer.start();
    }
    
    public void addTable(Dimension d) {
    	tableLocations.add(d);
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  // Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        // Clear the screen by painting a rectangle the size of the panel
        g2.setColor(getBackground());
        g2.fillRect(0, 0, ANIMATIONX, ANIMATIONY);

        // Here are the tables
        g2.setColor(Color.ORANGE);
        
        for (Dimension d : tableLocations) {
        	g2.fillRect(d.width, d.height, 50, 50);
        	
        }

        for (CwagonerGui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }

        for (CwagonerGui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
    }

    public void addGui(CwagonerGui gui) {
        guis.add(gui);
    }
}
