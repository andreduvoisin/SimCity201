package restaurant.restaurant_tranac.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import restaurant.restaurant_tranac.roles.TranacRestaurantHostRole;

public class TranacHostGui implements Gui {

    private TranacRestaurantHostRole agent = null;

    private int xPos = 50, yPos = 20;					//static host position
    private int xDestination = 50, yDestination = 20;	//static host position

    private BufferedImage image;
    
    public TranacHostGui(TranacRestaurantHostRole agent) {
        this.agent = agent;
        
    	image = null;
    	try {
    	java.net.URL imageURL = this.getClass().getClassLoader().getResource("restaurant/restaurant_tranac/gui/images/green-requiem.png");
    	image = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		System.out.println(e.getMessage());
    	}
    }

    public void updatePosition() {
        if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;   
    }

    public void draw(Graphics2D g) {
    	g.setColor(Color.WHITE);
    	g.drawImage(image, xPos, yPos, null);
    	g.drawString("The Host", xPos-15, yPos-3);
    }

    public boolean isPresent() {
        return true;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
