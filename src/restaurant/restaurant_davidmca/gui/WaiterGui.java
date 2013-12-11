package restaurant.restaurant_davidmca.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import city.gui.SimCityGui;
import restaurant.restaurant_davidmca.Table;
import restaurant.restaurant_davidmca.interfaces.Waiter;
import base.Gui;

public class WaiterGui implements Gui {

	private Waiter role = null;

	private int xPos, yPos = 20;
	private int xHome, yHome;
	private int xDestination = 20, yDestination = 20;
	private static int WaiterSize = 20;
	private String labelText = "";
	
	private boolean onFire = false;
	private BufferedImage fireImage;

	private boolean currentlyAnimating;

	public WaiterGui(Waiter agent, int home) {
		currentlyAnimating = false;
		this.role = agent;
		xHome = 10;
		yHome = 150+(100*home);
		xPos = xHome;
		yPos = yHome;
		
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
			xPos += 1;
		else if (xPos > xDestination)
			xPos -= 1;
		if (yPos < yDestination)
			yPos += 1;
		else if (yPos > yDestination)
			yPos -= 1;
		if (xPos == xDestination && yPos == yDestination && currentlyAnimating) {
			currentlyAnimating = false;
			role.msgDoneAnimating();
		}
	}

	public void wantsBreak() {
		role.RequestBreak();
	}

	public boolean isOnBreak() {
		return role.isOnBreak();
	}

	public void draw(Graphics2D g) {
		if(onFire)
			g.drawImage(fireImage, xPos, yPos, null);
		else{
		g.setColor(Color.CYAN);
		g.fillRect(xPos, yPos, WaiterSize, WaiterSize);
		 if (SimCityGui.TESTING) 
         	g.setColor(Color.BLACK);
         else 
         	g.setColor(Color.WHITE);
		g.drawString(labelText, xPos, yPos);}
	}

	public boolean isPresent() {
		return true;
	}

	public void setLabelText(String text) {
		labelText = text;
	}

	public void DoGoToTable(Table table) {
		currentlyAnimating = true;
		xDestination = table.getX() + 20;
		yDestination = table.getY() - 20;
	}

	public void DoGoToCustomer(int loc) {
		currentlyAnimating = true;
		xDestination = loc;
		yDestination = 30;
	}

	public void DoGoToKitchen() {
		currentlyAnimating = true;
		xDestination = 250;
		yDestination = 450;
	}
	
	public void DoGoToFront() {
		currentlyAnimating = true;
		xDestination = xHome;
		yDestination = yHome;
	}

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}

	@Override
	public void setPresent(boolean state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFired(boolean state) {
		onFire = state;
	}
}
