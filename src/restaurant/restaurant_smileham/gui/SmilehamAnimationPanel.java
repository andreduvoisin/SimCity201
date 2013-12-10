package restaurant.restaurant_smileham.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import restaurant.restaurant_smileham.SmilehamRestaurant;
import restaurant.restaurant_smileham.Table;
import restaurant.restaurant_smileham.WaitingArea;
import restaurant.restaurant_smileham.roles.SmilehamHostRole;
import base.Gui;
import base.Time;
import city.gui.CityCard;
import city.gui.SimCityGui;

@SuppressWarnings({"serial","static-access"})
public class SmilehamAnimationPanel extends CityCard implements ActionListener {

	public static SmilehamRestaurant restaurant;
	
    private final int WINDOWX = 500;
    private final int WINDOWY = 500;
    
    //ANIMATION
  	private BufferedImage background;
    
    //CONSTRUCTOR
	public SmilehamAnimationPanel(SimCityGui city, SmilehamRestaurant rest) {
    	super(city);
    	this.restaurant = rest;
    	setSize(WINDOWX, WINDOWY);
        setVisible(true); 
    	Timer timer = new Timer(Time.cSYSCLK/10, this );
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
		synchronized (restaurant.mGuis) {
        	for(Gui gui : restaurant.mGuis) {
        		synchronized(gui){
	                if (gui.isPresent()) {
	                    gui.updatePosition();
	                }
        		}
            }
		}
		repaint();  //Will have paintComponent called
	}

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );
        
        if(background != null)
        	g2.drawImage(background,0,0,null);

        //draw the tables
        g2.setColor(Table.cTABLE_COLOR);
        for (int iTableNum = 0; iTableNum < SmilehamHostRole.cNUM_TABLES; iTableNum++){
        	g2.fillRect(Table.getX(iTableNum), Table.getY(iTableNum), Table.cTABLE_WIDTH, Table.cTABLE_HEIGHT);
        }
        
        //draw waiting area
        g2.setColor(WaitingArea.cWAITINGAREA_COLOR);
        g2.fillRect(WaitingArea.cWAITINGAREA_X, WaitingArea.cWAITINGAREA_Y, WaitingArea.cWAITINGAREA_WIDTH, WaitingArea.cWAITINGAREA_HEIGHT);

        //draw plating area
        g2.setColor(CookGui.cPLATINGAREA_COLOR);
        g2.fillRect(CookGui.cPLATINGAREA_X, CookGui.cPLATINGAREA_Y, CookGui.cPLATINGAREA_WIDTH, CookGui.cPLATINGAREA_HEIGHT);
        
        //draw cooking area
        g2.setColor(CookGui.cCOOKINGAREA_COLOR);
        g2.fillRect(CookGui.cCOOKINGAREA_X, CookGui.cCOOKINGAREA_Y, CookGui.cCOOKINGAREA_WIDTH, CookGui.cCOOKINGAREA_HEIGHT);
        
        //draw fridge
        g2.setColor(CookGui.cFRIDGE_COLOR);
        g2.fillRect(CookGui.cFRIDGE_X, CookGui.cFRIDGE_Y, CookGui.cFRIDGE_WIDTH, CookGui.cFRIDGE_HEIGHT);
        
        //animation
        synchronized (restaurant.mGuis) {
            for(Gui gui : restaurant.mGuis) {
                if (gui.isPresent()) {
                    gui.draw(g2);
                }
            }
		}
        
    }
	
}