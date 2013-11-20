package restaurant_cwagoner.gui;

import restaurant_cwagoner.CustomerAgent;
import restaurant_cwagoner.WaiterAgent;

import java.awt.*;

public class CustomerGui implements Gui {

	private final int PLATE = 20;
	private static int customerNum = 0;
	
	private CustomerAgent agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;

	WaiterAgent waiter; 
	RestaurantGui restaurantGui;

	private int size = 20,
				GONE_X = -2 * size, GONE_Y = -2 * size,
				CASHIER_X = -size, CASHIER_Y = 100,
				WAITING_X, WAITING_Y,
				TABLE_X, TABLE_Y,
				xPos, yPos,
				xDestination, yDestination;
	
	private enum Command { noCommand, GoToRestaurant, GoToSeat, PayCashier, LeaveRestaurant };
	private Command command = Command.noCommand;
	
	private String food = "";
	
	public CustomerGui(CustomerAgent c, RestaurantGui g){
		agent = c;
		restaurantGui = g;
		WAITING_X = size;
		WAITING_Y = 100 - (customerNum % 4) * (size + 10);
		customerNum++;

        xPos = xDestination = GONE_X;
        yPos = yDestination = GONE_Y;
	}
	
	public void setTableLocation(int tableNum) {
		Dimension tableLoc = restaurantGui.getTableLocation(tableNum);
		TABLE_X = tableLoc.width;
		TABLE_Y = tableLoc.height;
	}

	public void updatePosition() {
		if (xPos < xDestination)		xPos++;
		else if (xPos > xDestination)	xPos--;

		if (yPos < yDestination)		yPos++;
		else if (yPos > yDestination)	yPos--;

		if (xPos == xDestination && yPos == yDestination) {
			
			if (command.equals(Command.GoToRestaurant)) {
				agent.msgGuiAtRestaurant();
			}
			
			if (command.equals(Command.GoToSeat)
					&& xDestination == TABLE_X && yDestination == TABLE_Y) {
				agent.msgGuiAtSeat();
			}
			
			else if (command.equals(Command.PayCashier)
						&& xDestination == CASHIER_X && yDestination == CASHIER_Y) {
				agent.msgGuiAtCashier();
			}
			
			else if (command.equals(Command.LeaveRestaurant)
					&& xDestination == GONE_X && yDestination == GONE_Y) {
				agent.msgGuiLeftRestaurant();
				isHungry = false;
				restaurantGui.setCustomerEnabled(agent);
			}
			command = Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, size, size);
		
		if (food.equals("")) {
	    	g.setColor(Color.GREEN);
	    	g.fillRect(xPos, yPos, size, size);
		}
		else {	// Either ordered or already eating
			if (food.length() == 2) { 	// Eating. Draw plate
				g.setColor(Color.WHITE);
	    		g.fillOval(xPos, yPos, PLATE, PLATE);
	    		g.setColor(Color.BLACK); // For delivered food// Write food name
				g.drawString(food, (int) (xPos + size / 5), (int) (yPos + size * 3 / 4));
			}
			else {	// Waiting for food
				g.setColor(Color.RED); // For undelivered food// Write food name
				g.drawString(food, xPos, (int) (yPos + size * 3 / 4));
			}
		}
	}

	
	// Accessors
	
	public boolean isPresent() {
		return isPresent;
	}
	
	public void setHungry() {
		isHungry = true;
		agent.msgGuiGotHungry();
		setPresent(true);
	}
	
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	public Dimension getPosition() {
		return new Dimension(xPos, yPos);
	}


	// Position changes
	
	public void DoGoToRestaurant() {
		xDestination = WAITING_X;
		yDestination = WAITING_Y;
		command = Command.GoToRestaurant;
	}
	
	public void DoGoToSeat(int seatNumber) {
		xDestination = TABLE_X;
		yDestination = TABLE_Y;
		command = Command.GoToSeat;
	}
	
	public void DoGoToCashier() {
		xDestination = CASHIER_X;
		yDestination = CASHIER_Y;
		command = Command.PayCashier;
	}

	public void DoExitRestaurant() {
		xDestination = GONE_X;
		yDestination = GONE_Y;
		command = Command.LeaveRestaurant;
	}
	
	
	// Display changes
	
	public void showFood(String text) {
		food = text;
	}
	
	public void clearFood() {
		food = "";
	}
}
