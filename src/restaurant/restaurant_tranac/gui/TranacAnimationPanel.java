package restaurant.restaurant_tranac.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import restaurant.restaurant_tranac.TranacRestaurant;
import base.Time;
import city.gui.CityCard;
import city.gui.SimCityGui;

@SuppressWarnings("serial")
public class TranacAnimationPanel extends CityCard implements ActionListener {
	public static TranacAnimationPanel instance;

	private final int WINDOWX = 626;
	private final int WINDOWY = 507;
    private BufferedImage background;

    public TranacAnimationPanel(SimCityGui city) {
    	super(city);
    	setBounds(0,0,WINDOWX, WINDOWY);
    	setBackground(Color.white);
    	
    	TranacAnimationPanel.instance = this;
 
    	Timer timer = new Timer(Time.cSYSCLK/25, this );
    	timer.start();
    	
    	background = null;
    	try {
    	java.net.URL imageURL = this.getClass().getClassLoader().getResource("restaurant/restaurant_tranac/gui/images/restaurant-tranac.png");
    	background = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		System.out.println(e.getMessage());
    	}
    }

	public void actionPerformed(ActionEvent e) {
		synchronized(TranacRestaurant.guis) {
        for(Gui gui : TranacRestaurant.guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }
		}
		
		repaint();
	}

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        
        if(background != null)
        	g2.drawImage(background,0,0,null);

        synchronized(TranacRestaurant.guis) {
        for(Gui gui : TranacRestaurant.guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
        }
    }
    
    public static TranacAnimationPanel getInstance() {
    	return instance;
    }
}
