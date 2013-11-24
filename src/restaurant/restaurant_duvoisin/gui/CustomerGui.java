package restaurant.restaurant_duvoisin.gui;

import restaurant.restaurant_duvoisin.CustomerAgent;

import java.awt.*;

public class CustomerGui implements Gui{

	private CustomerAgent agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;

	//private HostAgent host;
	RestaurantGui gui;

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
	
	TableGui myTables;
	
	private String myChoice = "";
	
	Boolean notifiedAtWait = false;

	public CustomerGui(CustomerAgent c, RestaurantGui gui){ //HostAgent m) {
		agent = c;
		xPos = STARTPOS;
		yPos = STARTPOS;
		xDestination = STARTPOS;
		yDestination = STARTPOS;
		//maitreD = m;
		this.gui = gui;
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
				gui.setCustomerEnabled(agent);
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
		for(int i = 0; i < gui.waitHere.length; i++)
			if(gui.waitHere[i] == false) {
				xDestination = WAIT_X + (WAIT_INCREMENT * i);
				yDestination = WAIT_Y;
				WAIT_POS = i;
				gui.waitHere[i] = true;
				notifiedAtWait = false;
				break;
			}
	}

	public void DoGoToSeat(int table) {//later you will map seatnumber to table coordinates.
		xDestination = myTables.getTableX(table - 1);
		yDestination = myTables.getTableY(table - 1);
		gui.waitHere[WAIT_POS] = false;
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
		gui.waitHere[WAIT_POS] = false;
	}
	
	public void setTables(TableGui tg) { myTables = tg; }
	
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