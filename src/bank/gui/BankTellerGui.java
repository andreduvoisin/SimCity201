package bank.gui;

import bank.interfaces.BankCustomer;
import bank.interfaces.BankTeller;
import base.Gui;
import base.Location;

import java.awt.*;

public class BankTellerGui implements Gui {

	private BankTeller agent = null;
	private boolean isPresent = true;
	private BankPanel bankPanel;

	private int xPos, yPos;
	private int xDestination, yDestination;

	static final int CUSTOMERSIZE = 20;	// Size of each side of customer (square).
	static final int STARTPOSX = 250;
	static final int STARTPOSY = 470;

	public BankTellerGui(BankTeller bt, BankPanel bp) {
		agent = bt;
		bankPanel = bp;
		xPos = STARTPOSX;
		yPos = STARTPOSY;
		xDestination = STARTPOSX;
		yDestination = STARTPOSY;
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
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.BLUE);
		g.fillRect(xPos, yPos, CUSTOMERSIZE, CUSTOMERSIZE);
	}

	public boolean isPresent() {
		return isPresent;
	}

	public void setPresent(boolean state) {
		isPresent = state;
	}
}