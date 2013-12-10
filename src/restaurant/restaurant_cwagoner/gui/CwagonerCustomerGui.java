package restaurant.restaurant_cwagoner.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import restaurant.restaurant_cwagoner.roles.CwagonerCustomerRole;
import restaurant.restaurant_cwagoner.roles.CwagonerWaiterRole;
import base.Location;

public class CwagonerCustomerGui extends CwagonerBaseGui implements CwagonerGui {

	private final int PLATE = 20;
	private static int customerNum = 0;
	
	private boolean isHungry = false;

	CwagonerWaiterRole waiter;

	private int size = 20;
	private Location gonePos = new Location(-2 * size, -2 * size),
					cashierPos = new Location(-size, 100),
					waitingPos = new Location(size, 100 - (customerNum % 4) * (size + 10)),
					tablePos = new Location(0, 0),
					position,
					destination;
	
	private enum Command { noCommand, GoToRestaurant, GoToSeat, PayCashier, LeaveRestaurant };
	private Command command = Command.noCommand;
	
	private String food = "";

	public CwagonerCustomerGui(CwagonerCustomerRole c, CwagonerAnimationPanel panel) {
		super(c, panel);
		customerNum++;

		CwagonerAnimationPanel.restaurant.addGui(this);

        position = new Location(waitingPos.mX, waitingPos.mY);
        destination = new Location(waitingPos.mX, waitingPos.mY);
	}
	
	public void setTableLocation(int tableNum) {
		Location tableLoc = animationPanel.getTableLocation(tableNum);
		tablePos.setTo(tableLoc);
	}

	public void updatePosition() {
		if (position.mX < destination.mX)		position.mX++;
		else if (position.mX > destination.mX)	position.mX--;

		if (position.mY < destination.mY)		position.mY++;
		else if (position.mY > destination.mY)	position.mY--;

		if (position.mX == destination.mX && position.mY == destination.mY) {
			
			if (command.equals(Command.GoToSeat)
					&& destination.mX == tablePos.mX && destination.mY == tablePos.mY) {
				((CwagonerCustomerRole)role).msgGuiAtSeat();
			}
			
			else if (command.equals(Command.PayCashier)
						&& destination.mX == cashierPos.mX && destination.mY == cashierPos.mY) {
				((CwagonerCustomerRole)role).msgGuiAtCashier();
			}
			
			else if (command.equals(Command.LeaveRestaurant)
					&& destination.mX == gonePos.mX && destination.mY == gonePos.mY) {
				((CwagonerCustomerRole)role).msgGuiLeftRestaurant();
				isHungry = false;
			}
			command = Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(position.mX, position.mY, size, size);
		
		if (food.equals("")) {
	    	g.setColor(Color.GREEN);
	    	g.fillRect(position.mX, position.mY, size, size);
		}
		else {	// Either ordered or already eating
			if (food.length() == 2) { 	// Eating. Draw plate
				g.setColor(Color.WHITE);
	    		g.fillOval(position.mX, position.mY, PLATE, PLATE);
	    		g.setColor(Color.BLACK); // For delivered food// Write food name
				g.drawString(food, (int) (position.mX + size / 5), (int) (position.mY + size * 3 / 4));
			}
			else {	// Waiting for food
				g.setColor(Color.RED); // For undelivered food// Write food name
				g.drawString(food, position.mX, (int) (position.mY + size * 3 / 4));
			}
		}
	}

	
	// Accessors
	
	public boolean isHungry() {
		return isHungry;
	}
	
	public Dimension getPosition() {
		return new Dimension(position.mX, position.mY);
	}


	// Position changes
	
	public void DoGoToRestaurant() {
		destination.setTo(waitingPos);
		command = Command.GoToRestaurant;
	}
	
	public void DoGoToSeat(int seatNumber) {
		destination.setTo(tablePos);
		command = Command.GoToSeat;
	}
	
	public void DoGoToCashier() {
		destination.setTo(cashierPos);
		command = Command.PayCashier;
	}

	public void DoExitRestaurant() {
		destination.setTo(gonePos);
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
