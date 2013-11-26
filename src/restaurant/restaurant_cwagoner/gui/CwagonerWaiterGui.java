package restaurant.restaurant_cwagoner.gui;


import restaurant.restaurant_cwagoner.roles.CwagonerWaiterRole;

import java.awt.*;

import base.Location;

public class CwagonerWaiterGui implements CwagonerGui {

	private static int waiterNum = 0;

    private CwagonerWaiterRole agent = null;
    CwagonerRestaurantGui RestaurantGui = null;
    
    private enum State { idle, gettingCustomer, movingToTable, movingToCook,
    						movingToCashier, onBreak }
    private State state;
    private String food = "";

    // GUI is a square. While not busy, wait in home position
    private int size = 20, plateSize = 20;
    private Location homePos = new Location(100 + waiterNum * (size + 10), 2 * size),
    				cookPos = new Location(460, 150),
    				cashierPos = new Location(-size, 100),
    				position,
    				destination;

    public CwagonerWaiterGui(CwagonerWaiterRole w, CwagonerRestaurantGui g) {
    	state = State.idle;
        agent = w;
        RestaurantGui = g;
        waiterNum++;

        position.mX = destination.mX = homePos.mX;
        position.mY = destination.mY = homePos.mY;
    }

    public void updatePosition() {
        if (position.mX < destination.mX)		position.mX++;
        else if (position.mX > destination.mX)	position.mX--;

        if (position.mY < destination.mY)		position.mY++;
        else if (position.mY > destination.mY)	position.mY--;

        if (position.mX == destination.mX && position.mY == destination.mY) {
			if (! state.equals(State.idle)) {
        		state = State.idle;
        		agent.msgAnimationFinished();
			}
        }
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.LIGHT_GRAY);
		g.fillRect(position.mX, position.mY, size, size);
		
		if (food.equals("")) {
	    	g.setColor(Color.LIGHT_GRAY);
	    	g.fillRect(position.mX, position.mY, size, size);
		}
		else {	// Waiter is carrying food
			g.setColor(Color.WHITE);
    		g.fillOval(position.mX + size / 4, position.mY + size / 4, plateSize, plateSize);
    		g.setColor(Color.BLACK);
    		g.drawString(food, (int) (position.mX + size / 2), position.mY + size);
		}
    }

    public boolean isPresent() {
        return true;
    }

    public void DoGoGetCustomer(Dimension custPos) {
    	state = State.gettingCustomer;
    	destination.mX = custPos.width + size;
    	destination.mY = custPos.height - size;
    }
    
    // Used for bringing customer to table,
    // and returning to table to take order or deliver food
    public void DoGoToTable(int tableNum) {
    	state = State.movingToTable;
    	Location tableLoc = RestaurantGui.getTableLocation(tableNum);
        destination.mX = tableLoc.mX + size;
        destination.mY = tableLoc.mY - size;
    }

    public void DoGoToHomePosition() {
    	state = State.idle; // Prevents updatePosition() from calling animationFinished.release()
        destination.mX = homePos.mX;
        destination.mY = homePos.mY;
    }

    public void DoGoToCook() {
    	state = State.movingToCook;
    	destination.mX = cookPos.mX;
    	destination.mY = cookPos.mY;
    }
    
    public void DoDeliverFood(int tableNum) {
    	state = State.movingToTable;
    	DoGoToTable(tableNum);
    }
    
    public void DoGoToCashier() {
    	state = State.movingToCashier;
    	destination.mX = cashierPos.mX;
    	destination.mY = cashierPos.mY;
    }
    
    // Adds food to waiter's GUI (to drop it off at customer)
    public void DoDrawFood(String f) {
    	food = f;
    }
    
    // Removes food from waiter's GUI (dropped off at customer)
    public void DoClearFood() {
    	food = "";
    }
}
