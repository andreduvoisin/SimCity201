package restaurant_xurex.gui;

import restaurant_xurex.CustomerAgent;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class CustomerGui implements Gui{

	private CustomerAgent agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;
	
	RestaurantGui gui;
	
	private static int sNum = 0;
	private final int mNum;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant};
	private Command command=Command.noCommand;
	
	static final int custDim = 10;
	private int wAreax;
	private static final int wAreay = 10;
	
    public Map<Integer, Point> places = new HashMap<Integer, Point>();

	public CustomerGui(CustomerAgent c, RestaurantGui gui){
		agent = c;
		xPos = -40;
		yPos = -40;
		xDestination = -40;
		yDestination = -40;
		this.gui = gui;
		places.put(new Integer(1), new Point(200,150));
		places.put(new Integer(2), new Point(300,150));
		places.put(new Integer(3), new Point(200,250));
		places.put(new Integer(4), new Point(300,250));
		places.put(new Integer(5), new Point(25, 50)); //Cashier 
		
		mNum = sNum ++;
		wAreax = 30 + 20*mNum;
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

		if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.GoToSeat){
				agent.msgAnimationFinishedGoToSeat();
			}
			else if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				isHungry = false;
				gui.setCustomerEnabled(agent);
			}
			command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, custDim, custDim);
	}

	public boolean isPresent() {
		return isPresent;
	}
	public void setHungry() {
		isHungry = true;
		agent.gotHungry();
		setPresent(true);
	}
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void DoGoToSeat(int seatnumber) {
		xDestination = places.get(seatnumber).getX();
		yDestination = places.get(seatnumber).getY();
		
		command = Command.GoToSeat;
	}

	public void DoExitRestaurant() {
		xDestination = -40;
		yDestination = -40;
		command = Command.LeaveRestaurant;
	}
	
	public void DoGoArea(){
		xDestination = wAreax;
		yDestination = wAreay;
	}
	
	public void SetCustomerEnabled() {
		gui.setCustomerEnabled(agent);
	}
}