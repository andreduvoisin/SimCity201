package restaurant.restaurant_tranac.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import restaurant.restaurant_tranac.roles.TranacCashierRole;
import city.gui.SimCityGui;

public class TranacCashierGui implements Gui {

    @SuppressWarnings("unused")
	private TranacCashierRole agent;

    private int xPos = 230, yPos = 130;					//static host position
    private int xDestination = 230, yDestination = 130;	//static cashier position

    private BufferedImage image;
    
    public TranacCashierGui(TranacCashierRole agent) {
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
    	if(SimCityGui.GRADINGVIEW) {
    		g.setColor(Color.WHITE);
    		g.drawString("Cashier", xPos, yPos);
    	}
    	else {
    		g.setColor(Color.WHITE);
    		g.drawImage(image, xPos, yPos, null);
    		g.drawString("The Cashier", xPos-25, yPos-3);
    	}
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
