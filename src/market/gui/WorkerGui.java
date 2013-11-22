package market.gui;

import java.awt.*;

import market.*;
import market.roles.MarketWorkerRole;

public class WorkerGui implements Gui {
	private MarketWorkerRole mAgent;
	
	private static final int xStart = -20, yStart = -20;
	private static final int xHome = 0, yHome = 100;
	
	private int xPos = 50, yPos = 50;
	private int xDestination = xHome, yDestination = yHome;
	private static final int SIZE = 20;
	
	private enum EnumCommand {noCommand, goToMarket, fulFillOrder, goToCashier, goToCustomer, goToDeliveryTruck, leaveMarket};
	private EnumCommand mCommand = EnumCommand.noCommand;
	
	public WorkerGui(MarketWorkerRole agent) {
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
        		mCommand = EnumCommand.noCommand;
        	}
        	case fulFillOrder: {
        		mCommand = EnumCommand.noCommand;
        		break;
        	}
        	case goToCashier: {
        		mCommand = EnumCommand.noCommand;
        		break;
        	}
        	case goToCustomer: {
        		mCommand = EnumCommand.noCommand;
        		break;
        	}
        	case goToDeliveryTruck: {
        		mCommand = EnumCommand.noCommand;
        		break;
        	}
        	case leaveMarket: {
        		mCommand = EnumCommand.noCommand;
        		break;
        	}
        		default:
        			break;
        	}
        }
	}
	
	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, SIZE, SIZE);
	}
	
/* Action Calls */
	public void DoGoToMarket() {
		xDestination = xHome;
		yDestination = yHome;
		mCommand = EnumCommand.goToMarket;
	}
	
	public void DoFulfillOrder(Order o) {
		
	}
	
	public void DoGoToCustomer() {
		
	}
	
	public void DoGoToDeliveryTruck() {
		
	}
	
	public void DoLeaveMarket() {
		xDestination = xStart;
		yDestination = yStart;
		mCommand = EnumCommand.leaveMarket;
	}
	
	public void DoGoToHome() {
		xDestination = xHome;
		yDestination = yHome;
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
