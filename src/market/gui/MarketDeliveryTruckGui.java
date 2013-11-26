package market.gui;

import java.awt.*;

import city.gui.CityComponent;
import base.ContactList;
import market.interfaces.MarketDeliveryTruck;

public class MarketDeliveryTruckGui extends CityComponent implements MarketBaseGui {
	private MarketDeliveryTruck mAgent;
	
	private String mDestination;
	
	private static final int xMarketBase = ContactList.cMARKET_LOCATION.mX-20, yMarketBase = ContactList.cMARKET_LOCATION.mY;
	//	private int xMarket = xMarketBase, yMarket = yMarketBase;
	
	private int xPos = xMarketBase, yPos = yMarketBase;
	private int xDestination = xMarketBase, yDestination = yMarketBase;
	private static final int SIZE = 20;
	
	private enum EnumCommand {noCommand, goToMarket, goToRestaurant, leaveMarket};
	private EnumCommand mCommand = EnumCommand.noCommand;
	
	public MarketDeliveryTruckGui(MarketDeliveryTruck agent) {
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
        		mAgent.msgAnimationAtMarket();
        		mCommand = EnumCommand.noCommand;
        		break;
        	}
        	case goToRestaurant: {
        		mAgent.msgAnimationAtRestaurant(mDestination);
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
		g.setColor(Color.DARK_GRAY);
		g.fillRect(xPos, yPos, SIZE, SIZE);
	}
	
/* Action Calls */
	public void DoGoToMarket() {
		xDestination = xMarketBase;
		yDestination = yMarketBase;
		mCommand = EnumCommand.goToMarket;
	}

	public void DoGoToRestaurant(String r) {
		//fill in;
		mDestination = r;
		//must consider parameters
		//proper way to get to restaurant
		//mCommand = EnumCommand.goToRestaurant;
	}
	
	public void DoLeaveMarket() {
		xDestination = xMarketBase;
		yDestination = yMarketBase;
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

	@Override
	public void setPresent(boolean state) {	}
}
