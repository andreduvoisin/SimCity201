package market.gui;

import java.awt.*;

import city.gui.CityComponent;
import base.ContactList;
import market.interfaces.MarketDeliveryTruck;

public class MarketDeliveryTruckGui extends CityComponent implements MarketBaseGui {
	private MarketDeliveryTruck mAgent;
	
	private int mDestinationRestaurant;
	
	private static final int xMarketBase = ContactList.cMARKET_LOCATION.mX-35, yMarketBase = ContactList.cMARKET_LOCATION.mY-35;
	
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
     /*   if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;
      */
		//delivery truck can only move in 4 directions
		//left road
		if(xPos == ContactList.cGRID_POINT1-35) {
			if(yPos < yDestination)
				yPos++;
			else if(yPos > yDestination)
				yPos--;
		}
		//right road
		if(xPos == ContactList.cGRID_POINT7+35) {
			if(yPos < yDestination)
				yPos++;
			else if(yPos > yDestination)
				yPos--;
		}
		//top road
		if(yPos == ContactList.cGRID_POINT1-35) {
			if(xPos < xDestination)
				xPos++;
			else if(xPos > xDestination)
				xPos--;
		}
		//bottom road
		if(yPos == ContactList.cGRID_POINT7+35) {
			if(xPos < xDestination)
				xPos++;
			else if(xPos > xDestination)
				xPos--;
		}
		
        if(xPos == xDestination && yPos == yDestination) {
        	switch(mCommand) {
        	case goToMarket: {
        		mAgent.msgAnimationAtMarket();
        		mCommand = EnumCommand.noCommand;
        		break;
        	}
        	case goToRestaurant: {
        		mAgent.msgAnimationAtRestaurant(mDestinationRestaurant);
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

	public void DoGoToRestaurant(int n) {
		mDestinationRestaurant = n;
		xDestination = ContactList.cRESTAURANT_LOCATIONS.get(n).mX;
		yDestination = ContactList.cRESTAURANT_LOCATIONS.get(n).mY;
		mCommand = EnumCommand.goToRestaurant;
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
