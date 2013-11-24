package restaurant.restaurant_cwagoner.gui;


import restaurant.restaurant_cwagoner.WaiterAgent;

import java.awt.*;

public class WaiterGui implements Gui {

    private WaiterAgent agent = null;
    RestaurantGui restaurantGui = null;
    
    private enum State { idle, gettingCustomer, movingToTable, movingToCook,
    						movingToCashier, onBreak }
    private State state;
    private String food = "";

    // GUI is a square. While not busy, wait in home position
    private int size = 20;
    private int HOME_X, HOME_Y,
    			COOK_X = 460, COOK_Y = 150,
    			CASHIER_X = -size, CASHIER_Y = 100,
    			PLATE_SIZE = 20,
    	    	xPos, yPos,
    	    	xDestination, yDestination;

    public WaiterGui(WaiterAgent w, RestaurantGui g, int waiterNum) {
    	state = State.idle;
        agent = w;
        restaurantGui = g;
        HOME_X = 100 + waiterNum * (size + 10);
        HOME_Y = size;
        xPos = xDestination = HOME_X;
        yPos = yDestination = HOME_Y;
    }

    public void updatePosition() {
        if (xPos < xDestination)		xPos++;
        else if (xPos > xDestination)	xPos--;

        if (yPos < yDestination)		yPos++;
        else if (yPos > yDestination)	yPos--;

        if (xPos == xDestination && yPos == yDestination) {
			if (! state.equals(State.idle)) {
        		state = State.idle;
        		agent.msgAnimationFinished();
			}
        }
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.LIGHT_GRAY);
		g.fillRect(xPos, yPos, size, size);
		
		if (food.equals("")) {
	    	g.setColor(Color.LIGHT_GRAY);
	    	g.fillRect(xPos, yPos, size, size);
		}
		else {	// Waiter is carrying food
			g.setColor(Color.WHITE);
    		g.fillOval(xPos + size / 4, yPos + size / 4, PLATE_SIZE, PLATE_SIZE);
    		g.setColor(Color.BLACK);
    		g.drawString(food, (int) (xPos + size / 2), yPos + size);
		}
    }

    public boolean isPresent() {
        return true;
    }

    public void DoGoGetCustomer(Dimension custPos) {
    	state = State.gettingCustomer;
    	xDestination = custPos.width + size;
    	yDestination = custPos.height - size;
    }
    
    // Used for bringing customer to table,
    // and returning to table to take order or deliver food
    public void DoGoToTable(int tableNum) {
    	state = State.movingToTable;
    	Dimension tableLoc = restaurantGui.getTableLocation(tableNum);
        xDestination = tableLoc.width + size;
        yDestination = tableLoc.height - size;
    }

    public void DoGoToHomePosition() {
    	state = State.idle; // Prevents updatePosition() from calling animationFinished.release()
        xDestination = HOME_X;
        yDestination = HOME_Y;
    }

    public void DoGoToCook() {
    	state = State.movingToCook;
    	xDestination = COOK_X;
    	yDestination = COOK_Y;
    }
    
    public void DoDeliverFood(int tableNum) {
    	state = State.movingToTable;
    	DoGoToTable(tableNum);
    }
    
    public void DoGoToCashier() {
    	state = State.movingToCashier;
    	xDestination = CASHIER_X;
    	yDestination = CASHIER_Y;
    }
    
    // Adds food to waiter's GUI (to drop it off at customer)
    public void DoDrawFood(String f) {
    	food = f;
    }
    
    // Removes food from waiter's GUI (dropped off at customer)
    public void DoClearFood() {
    	food = "";
    }
    
    public void onBreak(boolean allowed) {
    	if (allowed) {
	    	state = State.onBreak;
	    	xDestination = HOME_X;
	    	yDestination = HOME_Y;
    	}
    	else {
        	state = State.idle;
    	}
    	restaurantGui.waiterOnBreak(allowed, (WaiterAgent) agent);
    }
    
    public boolean isOnBreak() {
    	return state.equals(State.onBreak);
    }
}
