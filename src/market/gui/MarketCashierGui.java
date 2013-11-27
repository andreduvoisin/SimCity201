package market.gui;

import java.awt.*;

import market.interfaces.MarketCashier;

public class MarketCashierGui implements MarketBaseGui {
	private MarketCashier mAgent;
	
	private static final int xStart = -20, yStart = -20;
	private static final int xHome = 100, yHome = 50;
	
	private int xPos = xStart, yPos = yStart;
	private int xDestination = xHome, yDestination = yHome;
//	private int xDestination = xStart, yDestination = yStart;
	private static final int SIZE = 20;
	
	public enum EnumCommand {noCommand, goToPosition, leaveMarket};
	public EnumCommand mCommand = EnumCommand.noCommand;
	
	public MarketCashierGui(MarketCashier agent) {
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
        		mCommand = EnumCommand.noCommand;
        		mAgent.msgAnimationAtPosition();
        		break;
        	}
        	case leaveMarket: {
        		mCommand = EnumCommand.noCommand;
         		mAgent.msgAnimationLeftMarket();
         		break;
        	}
        	default:
        		break;
        	}
        }
	}
	
	public void draw(Graphics2D g) {
		System.out.println("test");
		g.setColor(Color.ORANGE);
		g.fillRect(xPos, yPos, SIZE, SIZE);
	}
	
/* Action Calls */
	public void DoGoToPosition() {
		xDestination = xHome;
		yDestination = yHome;
		mCommand = EnumCommand.goToPosition;
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
