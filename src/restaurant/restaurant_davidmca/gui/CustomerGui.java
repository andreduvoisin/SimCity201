package restaurant.restaurant_davidmca.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import base.Gui;
import restaurant.restaurant_davidmca.interfaces.Customer;

public class CustomerGui implements Gui {

	private Customer role = null;
	private boolean isPresent = false;
	private boolean isHungry = false;
	private static int CustomerSize = 20;
	private String labelText = "";

	DavidRestaurantGui gui;

	private int xPos, yPos;
	private int xHome, yHome;
	private int xDestination, yDestination;

//	private enum Command {
//		noCommand, GoToSeat, LeaveRestaurant, WaitingArea
//	};

//	private Command command = Command.noCommand;
	private boolean currentlyAnimating = false;

	public CustomerGui(Customer c, DavidRestaurantGui gui, int home) {
		currentlyAnimating = false;
		this.role = c;
		xPos = -40;
		yPos = -40;
		yHome = 10;
		xHome = 30*(home);
		xDestination = -40;
		yDestination = -40;
		//this.gui = gui;
	}

	public void updatePosition() {
		if (xPos < xDestination)
			xPos += 2;
		else if (xPos > xDestination)
			xPos -= 2;

		if (yPos < yDestination)
			yPos += 2;
		else if (yPos > yDestination)
			yPos -= 2;
		if (xPos == xDestination && yPos == yDestination && currentlyAnimating) {
			currentlyAnimating = false;
			role.msgDoneAnimating();
		}
//		if (xPos == xDestination && yPos == yDestination) {
//			if (command == Command.WaitingArea) {
//				role.msgAnimationFinishedGoToWaitingArea();
//			}
//			if (command == Command.GoToSeat) {
//				role.msgAnimationFinishedGoToSeat();
//			}
//			else if (command == Command.LeaveRestaurant) {
//				role.msgAnimationFinishedLeaveRestaurant();
//				System.out
//						.println("about to call gui.setCustomerEnabled(restaurant_davidmca.agent);");
//				isHungry = false;
//				gui.setCustomerEnabled(role);
//			}
//			command = Command.noCommand;
//		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.BLUE);
		g.fillRect(xPos, yPos, CustomerSize, CustomerSize);
		g.setColor(Color.BLACK);
		g.drawString(labelText, xPos, yPos);
	}

	public boolean isPresent() {
		return isPresent;
	}

	public void setLabelText(String text) {
		labelText = text;
	}

	public void setHungry() {
		isHungry = true;
		role.gotHungry();
		setPresent(true);
	}

	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	public void DoGoToWaitingArea() {
		xDestination = xHome;
		yDestination = yHome;
		currentlyAnimating = true;
	}

	public void DoGoToSeat(int tablex, int tabley) {
		System.out.println("DoGoToSeat");
		xDestination = tablex;
		yDestination = tabley;
		currentlyAnimating = true;
	}

	public void DoExitRestaurant() {
		xDestination = -40;
		yDestination = -40;
		currentlyAnimating = true;
	}

	public int getHomeLocation() {
		return xHome;
	}
}
