package restaurant.restaurant_cwagoner.gui;


import restaurant.restaurant_cwagoner.roles.CwagonerCookRole;

import java.awt.*;

public class CwagonerCookGui implements CwagonerGui {

    private CwagonerCookRole agent = null;
    CwagonerRestaurantGui cwagoner_RestaurantGui = null;
    
    private enum State { idle, goingToFridge, goingToCooking, goingToPlating }
    private State state;
    
    private String food = "";

    // GUI is a square. While not busy, wait in home position
    private int size = 20, PLATE_SIZE = 20,
    			HOME_X, HOME_Y,
    	    	xPos, yPos,
    	    	xDestination, yDestination;
    
    private int FRIDGE_X = 500, FRIDGE_Y = 100, FRIDGE_WIDTH = 100, FRIDGE_HEIGHT = 50,
    			COOKING_X = 600, COOKING_Y = 150, COOKING_WIDTH = 40, COOKING_HEIGHT = 100,
    			PLATING_X = 480, PLATING_Y = 150, PLATING_WIDTH = 20, PLATING_HEIGHT = 100;

    public CwagonerCookGui(CwagonerCookRole c, CwagonerRestaurantGui g) {
    	state = State.idle;
        agent = c;
        cwagoner_RestaurantGui = g;
        HOME_X = 500;
        HOME_Y = 150;
        xPos = xDestination = HOME_X;
        yPos = yDestination = HOME_Y;
    }

    public void updatePosition() {
        if (xPos < xDestination)		xPos++;
        else if (xPos > xDestination)	xPos--;

        if (yPos < yDestination)		yPos++;
        else if (yPos > yDestination)	yPos--;

        if (xPos == xDestination && yPos == yDestination) {
        	if (state.equals(State.goingToCooking)) {
        		
        	}

			if (! state.equals(State.idle)) {
        		state = State.idle;
        		agent.msgAnimationFinished();
			}
        }
    }

    public void draw(Graphics2D g) {
    	// Cook himself
    	g.setColor(Color.PINK);
    	g.fillRect(xPos, yPos, size, size);
    	
    	// Fridge
        g.setColor(Color.GRAY);
		g.fillRect(FRIDGE_X, FRIDGE_Y, FRIDGE_WIDTH, FRIDGE_HEIGHT);
		
		// Cooking area
		g.setColor(Color.RED);
		g.fillRect(COOKING_X, COOKING_Y, COOKING_WIDTH, COOKING_HEIGHT);
		
		// Plating area
		g.setColor(Color.BLUE);
		g.fillRect(PLATING_X, PLATING_Y, PLATING_WIDTH, PLATING_HEIGHT);


		// Draw plate if taking food to plating area
		if (state.equals(State.goingToPlating)) {
			g.setColor(Color.WHITE);
    		g.fillOval(xPos + size / 4, yPos + size / 4, PLATE_SIZE, PLATE_SIZE);
		}
		// Draw string if going to cooking or plating area
		if (state.equals(State.goingToPlating) || state.equals(State.goingToCooking)) {
    		g.setColor(Color.BLACK);
    		g.drawString(food, (int) (xPos + size / 2), yPos + size);
		}
    }

    public boolean isPresent() {
        return true;
    }

	public void DoGoToHomePosition() {
    	state = State.idle; // Prevents updatePosition() from calling animationFinished.release()
        xDestination = HOME_X;
        yDestination = HOME_Y;
    }
    
    public void DoGoToFridge() {
    	state = State.goingToFridge;
    	xDestination = FRIDGE_X + FRIDGE_WIDTH / 2;
    	yDestination = FRIDGE_Y + FRIDGE_HEIGHT;
    }
    
    public void DoGoToCooking() {
    	state = State.goingToCooking;
    	xDestination = COOKING_X - size;
    	yDestination = COOKING_Y + (COOKING_HEIGHT - size) / 2;
    }
    
    public void DoGoToPlating() {
    	state = State.goingToPlating;
    	xDestination = PLATING_X + PLATING_WIDTH;
    	yDestination = PLATING_Y + PLATE_SIZE - size;
    }
    
    
    // Adds food to GUI
    public void DoDrawFood(String f) {
    	food = f;
    }
    
    // Removes food from GUI
    public void DoClearFood() {
    	food = "";
    }
}
