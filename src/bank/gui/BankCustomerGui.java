package bank.gui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import bank.Bank;
import bank.interfaces.BankCustomer;
import bank.roles.BankCustomerRole;
import base.ContactList;
import base.Gui;

public class BankCustomerGui implements Gui {

	private BankCustomer agent = null;
	private boolean isPresent = false;

	private Timer timer = new Timer(); 
	
	private static final int deathTime = 15000;
	
	private int xPos, yPos;
	private int xDestination, yDestination;
	
	private boolean isMovingToTeller = false;

	static final int CUSTOMERSIZE = 20;	// Size of each side of customer (square).
	static final int STARTPOSX = 235;
	static final int STARTPOSY = -50;
	
	private Bank	mBank;
	private int positionInLine;
	
	//Animation Upgrades 
	private BufferedImage image;
	
	//STATIC VARIABLES
	static final int INTERACT_X1 = 250;
	static final int INTERACT_X2 = 200;
	static final int INTERACT_X3 = 300;
	static final int INTERACT_Y = 420;
	
	public static int LINE_POSITION = 0;
	
	static final int LINE_X = 225;
	static final int LINE_Y = 320;
	static final int LINE_INCREMENT = -25;	// in the y

	public BankCustomerGui(BankCustomer bc) {
		agent = bc;
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
    		//System.out.println(e.getMessage());
    	}
    	
    	mBank = ContactList.sBankList.get(bc.getBankID());
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
		
		if(xPos == xDestination && yPos == yDestination && isMovingToTeller) {
			isMovingToTeller = false;
			timer.schedule(new TimerTask(){
				public void run(){
					agent.msgAtLocation();
				}
			}, 2000); 
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
		positionInLine = LINE_POSITION++;
		xDestination = LINE_X;
		yDestination = LINE_Y + (LINE_INCREMENT * positionInLine);
	}
	
	public void DoLeaveBank() {
		xDestination = INTERACT_X1;
		yDestination = -50;
	}
	//ANDRE: how do we fix this... Possible to call mBank.mCustomerGuis() in bankPanel?
	public void DoGoToTeller() {
		xDestination = INTERACT_X1;
		yDestination = INTERACT_Y;
		mBank.updateCustomerLine();
		isMovingToTeller = true;
	}
	
	public void DoGoToTeller(int location) {
		xDestination = INTERACT_X1;
		yDestination = INTERACT_Y;
		mBank.updateCustomerLine();
		isMovingToTeller = true;
	}
	
	public void moveForwardInLine() {
		positionInLine--;
		if(positionInLine >= 0) {
			xDestination = LINE_X;
			yDestination = LINE_Y + (LINE_INCREMENT * positionInLine);
		}
	}
	
	public void die(){
		xDestination = xPos;
		yDestination = yPos;
		try {
    		java.net.URL imageURL = this.getClass().getClassLoader().getResource("city/gui/images/Blood_Splatter.png");
    	image = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		//System.out.println(e.getMessage());
    	}
		timer.schedule(new TimerTask(){
			public void run(){
				setPresent(false);
				((BankCustomerRole)agent).mTeller.msgLeaving();
			}
		}, deathTime);
	}

	@Override
	public void setFired(boolean state) {
		// TODO Auto-generated method stub
		
	}
}