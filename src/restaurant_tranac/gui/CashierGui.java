package restaurant_tranac.gui;

import restaurant_tranac.roles.RestaurantCashierRole_at;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class CashierGui implements Gui {

    private RestaurantCashierRole_at agent;

    private int xPos = 230, yPos = 130;					//static host position
    private int xDestination = 230, yDestination = 130;	//static cashier position

    private BufferedImage image;
    
    public CashierGui(RestaurantCashierRole_at agent) {
        this.agent = agent;
        
    	image = null;
    	try {
    	java.net.URL imageURL = this.getClass().getClassLoader().getResource("restaurant_tranac/gui/images/green-requiem.png");
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
    	g.drawString("The Cashier", xPos-25, yPos-3);
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
