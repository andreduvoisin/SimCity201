package market.gui;

import java.awt.*;

import market.roles.MarketDeliveryTruckRole;

public class DeliveryTruckGui implements Gui {
	private MarketDeliveryTruckRole mAgent;
	
	private static final int xStart = -20, yStart = -20;
	private static final int xMarketBase = 20, yMarketBase = 20;
	private int xMarket = xMarketBase, yMarket = yMarketBase;
	
	private int xPos = 50, yPos = 50;
	private int xDestination = xMarket, yDestination = yMarket;
	private static final int SIZE = 20;
	
	private enum EnumCommand {noCommand, goToMarket, goToRestaurant, leaveMarket};
	private EnumCommand mCommand = EnumCommand.noCommand;
	
	public DeliveryTruckGui(MarketDeliveryTruckRole agent) {
		mAgent = agent;
		//add in functionality to choose distinct
		//market spots
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
        		//mAgent.msgAnimationAtMarket();
        		mCommand = EnumCommand.noCommand;
        		break;
        	}
        	case goToRestaurant: {
        		//mAgent.msgAnimationAtRestaurant();
        		mCommand = EnumCommand.noCommand;
        		break;
        	}
        	case leaveMarket: {
        		//mAgent.msgAnimationLeftRestaurant();
        		mCommand = EnumCommand.noCommand;
        		break;
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
	public void DoGoToMarket() {
		xDestination = xMarket;
		yDestination = yMarket;
		mCommand = EnumCommand.goToMarket;
	}

	public void DoGoToRestaurant(int n) {
		//fill in;
		//must consider parameters
		//proper way to get to restaurant
		//mCommand = EnumCommand.goToRestaurant;
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
