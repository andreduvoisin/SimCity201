package market.gui;

import java.awt.*;
import market.roles.MarketCookCustomerRole;

public class CookCustomerGui implements Gui{
	private MarketCookCustomerRole mAgent;
	
	private int xPos = 50, yPos = 50;
	private int xDestination = 150, yDestination = 150;
	private static final int SIZE = 20;
	
	public CookCustomerGui(MarketCookCustomerRole agent) {
		mAgent = agent;
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
		g.setColor(Color.BLUE);
		g.fillRect(xPos,yPos,SIZE,SIZE);
	}
	
	public boolean isPresent() {
		return true;
	}
}
