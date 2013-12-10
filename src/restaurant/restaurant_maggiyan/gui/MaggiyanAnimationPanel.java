package restaurant.restaurant_maggiyan.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import restaurant.restaurant_maggiyan.MaggiyanRestaurant;
import base.Time;
import city.gui.CityCard;
import city.gui.SimCityGui;

public class MaggiyanAnimationPanel extends CityCard implements ActionListener {
	public static MaggiyanAnimationPanel mInstance; 
	
    private final int WINDOWX = 500;
    private final int WINDOWY = 500;
    
	private static int XPOS = 50; 
	private static int YPOS = 175; 
	private static int GWIDTH = 50;
	private static int GHEIGHT = 50; 
	
	private static int CookingAreaX = 275;
	
	//ANIMATION
	private BufferedImage background;
	
    public MaggiyanAnimationPanel(SimCityGui city) {
    	super(city); 
    	mInstance = this;
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        setBackground(Color.white);
        
        Timer timer = new Timer(Time.cSYSCLK/40, this );
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
		repaint();  
		synchronized(MaggiyanRestaurant.guis){
	        for(MaggiyanGui gui : MaggiyanRestaurant.guis) {
	            if (gui.isPresent()) {
	                gui.updatePosition();
	            }
	        }
		}
	}

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );
        
        if(background != null)
        	g2.drawImage(background,0,0,null);

        //Here is the table
        g2.setColor(Color.ORANGE);
        g2.fillRect(XPOS, YPOS, GWIDTH, GHEIGHT);//200 and 250 need to be table params
        
        g2.setColor(Color.ORANGE);
        g2.fillRect(XPOS+100, YPOS, GWIDTH, GHEIGHT);//200 and 250 need to be table params
        
        g2.setColor(Color.ORANGE);
        g2.fillRect(XPOS+200, YPOS, GWIDTH, GHEIGHT);//200 and 250 need to be table params
        
        //Kitchen and Cook
        
        g2.setColor(Color.CYAN);
        g2.fillRect(CookingAreaX, YPOS+100, GWIDTH, GHEIGHT*4);
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(CookingAreaX+GWIDTH, YPOS+100, GWIDTH*3, GHEIGHT*4);
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(CookingAreaX + GWIDTH*3, YPOS+100, GWIDTH, GWIDTH);
        g2.fillRect(CookingAreaX + GWIDTH*3, YPOS+100 + GWIDTH + 25, GWIDTH, GWIDTH);
        g2.fillRect(CookingAreaX + GWIDTH*3, YPOS+100 + GHEIGHT*4 - GWIDTH, GWIDTH, GWIDTH);
        
        //g2.setColor(Color.BLUE);
        //g2.fillRect(XPOS+410, YPOS+50, 20, 20);
        
     

        for(MaggiyanGui gui : MaggiyanRestaurant.guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
    }


}
