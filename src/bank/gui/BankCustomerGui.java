package bank.gui;

import bank.interfaces.BankCustomer;
import base.Gui;
import base.Location;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BankCustomerGui implements Gui {

	private BankCustomer agent = null;
	private boolean isPresent = false;
	private BankPanel bankPanel;

	private int xPos, yPos;
	private int xDestination, yDestination;
	
	private boolean isMovingToTeller = false;

	static final int CUSTOMERSIZE = 20;	// Size of each side of customer (square).
	static final int STARTPOSX = 235;
	static final int STARTPOSY = -50;
	
	private int positionInLine;
	
	//Animation Upgrades 
	private BufferedImage image;
	
	static final int INTERACT_X1 = 250;
	static final int INTERACT_X2 = 200;
	static final int INTERACT_X3 = 300;
	static final int INTERACT_Y = 420;

	public BankCustomerGui(BankCustomer bc, BankPanel bp) {
		agent = bc;
		bankPanel = bp;
		xPos = STARTPOSX;
		yPos = STARTPOSY;
		xDestination = STARTPOSX;
		yDestination = STARTPOSY;
		
		image = null;
    	try {
    		java.net.URL imageURL = this.getClass().getClassLoader().getResource("city/gui/images/person.png");
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
		
		if(xPos == xDestination && yPos == yDestination == isMovingToTeller) {
			isMovingToTeller = false;
			agent.msgAtLocation();
		}
	}

	public void draw(Graphics2D g) {
		g.drawImage(image, xPos, yPos, null);
	}

	public boolean isPresent() {
		return isPresent;
	}

	public void setPresent(boolean state) {
		isPresent = state;
	}
	/*
	public void DoGoTo(Location location) {
		xDestination = location.mX;
		yDestination = location.mY;
		isMoving = true;
	}
	*/
	public void DoGoWaitInLine() {
		isPresent = true;
		positionInLine = BankPanel.LINE_POSITION++;
		xDestination = BankPanel.LINE_X;
		yDestination = BankPanel.LINE_Y + (BankPanel.LINE_INCREMENT * positionInLine);
	}
	
	public void DoLeaveBank() {
		xDestination = -50;
		yDestination = -50;
	}

	public void DoGoToTeller() {
		xDestination = INTERACT_X1;
		yDestination = INTERACT_Y;
		bankPanel.updateCustomerLine();
		isMovingToTeller = true;
	}
	
	public void DoGoToTeller(int location) {
		switch (location){
		case 1: xDestination = INTERACT_X1; 
		case 2: xDestination = INTERACT_X2;
		case 3: xDestination = INTERACT_X3;
		default: xDestination = INTERACT_X1;
		}
		yDestination = INTERACT_Y;
		bankPanel.updateCustomerLine();
		isMovingToTeller = true;
	}
	
	public void moveForwardInLine() {
		positionInLine--;
		if(positionInLine >= 0) {
			xDestination = BankPanel.LINE_X;
			yDestination = BankPanel.LINE_Y + (BankPanel.LINE_INCREMENT * positionInLine);
		}
	}
}