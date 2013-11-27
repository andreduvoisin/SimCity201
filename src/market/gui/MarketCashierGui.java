package market.gui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import base.BaseRole;
import market.interfaces.MarketCashier;
import market.roles.MarketCashierRole;

public class MarketCashierGui implements MarketBaseGui {
	private MarketCashier mAgent;

	private boolean isPresent;
	
	private static final int xStart = -20, yStart = -20;
	private static final int xHome = 100, yHome = 50;
	
	private int xPos = xStart, yPos = yStart;
	private int xDestination = xHome, yDestination = yHome;
//	private int xDestination = xStart, yDestination = yStart;
	private static final int SIZE = 20;
	
	public enum EnumCommand {noCommand, goToPosition, leaveMarket};
	public EnumCommand mCommand = EnumCommand.noCommand;
	
	BufferedImage image;
	
	public MarketCashierGui(MarketCashier agent) {
		mAgent = agent;
		
		isPresent = true;
		
    	image = null;
    	try {
    	java.net.URL imageURL = this.getClass().getClassLoader().getResource("restaurant/restaurant_tranac/gui/images/green-requiem.png");
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
/*		g.setColor(Color.ORANGE);
		g.fillRect(xPos, yPos, SIZE, SIZE);
*/		g.drawImage(image,xPos,yPos,null);
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
		MarketCashierRole c = (MarketCashierRole) mAgent;
	//	return c.getActive();
		return isPresent;
	}
	
	public void setPresent() {
		MarketCashierRole c = (MarketCashierRole) mAgent;
		c.setActive();
	//	isPresent = !isPresent;
		isPresent= true;
	}
	
	public int getXPos() {
		return xPos;
	}
	
	public int getYPos() {
		return yPos;
	}
}
