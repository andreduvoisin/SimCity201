package restaurant.restaurant_cwagoner.gui;

import javax.swing.*;

import base.Location;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class CwagonerAnimationPanel extends JPanel implements ActionListener {

	int width, height, tableSize = 50;
    List<CwagonerGui> guis = new ArrayList<CwagonerGui>();
    ArrayList<Location> tableLocations = new ArrayList<Location>();

    public CwagonerAnimationPanel(int width, int height) {
    	super();

    	this.setVisible(true);

    	this.width = width;
    	this.height = height;

    	this.setBounds(0, 0, width, height);
        this.setPreferredSize(new Dimension(width, height));

    	Timer timer = new Timer(12, this);
    	timer.addActionListener(this);
    	timer.start();
    }
    
    public void addTable(Location l) {
    	tableLocations.add(l);
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  // Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        // Clear the screen by painting a rectangle the size of the panel
        g2.setColor(getBackground());
        g2.fillRect(0, 0, width, height);

        // Here are the tables
        g2.setColor(Color.ORANGE);
        
        for (Location iL : tableLocations) {
        	g2.fillRect(iL.mX, iL.mY, tableSize, tableSize);
        	
        }

        for (CwagonerGui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }

        for (CwagonerGui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }
    }

    public void addGui(CwagonerGui gui) {
        guis.add(gui);
    }
}
