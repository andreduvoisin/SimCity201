package market.gui;

import java.awt.*;

import market.interfaces.MarketCustomer;

public class MarketCustomerGui implements MarketBaseGui {
	private MarketCustomer mAgent;

	private static final int xStart = -20, yStart = -20;
	private static final int xMarket = 70, yMarket = 50;
	private static final int xWaitingArea = 70, yWaitingArea = 200;
	
	private int xPos = xStart, yPos = yStart;
	private int xDestination = xStart, yDestination = yStart;
	private static final int SIZE = 20;
	
	private enum EnumCommand {noCommand, goToMarket, goToWaitingArea, leaveMarket};
	private EnumCommand mCommand = EnumCommand.noCommand;
	
	public MarketCustomerGui(MarketCustomer agent) {
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
        	case goToMarket: {
        		mAgent.msgAnimationAtMarket();
        		mCommand = EnumCommand.noCommand;
        		break;
        	}
        	case goToWaitingArea: {
        		mAgent.msgAnimationAtWaitingArea();
        		mCommand = EnumCommand.noCommand;
        		break;
        	}
        	case leaveMarket: {
        		mAgent.msgAnimationLeftMarket();
        		mCommand = EnumCommand.noCommand;
        		break;
        	}
        	default:
        		break;
        	}
        }
	}
	
	public void draw(Graphics2D g) {
		g.setColor(Color.BLUE);
		g.fillRect(xPos,yPos,SIZE,SIZE);
	}

/* Action Calls */
	public void DoGoToMarket() {
		//change to a waiting line
		xDestination = xMarket;
		yDestination = yMarket;
		mCommand = EnumCommand.goToMarket;
	}
	
	public void DoWaitForOrder() {
		//change to a waiting area
		xDestination = xWaitingArea;
		yDestination = yWaitingArea;
		mCommand = EnumCommand.goToWaitingArea;
	}
	
	public void DoLeaveMarket() {
		xDestination = xStart;
		yDestination = yStart;
		mCommand = EnumCommand.leaveMarket;
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
