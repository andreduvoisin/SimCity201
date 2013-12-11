package market.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.Semaphore;

import javax.imageio.ImageIO;

import market.MarketOrder;
import market.interfaces.MarketWorker;
import market.roles.MarketWorkerRole;
import base.Item.EnumItemType;
import city.gui.SimCityGui;

public class MarketWorkerGui implements MarketBaseGui {
	private MarketWorker mAgent;
	private int mNum;

	private boolean onFire = false;
	private BufferedImage fireImage;


	private MarketOrder mOrder = null;
	
	private static final int xStart = -20, yStart = -20;
	private static final int xBase = 60, yBase = 450;
	private static final int xDeliveryTruck = 230, yDeliveryTruck = 0;
	private int xCustomerBase = 50, yCustomerBase = 140;
	
	private int xPos, yPos;
	private int xHome, yHome;
	private int xDestination = xHome, yDestination = yHome;
	
	private enum EnumCommand {noCommand, goToMarket, fulFillOrder, goToItem, goToCashier, goToCustomer, goToDeliveryTruck, leaveMarket};
	private EnumCommand mCommand = EnumCommand.noCommand;
	
	private Semaphore gettingItem = new Semaphore(0,true);
	
	BufferedImage image;
	
	public MarketWorkerGui(MarketWorker agent, int i) {
		mAgent = agent;
		mNum = i;
        xHome = xBase + 30*(i % 5);
        yHome = yBase - 30*(int)(i/5);

        xPos = xHome;
        yPos = yHome;
        xDestination = xHome;
        yDestination = yHome;
	
    	image = null;
    	try {
    	java.net.URL imageURL = this.getClass().getClassLoader().getResource("market/gui/images/worker.png");
    	image = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		System.out.println(e.getMessage());
    	}
    	fireImage = null;
    	try {
    		java.net.URL imageURL = this.getClass().getClassLoader().getResource("city/gui/images/fire.png");
    		fireImage = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		System.out.println(e.getMessage());
    	}
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
        		gettingItem.release();
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
		if(onFire)
			g.drawImage(fireImage, xPos, yPos, null);
		else if(SimCityGui.GRADINGVIEW) {
			g.setColor(Color.BLACK);
			g.drawString("W"+mNum,xPos,yPos);
		}
		else
			g.drawImage(image,xPos,yPos,null);
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
			MarketWorkerRole r = (MarketWorkerRole) mAgent;
			if(mOrder.mItems.get(item) != 0) {
				MarketCoordinates c = r.mMarket.mItemsGui.getItemCoordinates(item);
				xDestination = c.getX()-30;
				yDestination = c.getY();
				mCommand = EnumCommand.goToItem;
				try {
					gettingItem.acquire();
				}
				catch(InterruptedException e) {
					e.printStackTrace();
				}
				mCommand = EnumCommand.noCommand;
			r.mMarket.mItemsGui.decreaseItemCount(item, mOrder.mItems.get(item));		
			}
		}
		mAgent.msgOrderFulfilled(mOrder);
		mOrder = null;
	}
	
	//ANGELICA: add in parameter
	public void DoGoToCustomer() {
		xDestination = xCustomerBase;
		yDestination = yCustomerBase;
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
		if(mAgent instanceof MarketWorkerRole) {
			MarketWorkerRole role = (MarketWorkerRole) mAgent;
			return role.getPerson() != null ? true : false;
		}
		else return false;
	}
	
	public int getXPos() {
		return xPos;
	}
	
	public int getYPos() {
		return yPos;
	}
	
	public void setFired(boolean state){
		onFire = state;
	}
}
