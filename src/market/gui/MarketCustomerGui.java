package market.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.imageio.ImageIO;

import market.interfaces.MarketCustomer;
import city.gui.SimCityGui;

public class MarketCustomerGui implements MarketBaseGui {
	private MarketCustomer mAgent;
	private int mNum;
	
	private static int sNumWaitingSpaces = 5;
	private static List<Semaphore> sWaitingSpaces = Collections.synchronizedList(new ArrayList<Semaphore>());

	private static final int xStart = -20, yStart = -20;
	private static final int xMarket = 70, yMarket = 50;
	private static final int xWaitingArea = 50, yWaitingArea = 420;
	
	private int xPos = xStart, yPos = yStart;
	private int xDestination = xStart, yDestination = yStart;
	private static final int SIZE = 20;
	
	private enum EnumCommand {noCommand, goToMarket, goToWaitingArea, leaveMarket};
	private EnumCommand mCommand = EnumCommand.noCommand;
	
	BufferedImage image;
	
	public MarketCustomerGui(MarketCustomer agent) {
		mAgent = agent;
		
		synchronized(sWaitingSpaces) {
		if(sWaitingSpaces.size() != 0) {
			for(int i=0;i<sNumWaitingSpaces;i++) {
				sWaitingSpaces.add(new Semaphore(1,true));
			}
		}
		}
		
    	image = null;
    	try {
    	java.net.URL imageURL = this.getClass().getClassLoader().getResource("restaurant/restaurant_tranac/gui/images/red-nocturne.png");
    	image = ImageIO.read(imageURL);
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
		if(SimCityGui.GRADINGVIEW) {
			g.setColor(Color.BLACK);
			g.drawString("MCustomer", xPos, yPos);
		}
		else
			g.drawImage(image, xPos, yPos, null);
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
		for(int i=0;i<sNumWaitingSpaces;i++) {
			if(sWaitingSpaces.get(i).tryAcquire()) {
				mNum = i;
				xDestination = xWaitingArea - 30*i;
				yDestination = yWaitingArea;
			}
		}
		mCommand = EnumCommand.goToWaitingArea;
	}
	
	public void DoLeaveMarket() {
		sWaitingSpaces.get(mNum).release();
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
