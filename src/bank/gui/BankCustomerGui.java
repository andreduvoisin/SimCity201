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
		xDestination = BankPanel.LINE_X;
		yDestination = BankPanel.LINE_Y + (BankPanel.LINE_INCREMENT * BankPanel.LINE_POSITION);
		positionInLine = BankPanel.LINE_POSITION;
		BankPanel.LINE_POSITION++;
	}
	
	public void DoLeaveBank() {
		xDestination = -20;
		yDestination = -20;
	}

	public void DoGoToTeller() {
		xDestination = BankPanel.INTERACT_X;
		yDestination = BankPanel.INTERACT_Y;
		bankPanel.updateCustomerLine();
		isMovingToTeller = true;
	}
	
	public void moveForwardInLine() {
		positionInLine--;
		if(positionInLine >= 0) {
			xDestination = BankPanel.LINE_X;
			yDestination = BankPanel.LINE_Y + (BankPanel.LINE_INCREMENT * BankPanel.LINE_POSITION);
		}
	}
}