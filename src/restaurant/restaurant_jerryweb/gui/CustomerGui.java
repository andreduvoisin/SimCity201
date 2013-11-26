package  restaurant.restaurant_jerryweb.gui;

import  restaurant.restaurant_jerryweb.CustomerRole;
import  restaurant.restaurant_jerryweb.HostRole;

import java.awt.*;

public class CustomerGui implements Gui{

	private CustomerRole agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;

	//private HostAgent host;
	JerrywebRestaurantGui gui;
	
	private int xPos, yPos;
	public int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, PayForMeal, LeaveRestaurant};
	private Command command=Command.noCommand;

    public static final int xTable1 = 200;
    public static final int yTable1 = 250;
	
	public static final int  xTable2 = 310;
	public static final int  yTable2 = 185;

	public static final int  xTable3 = 370;
	public static final int  yTable3 = 100;
	
	static final int cashierXpos = 50;
	static final int cashierYpos = 200;
	
	public String customerString = "";
	
	static final int custWidth = 20;
	static final int custHeight = 20;
	
	public CustomerGui(CustomerRole c, JerrywebRestaurantGui gui){ //HostAgent m) {
		agent = c;
		xPos = -40;
		yPos = -40;
		xDestination = 20;
		yDestination = 120;
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

		if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.GoToSeat) agent.msgAnimationFinishedGoToSeat();
			else if(command == Command.PayForMeal){
				agent.msgAnimationFinishedGoToCashier();
			}
			else if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				System.out.println("about to call gui.setCustomerEnabled(agent);");
				isHungry = false;
				gui.setCustomerEnabled(agent);
			}
			command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, custWidth, custHeight);
		g.setColor(Color.BLACK);
		g.drawString(customerString,  xPos +5,  yPos -5);
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

	public void DoGoToSeat(int seatnumber) {//later you will map seatnumber to table coordinates.

		if(seatnumber == 1){
			xDestination = xTable1;
			yDestination = yTable1;
			command = Command.GoToSeat;
		}
		
		if(seatnumber == 2){
			xDestination = xTable2;
			yDestination = yTable2;
			command = Command.GoToSeat;
		}
		
		if(seatnumber == 3){
			xDestination = xTable3;
			yDestination = yTable3;
			command = Command.GoToSeat;
		}
	}
	
	public void DoPayForMeal(){
		xDestination = cashierXpos + 20;
		yDestination = cashierYpos;
		command = Command.PayForMeal;
	}

	public void DoExitRestaurant() {
		xDestination = -40;
		yDestination = -40;
		command = Command.LeaveRestaurant;
	}
	
}
