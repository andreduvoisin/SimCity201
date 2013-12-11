package bank.gui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import bank.interfaces.BankTeller;
import base.Gui;

public class BankTellerGui implements Gui {

//	private BankTeller bankTeller = null;
	private boolean isPresent = true;
	
	private int xPos, yPos;
	private int xDestination, yDestination;
	
	//Animation Upgrades
	private boolean onFire = false;
	private BufferedImage fireImage;
	private BufferedImage bankTellerImage;
	

	static final int CUSTOMERSIZE = 20;	// Size of each side of customer (square).
	static final int STARTPOSX1 = 225;
	static final int STARTPOSX2 = 200;
	static final int STARTPOSX3 = 300;
	static final int STARTPOSY = 450;
	
	public BankTellerGui(BankTeller bt) {
//		bankTeller = bt;
		xPos =  225;
		yPos = 0;
		xDestination = STARTPOSX1;
		yDestination = STARTPOSY;
		
		bankTellerImage = null;
    	try {
    		java.net.URL imageURL = this.getClass().getClassLoader().getResource("city/gui/images/goblin.png");
    		bankTellerImage = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		//System.out.println(e.getMessage());
    	}
    	fireImage = null;
    	try {
    		java.net.URL imageURL = this.getClass().getClassLoader().getResource("city/gui/images/fire.png");
    		fireImage = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		//System.out.println(e.getMessage());
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
		if(onFire)
			g.drawImage(fireImage, xPos, yPos, null);
		else
			g.drawImage(bankTellerImage, xPos, yPos, null);
	}

	public boolean isPresent() {
		return isPresent;
	}

	public void setPresent(boolean state) {
		isPresent = state;
	}
	
//	MESSAGES
	public void DoLeaveBank() {
		xDestination = 220;
		yDestination = -50;
	}

	@Override
	public void setFired(boolean state) {
		onFire = state;
	}
}