package market.gui;

import java.awt.*;
import market.roles.MarketCustomerRole;

public class CustomerGui implements Gui {
	private MarketCustomerRole mAgent;

	private int xPos = 50, yPos = 50;
	private int xDestination = 50, yDestination = 50;
	private static final int SIZE = 20;
	
	public CustomerGui(MarketCustomerRole agent) {
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
		g.setColor(Color.WHITE);
		g.fillRect(xPos,yPos,SIZE,SIZE);
	}
	
	public boolean isPresent() {
		return true;
	}
}
