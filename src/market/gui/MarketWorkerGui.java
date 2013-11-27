package market.gui;

import java.awt.*;
import java.util.concurrent.Semaphore;

import restaurant.restaurant_cwagoner.roles.CwagonerCookRole.Order;
import base.Item.EnumItemType;
import market.*;
import market.interfaces.MarketWorker;

public class MarketWorkerGui implements MarketBaseGui {
	private MarketWorker mAgent;
	public MarketItemsGui mItems;
	
	private MarketOrder mOrder = null;
	
	private static final int xStart = -20, yStart = -20;
	private static final int xHome = 200, yHome = 10;
	private static final int xDeliveryTruck = 250, yDeliveryTruck = 500;
	private int xCustomer = 100, yCustomer = 250;
	
	private int xPos = xStart, yPos = yStart;
//	private int xDestination = xStart, yDestination = yStart;
	private int xDestination = xHome, yDestination = yHome;
	private static final int SIZE = 20;
	
	private enum EnumCommand {noCommand, goToMarket, fulFillOrder, goToItem, goToCashier, goToCustomer, goToDeliveryTruck, leaveMarket};
	private EnumCommand mCommand = EnumCommand.noCommand;
	
	private Semaphore gettingItem = new Semaphore(0,true);
	
	public MarketWorkerGui(MarketWorker agent) {
		mAgent = agent;
	}
	
	public MarketWorkerGui(MarketWorker agent, MarketItemsGui g) {
		mAgent = agent;
		mItems = g;
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
        	case goToItem: {
        		System.out.println(mAgent.toString() + " l");
        		gettingItem.release();
        		mCommand = EnumCommand.noCommand;
        		break;
        	}
        	case goToMarket: {
        		mAgent.msgAnimationAtMarket();
        		mCommand = EnumCommand.noCommand;
        		break;
        	}
        	case goToCashier: {
        		mCommand = EnumCommand.noCommand;
        		break;
        	}
        	case goToCustomer: {
        		mAgent.msgAnimationAtCustomer();
        		mCommand = EnumCommand.noCommand;
        		break;
        	}
        	case goToDeliveryTruck: {
        		mAgent.msgAnimationAtDeliveryTruck();
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
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, SIZE, SIZE);
	}
	
/* Action Calls */
	public void DoGoToMarket() {
		xDestination = xHome;
		yDestination = yHome;
		mCommand = EnumCommand.goToMarket;
	}
	
	public void DoFulfillOrder(MarketOrder o) {
		mOrder = o;
	for(EnumItemType item : mOrder.mItems.keySet()) {
			System.out.println(mAgent.toString() + " k");
			MarketCoordinates c = mItems.getItemCoordinates(item);
			xDestination = c.getX()-30;
			yDestination = c.getY();
			mCommand = EnumCommand.goToItem;
			try {
				gettingItem.acquire();
			}
			catch(InterruptedException e) {
				e.printStackTrace();
			}
			mItems.decreaseItemCount(item, mOrder.mItems.get(item));		
	}
		mAgent.msgOrderFulfilled(mOrder);
		mOrder = null;
	}
	
	//ANGELICA: add in parameter
	public void DoGoToCustomer() {
		xDestination = xCustomer;
		yDestination = yCustomer;
		mCommand = EnumCommand.goToCustomer;
	}
	
	public void DoGoToDeliveryTruck() {
		xDestination = xDeliveryTruck;
		yDestination = yDeliveryTruck;
		mCommand = EnumCommand.goToDeliveryTruck;
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
	
	public void setItemsGui(MarketItemsGui g) {
		mItems = g;
	}
}
