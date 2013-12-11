package restaurant.restaurant_duvoisin.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import restaurant.restaurant_duvoisin.AndreRestaurant;
import base.Gui;
import base.Time;
import city.gui.CityCard;
import city.gui.SimCityGui;

@SuppressWarnings("serial")
public class AndreAnimationPanel extends CityCard implements ActionListener {
    private final int WINDOWX = 500;
    private final int WINDOWY = 500;
    
    static final int IDLE_X = 10;
    static final int IDLE_Y = 52;
    static final int IDLE_SIZE_X = 30;
    static final int IDLE_SIZE_Y = 435;
    static final int WAIT_X = 10;
    static final int WAIT_Y = 10;
    static final int WAIT_SIZE_X = 480;
    static final int WAIT_SIZE_Y = 30;
    
    static final int GRILL_X = 70;
    static final int GRILL_Y = 451;
    static final int GRILL_SIZE_X = 400;
    static final int GRILL_SIZE_Y = 14;
    
    static final int FRIDGE_X = 450;
    static final int FRIDGE_Y = 428;
    static final int FRIDGESIZE = 20;
    
    static final int STAND_X = 410;
    static final int STAND_Y = 401;
    static final int STAND_SIZE = 24;
    
    Timer timer;
    
    //ANIMATION
  	private BufferedImage background;

    public AndreAnimationPanel(SimCityGui gui) {
    	super(gui);
    	
    	Dimension animDim = new Dimension(WINDOWX, WINDOWY);
        setPreferredSize(animDim);
        setMinimumSize(animDim);
        setMaximumSize(animDim);
        
        setVisible(true);
 
    	timer = new Timer(Time.cSYSCLK/25, this);
    	timer.start();
    	
    	background = null;
    	try {
    	java.net.URL imageURL = this.getClass().getClassLoader().getResource("restaurant/restaurant_maggiyan/images/mybg.png");
    	background = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		System.out.println(e.getMessage());
    	}
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
		synchronized(AndreRestaurant.guis) {
			for(Gui gui : AndreRestaurant.guis) {
	            if (gui.isPresent()) {
	                gui.updatePosition();
	            }
	        }
		}
	}

    //public void paintComponent(Graphics g) {
	public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );
        
        if(!SimCityGui.GRADINGVIEW) {
        	if(background != null)
        		g2.drawImage(background,0,0,null);
        	
        }
        
        //Here is the table
        g2.setColor(Color.ORANGE);
        g2.fillRect(AndreRestaurant.tgui.getTableX(0), AndreRestaurant.tgui.getTableY(0), AndreRestaurant.tgui.getTableSize(0), AndreRestaurant.tgui.getTableSize(0));
        g2.fillRect(AndreRestaurant.tgui.getTableX(1), AndreRestaurant.tgui.getTableY(1), AndreRestaurant.tgui.getTableSize(1), AndreRestaurant.tgui.getTableSize(1));
        g2.fillRect(AndreRestaurant.tgui.getTableX(2), AndreRestaurant.tgui.getTableY(2), AndreRestaurant.tgui.getTableSize(2), AndreRestaurant.tgui.getTableSize(2));
        
        //Idle Area for Waiters
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(IDLE_X, IDLE_Y, IDLE_SIZE_X, IDLE_SIZE_Y);
        g2.setColor(Color.BLACK);
        //g2.drawString("Idle Area", IDLE_X - 14, IDLE_Y - 15);
        
        //Waiting Area for Customers
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(WAIT_X, WAIT_Y, WAIT_SIZE_X, WAIT_SIZE_Y);
        g2.setColor(Color.BLACK);
        //g2.drawString("Waiting Area", 5, 15);
        
        // Grill
        g2.setColor(Color.CYAN);
        g2.fillRect(GRILL_X, GRILL_Y, GRILL_SIZE_X, GRILL_SIZE_Y);
        
        // Plating Area
        g2.setColor(Color.CYAN);
        g2.fillRect(GRILL_X, GRILL_Y - 40, GRILL_SIZE_X, GRILL_SIZE_Y);
        
        // Refrigerator
        g2.setColor(Color.BLACK);
        g2.fillRect(FRIDGE_X, FRIDGE_Y, FRIDGESIZE, FRIDGESIZE);
        
        // Stand
        g2.setColor(Color.PINK);
        g2.fillRect(STAND_X, STAND_Y, STAND_SIZE, STAND_SIZE);

        synchronized(AndreRestaurant.guis) {
	        for(Gui gui : AndreRestaurant.guis) {
	            if (gui.isPresent()) {
	                gui.draw(g2);
	            }
	        }
        }
    }
    
    public void pauseAnimations() {
    	if(timer.isRunning()) { timer.stop(); }
    }
    
    public void resumeAnimations() {
    	if(!timer.isRunning()) { timer.start(); }
    }
}
