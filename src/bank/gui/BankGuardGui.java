package bank.gui;

import bank.interfaces.BankCustomer;
import bank.interfaces.BankGuard;
import base.Gui;

import java.awt.*;

public class BankGuardGui implements Gui {

	private BankGuard agent = null;
	private boolean isPresent = true;

	private int xPos, yPos;
	private int xDestination, yDestination;

	static final int CUSTOMERSIZE = 20;	// Size of each side of customer (square).
	static final int STARTPOS = 0;

	public BankGuardGui(BankGuard bg) {
		agent = bg;
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
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.RED);
		g.fillRect(xPos, yPos, CUSTOMERSIZE, CUSTOMERSIZE);
	}

	public boolean isPresent() {
		return isPresent;
	}

	public void setPresent(boolean state) {
		isPresent = state;
	}
}