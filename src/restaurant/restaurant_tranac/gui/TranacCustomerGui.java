package restaurant.restaurant_tranac.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import restaurant.restaurant_tranac.roles.TranacRestaurantCustomerRole;

public class TranacCustomerGui implements Gui{

	private TranacRestaurantCustomerRole agent = null;
	private boolean isPresent = true;
	private String food;

	TranacRestaurantPanel gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private int xHost = 20, yHost = 20;
	private int xWaitingArea = 20, yWaitingArea = 60;
	private int xCashier = 230, yCashier = 180;
	private int xStart = -40, yStart = -40;
	private enum Command {noCommand, GoToHost, GoToWaitingArea, GoToSeat, GoToCashier, LeaveRestaurant};
	private Command command=Command.noCommand;

    private enum State {noState, asking, alerting, ordering, eating, paying};
    private State state = State.noState;

    BufferedImage image;
    BufferedImage check;
    BufferedImage alertBubble;
    BufferedImage askingBubble;

	public TranacCustomerGui(TranacRestaurantCustomerRole c, TranacRestaurantPanel gui){
		agent = c;
		xPos = -40;
		yPos = -40;
		xDestination = -40;
		yDestination = -40;
		this.gui = gui;
		
		//grabs the correct image using relative file paths
    	image = null;
    	try {
    	java.net.URL imageURL = this.getClass().getClassLoader().getResource("restaurant/restaurant_tranac/gui/images/red-nocturne.png");
    	image = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		System.out.println(e.getMessage());
    	}
    	
    	check = null;
    	try {
    	java.net.URL imageURL = this.getClass().getClassLoader().getResource("restaurant/restaurant_tranac/gui/images/check.png");
    	check = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		System.out.println(e.getMessage());
    	}
    	
    	alertBubble = null;
    	try {
    	java.net.URL imageURL = this.getClass().getClassLoader().getResource("restaurant/restaurant_tranac/gui/images/speech-attention.png");
    	alertBubble = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		System.out.println(e.getMessage());
    	}
    	
    	askingBubble = null;
    	try {
    	java.net.URL imageURL = this.getClass().getClassLoader().getResource("restaurant/restaurant_tranac/gui/images/speech-question.png");
    	askingBubble = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		System.out.println(e.getMessage());
    	}
	}
	
	public TranacCustomerGui(TranacRestaurantCustomerRole c){
		agent = c;
		xPos = -40;
		yPos = -40;
		xDestination = -40;
		yDestination = -40;
		this.gui = TranacRestaurantPanel.getInstance();
		
		//grabs the correct image using relative file paths
    	image = null;
    	try {
    	java.net.URL imageURL = this.getClass().getClassLoader().getResource("restaurant/restaurant_tranac/gui/images/red-nocturne.png");
    	image = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		System.out.println(e.getMessage());
    	}
    	
    	check = null;
    	try {
    	java.net.URL imageURL = this.getClass().getClassLoader().getResource("restaurant/restaurant_tranac/gui/images/check.png");
    	check = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		System.out.println(e.getMessage());
    	}
    	
    	alertBubble = null;
    	try {
    	java.net.URL imageURL = this.getClass().getClassLoader().getResource("restaurant/restaurant_tranac/gui/images/speech-attention.png");
    	alertBubble = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		System.out.println(e.getMessage());
    	}
    	
    	askingBubble = null;
    	try {
    	java.net.URL imageURL = this.getClass().getClassLoader().getResource("restaurant/restaurant_tranac/gui/images/speech-question.png");
    	askingBubble = ImageIO.read(imageURL);
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

		//sends the correct message to the agent
		if (xPos == xDestination && yPos == yDestination) {
			switch(command) {
				case GoToHost: {
					agent.msgAnimationAtHost();
					break;
				}
				case GoToWaitingArea: {
					agent.msgAnimationAtWaitingArea();
					break;
				}
				case GoToSeat: {
					agent.msgAnimationFinishedGoToSeat();
					break;
				}
				case GoToCashier: {
					agent.msgAnimationFinishedGoToCashier();
					break;
				}
				case LeaveRestaurant: {
					agent.msgAnimationFinishedLeaveRestaurant();
		//			gui.setEnabled(agent);
					break;
				}
				default:
					break;
			}
			command=Command.noCommand;	
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.drawImage(image,xPos,yPos,null);
		
    	switch(state) {
		case ordering: {
        	g.drawString(food+"?", xPos-2, yPos-5);
        	break;
		}
		case eating: {
        	g.drawString(food, xPos-2, yPos-5);
        	break;
		}
		case paying: {
			g.drawImage(check, xPos-14, yPos+10, null);
			break;
		}
		case asking: {
			g.drawImage(askingBubble, xPos-14, yPos+10, null);
			break;
		}
		case alerting: {
			g.drawImage(alertBubble, xPos-14, yPos+10, null);
			break;
		}
		default:
			break;
    	}
	}

	public void setFood(String f) {				//sets the string for the food icon
    	if(f.equals("Steak"))
    		food = "STK";
    	else if(f.equals("Chicken"))
    		food = "CHK";
    	else if(f.equals("Salad"))
    		food = "SLD";
    	else
    		food = "PIZ";
	}
	/*
	public void setHungry() {
		agent.msgGotHungry();
		setPresent(true);
	}
	*/
	public boolean isPresent() {
		return isPresent;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

    public void setAsking() {
    	state = State.asking;
    }
    
    public void setAlerting() {
    	state = State.alerting;
    }
    
    public void setPaying() {
    	state = State.paying;
    }
    
	public void setEating() {					//tells the graphics to remove the '?' from the food icon
    	state = State.eating;
	}

	public void setOrdering() {
		state = State.ordering;
	}
	
    public void setClear() {
    	state = State.noState;
    }
    
	/** Actions called from Customer Agent */
/*	public void DoSetEnabled() {
		gui.setEnabled(agent);
	}
*/	
	public void DoGoToHost() {
		xDestination = xHost;
		yDestination = yHost;
		command = Command.GoToHost;
	}
	
	public void DoGoToWaitingArea(int n) {
		xDestination = xWaitingArea + 30*(int)(n/10);
		yDestination = yWaitingArea + 35*(n % 10);
		
		command = Command.GoToWaitingArea;
	}
	
	public void DoGoToSeat(int xTable, int yTable) {
		xDestination = xTable;
		yDestination = yTable;
		command = Command.GoToSeat;
	}
	
	public void DoGoToCashier() {
		xDestination = xCashier;
		yDestination = yCashier;
		command = Command.GoToCashier;
	}
	
	public void DoExitRestaurant() {
		xDestination = xStart;
		yDestination = yStart;
		command = Command.LeaveRestaurant;
	}
}
