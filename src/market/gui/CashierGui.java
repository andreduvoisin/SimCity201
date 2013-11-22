package market.gui;

import java.awt.*;
import market.roles.MarketCashierRole;

public class CashierGui implements Gui {
	private MarketCashierRole mAgent;
	
	private static final int xStart = -20, yStart = -20;
	private static final int xHome = 200, yHome = 200;
	
	private int xPos = 50, yPos = 50;
	private int xDestination = xHome, yDestination = yHome;
	private static final int SIZE = 20;
	
	private enum EnumCommand {noCommand, goToPosition, leaveMarket};
	private EnumCommand mCommand = EnumCommand.noCommand;
	
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
        
        if(xPos == xDestination && yPos == yDestination) {
        	switch(mCommand) {
        	case goToPosition: {
        		//mAgent.msgAnimationAtPosition();
        		mCommand = EnumCommand.noCommand;
        		break;
        	}
        	case leaveMarket: {
        		//mAgent.msgAnimationLeftRestaurant();
        		mCommand = EnumCommand.noCommand;
        	}
        	default:
        		break;
        	}
        }
	}
	
	public void draw(Graphics2D g) {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(xPos, yPos, SIZE, SIZE);
	}
	
/* Action Calls */
	public void DoGoToPosition() {
		xDestination = xHome;
		yDestination = yHome;
	}
	
	public void DoLeaveMarket() {
		xDestination = xStart;
		yDestination = yStart;
	}
	
/* Utilities */
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
