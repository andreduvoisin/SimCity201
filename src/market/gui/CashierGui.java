package market.gui;

import java.awt.*;
import market.roles.MarketCashierRole;

public class CashierGui implements Gui {
	private MarketCashierRole mAgent;
	
	private int xPos = 50, yPos = 50;
	private int xDestination = 200, yDestination = 200;
	private static final int SIZE = 20;
	
	public CashierGui(MarketCashierRole agent) {
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
		g.setColor(Color.DARK_GRAY);
		g.fillRect(xPos, yPos, SIZE, SIZE);
	}
	
	public boolean isPresent() {
		return true;
	}
}
