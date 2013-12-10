package bank.gui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import bank.interfaces.BankGuard;
import base.Gui;

public class BankGuardGui implements Gui {

//	private BankGuard agent = null;
	private boolean isPresent = true;
	

	private int xPos, yPos;
	private int xDestination, yDestination;

	static final int CUSTOMERSIZE = 20;	// Size of each side of customer (square).
	static final int STARTPOSX = 200;
	static final int STARTPOSY = 40;
	
	//Animation upgrades
	private BufferedImage bankguardimage;
	//Jerry changed the constructors for both this and the customer constructers
	public BankGuardGui(BankGuard bg){
//		agent = bg;
		xPos = STARTPOSX;
		yPos = STARTPOSY;
		xDestination = STARTPOSX;
		yDestination = STARTPOSY;
		
		bankguardimage = null;
    	try {
    		java.net.URL imageURL = this.getClass().getClassLoader().getResource("city/gui/images/dad1.png");
    		bankguardimage = ImageIO.read(imageURL);
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
	}

	public void draw(Graphics2D g) {
		g.drawImage(bankguardimage, xPos, yPos, null);
	}

	public boolean isPresent() {
		return isPresent;
	}

	public void setPresent(boolean state) {
		isPresent = state;
	}
	
//	MESSAGES
	public void DoKillRobber () {
		
	}
	
}