package bank.gui;

import bank.interfaces.BankCustomer;
import base.Gui;
import base.Location;

import java.awt.*;

public class BankCustomerGui implements Gui {

	private BankCustomer agent = null;
	private boolean isPresent = false;
	private BankPanel bankPanel;

	private int xPos, yPos;
	private int xDestination, yDestination;
	
	private boolean isMovingToTeller = false;

	static final int CUSTOMERSIZE = 20;	// Size of each side of customer (square).
	static final int STARTPOS = -20;
	
	private int positionInLine;
	
	
	static final int INTERACT_X1 = 250;
	static final int INTERACT_X2 = 200;
	static final int INTERACT_X3 = 300;
	static final int INTERACT_Y = 420;

	public BankCustomerGui(BankCustomer bc, BankPanel bp) {
		agent = bc;
		bankPanel = bp;
		xPos = STARTPOS;
		yPos = STARTPOS;
		xDestination = STARTPOS;
		yDestination = STARTPOS;
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
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, CUSTOMERSIZE, CUSTOMERSIZE);
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
		xDestination = -20;
		yDestination = -20;
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