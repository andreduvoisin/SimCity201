package restaurant.restaurant_duvoisin.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import restaurant.restaurant_duvoisin.AndreRestaurant;
import restaurant.restaurant_duvoisin.roles.AndreCustomerRole;
import base.Gui;

public class CustomerGui implements Gui{
	private AndreCustomerRole agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant};
	private Command command=Command.noCommand;

	static final int CUSTOMERSIZE = 20;	// Size of each side of customer (square).
	static final int STARTPOS = -40;
	private int WAIT_X = 15;
	static final int WAIT_Y = 15;
	static final int WAIT_INCREMENT = 25;
	public int WAIT_POS;
	static final int TEXT_OFFSET_X = 24;
    static final int TEXT_OFFSET_Y = 45;
	
	private String myChoice = "";
	
	Boolean notifiedAtWait = false;

	public CustomerGui(AndreCustomerRole c){ //HostAgent m) {
		agent = c;
		xPos = STARTPOS;
		yPos = STARTPOS;
		xDestination = STARTPOS;
		yDestination = STARTPOS;
		//maitreD = m;
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

		if (xPos == xDestination && yPos == yDestination
        		& (xDestination == WAIT_X + (WAIT_INCREMENT * WAIT_POS)) & (yDestination == WAIT_Y) && !notifiedAtWait) {
        	agent.msgAtWaitingArea();
        	notifiedAtWait = true;
        }
		if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.GoToSeat) agent.msgAtSeat();
			else if (command==Command.LeaveRestaurant) {
				agent.msgDoneLeaving();
				//System.out.println("about to call gui.setCustomerEnabled(agent);");
				isHungry = false;
				//gui.setCustomerEnabled(agent);
			}
			command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, CUSTOMERSIZE, CUSTOMERSIZE);
		g.setColor(Color.BLACK);
		g.drawString(myChoice, xPos + TEXT_OFFSET_X, yPos + TEXT_OFFSET_Y);	//yPos + 15
	}

	public boolean isPresent() {
		return isPresent;
	}
	public void setHungry() {
		isHungry = true;
		agent.msgGotHungry();
		setPresent(true);
	}
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	public void DoGoToWaitingArea() {
		for(int i = 0; i < AndreRestaurant.waitHere.length; i++)
			if(AndreRestaurant.waitHere[i] == false) {
				xDestination = WAIT_X + (WAIT_INCREMENT * i);
				yDestination = WAIT_Y;
				WAIT_POS = i;
				AndreRestaurant.waitHere[i] = true;
				notifiedAtWait = false;
				break;
			}
	}

	public void DoGoToSeat(int table) {//later you will map seatnumber to table coordinates.
		xDestination = AndreRestaurant.tgui.getTableX(table - 1);
		yDestination = AndreRestaurant.tgui.getTableY(table - 1);
		AndreRestaurant.waitHere[WAIT_POS] = false;
		command = Command.GoToSeat;
	}

	public void DoExitRestaurant() {
		xDestination = STARTPOS;
		yDestination = STARTPOS;
		command = Command.LeaveRestaurant;
		myChoice = "";
	}
	
	public void DoExitFullRestaurant() {
		xDestination = STARTPOS;
		yDestination = STARTPOS;
		command = Command.LeaveRestaurant;
		myChoice = "";
		AndreRestaurant.waitHere[WAIT_POS] = false;
	}
	
	public void OrderedChoice(String choice) {
		switch(choice) {
			case "steak":
				this.myChoice = "ST?";
				break;
			case "chicken":
				this.myChoice = "CH?";
				break;
			case "salad":
				this.myChoice = "SA?";
				break;
			case "pizza":
				this.myChoice = "PI?";
				break;
		}
	}
	
	public void RecievedChoice(String choice) {
		switch(choice) {
			case "steak":
				this.myChoice = "ST";
				break;
			case "chicken":
				this.myChoice = "CH";
				break;
			case "salad":
				this.myChoice = "SA";
				break;
			case "pizza":
				this.myChoice = "PI";
				break;
		}
	}
}