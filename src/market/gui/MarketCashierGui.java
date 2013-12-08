package market.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import market.interfaces.MarketCashier;
import market.roles.MarketCashierRole;
import market.roles.MarketCustomerRole;
import market.test.mock.MockCashier;
import city.gui.SimCityGui;

public class MarketCashierGui implements MarketBaseGui {
	private MarketCashier mAgent;
	
	private static final int xStart = -20, yStart = -20;
	private static final int xHome = 100, yHome = 50;
	
	private int xPos = xStart, yPos = yStart;
	private int xDestination = xHome, yDestination = yHome;
//	private int xDestination = xStart, yDestination = yStart;
	
	public enum EnumCommand {noCommand, goToPosition, leaveMarket};
	public EnumCommand mCommand = EnumCommand.noCommand;
	
	BufferedImage image;
	
	public MarketCashierGui(MarketCashierRole agent) {
		mAgent = agent;
		
    	image = null;
    	try {
    	java.net.URL imageURL = this.getClass().getClassLoader().getResource("restaurant/restaurant_tranac/gui/images/green-requiem.png");
    	image = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		System.out.println(e.getMessage());
    	}
	}
	
	/* For animation unit testing. */
	public MarketCashierGui(MockCashier mCashier) {
		mAgent = mCashier;
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
		if(SimCityGui.GRADINGVIEW) {
			g.setColor(Color.BLACK);
			g.drawString("CASH",xPos,yPos);
		}
		else
			g.drawImage(image,xPos,yPos,null);
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
		if(mAgent instanceof MarketCustomerRole) {
			MarketCustomerRole role = (MarketCustomerRole) mAgent;
			return role.getPerson() != null ? true : false;
		}
		else
			return false;
	}
	
	public int getXPos() {
		return xPos;
	}
	
	public int getYPos() {
		return yPos;
	}
}
