package restaurant.restaurant_cwagoner.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import restaurant.restaurant_cwagoner.CwagonerRestaurant;
import base.Gui;
import base.Location;
import base.Time;
import city.gui.CityCard;
import city.gui.SimCityGui;

@SuppressWarnings("serial")
public class CwagonerAnimationPanel extends CityCard implements ActionListener {

	//ANIMATIONS
	private BufferedImage background;
	static Location fridgePos = new Location(200, 300),
			cookingPos = new Location(300, 350),
			platingPos = new Location(180, 350);
	BufferedImage fridgeImg, stoveImg, platingImg, tableImg;
	
	int tableSize = 50;
	public static CwagonerRestaurant restaurant;

    static ArrayList<Location> tableLocations = new ArrayList<Location>();

    public CwagonerAnimationPanel(SimCityGui g, CwagonerRestaurant r) {
    	super(g);
    	CwagonerAnimationPanel.restaurant = r;

    	this.setVisible(true);

    	this.setBounds(0, 0, CARD_WIDTH, CARD_HEIGHT);
        this.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));

    	Timer timer = new Timer(Time.cSYSCLK/15, this);
    	timer.addActionListener(this);
    	timer.start();

    	initializeTables();
    	initializeCookingArea();
    	
    	background = null;
    	try {
    	java.net.URL imageURL = this.getClass().getClassLoader().getResource("restaurant/restaurant_maggiyan/images/mybg.png");
    	background = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		//System.out.println(e.getMessage());
    	}
    }
    
    private void initializeTables() {
        tableLocations.add(new Location(100, 100));
        tableLocations.add(new Location(300, 100));
        tableLocations.add(new Location(100, 200));
        tableLocations.add(new Location(300, 200));
    }

    private void initializeCookingArea() {
		try {
			java.net.URL fridgeURL = this.getClass().getClassLoader().getResource("restaurant/restaurant_cwagoner/gui/img/fridge.png");
			fridgeImg = ImageIO.read(fridgeURL);
			java.net.URL stoveURL = this.getClass().getClassLoader().getResource("restaurant/restaurant_cwagoner/gui/img/stove.png");
			stoveImg = ImageIO.read(stoveURL);
			java.net.URL platingURL = this.getClass().getClassLoader().getResource("restaurant/restaurant_cwagoner/gui/img/plating.png");
			platingImg = ImageIO.read(platingURL);
			java.net.URL tableURL = this.getClass().getClassLoader().getResource("restaurant/restaurant_cwagoner/gui/img/table.png");
			tableImg = ImageIO.read(tableURL);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public static Location getTableLocation(int tableNum) {
    	return tableLocations.get(tableNum);
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  // Will have paint() called
		
		synchronized(CwagonerRestaurant.guis) {
			try {
				for (Gui gui : CwagonerRestaurant.guis) {
		            gui.updatePosition();
		        }
			} catch(Exception ex) {}
		}
	}

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        // Clear the screen by painting a rectangle the size of the panel
        g2.setColor(getBackground());
        g2.fillRect(0, 0, CARD_WIDTH, CARD_HEIGHT);
        
        if(!SimCityGui.GRADINGVIEW) {
        	if(background != null)
        		g2.drawImage(background,0,0,null);
        	
        }
        
        // Tables
        g2.setColor(Color.ORANGE);
        
        for (Location iL : tableLocations) {
        	g2.drawImage(tableImg, iL.mX, iL.mY, null);
        }

        // Cook areas
        	// Stove
			g2.drawImage(stoveImg, cookingPos.mX, cookingPos.mY, null);
			// Plating area
			g2.drawImage(platingImg, platingPos.mX, platingPos.mY, null);
	    	// Fridge
			g2.drawImage(fridgeImg, fridgePos.mX, fridgePos.mY, null);

		synchronized(CwagonerRestaurant.guis) {
	        for (Gui gui : CwagonerRestaurant.guis) {
	            gui.draw(g2);
	        }
        }
    }
}
