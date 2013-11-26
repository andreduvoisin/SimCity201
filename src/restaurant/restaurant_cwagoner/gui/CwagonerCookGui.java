package restaurant.restaurant_cwagoner.gui;


import restaurant.restaurant_cwagoner.roles.CwagonerCookRole;

import java.awt.*;

import base.Location;

public class CwagonerCookGui implements CwagonerGui {

    private CwagonerCookRole agent = null;
    CwagonerRestaurantGui cwagoner_RestaurantGui = null;
    
    private enum State { idle, goingToFridge, goingToCooking, goingToPlating }
    private State state;
    
    private String food = "";

    // GUI is a square. While not busy, wait in home position
    private int size = 20, plateSize = 20;
    private Location position, destination, homePos,
    				fridgePos = new Location(500, 100),
    				cookingPos = new Location(600, 150),
    				platingPos = new Location(480, 150);
    private Dimension fridgeDim = new Dimension(100, 50),
    				cookingDim = new Dimension(40, 100),
    				platingDim = new Dimension(20, 100);

    public CwagonerCookGui(CwagonerCookRole c, CwagonerRestaurantGui g) {
    	state = State.idle;
        agent = c;
        cwagoner_RestaurantGui = g;
        homePos = new Location(500, 150);
        position = destination = new Location(homePos.mX, homePos.mY);
    }

    public void updatePosition() {
        if (position.mX < destination.mX)		position.mX++;
        else if (position.mX > destination.mX)	position.mX--;

        if (position.mY < destination.mY)		position.mY++;
        else if (position.mY > destination.mY)	position.mY--;

        if (position.mX == destination.mX && position.mY == destination.mY) {
        	if (state.equals(State.goingToCooking)) {
        		//CHASE: wtf
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
    	g.fillRect(position.mX, position.mY, size, size);
    	
    	// Fridge
        g.setColor(Color.GRAY);
		g.fillRect(fridgePos.mY, fridgePos.mY, fridgeDim.width, fridgeDim.height);
		
		// Cooking area
		g.setColor(Color.RED);
		g.fillRect(cookingPos.mX, cookingPos.mY, cookingDim.width, cookingDim.height);
		
		// Plating area
		g.setColor(Color.BLUE);
		g.fillRect(platingPos.mX, platingPos.mY, platingDim.width, platingDim.height);


		// Draw plate if taking food to plating area
		if (state.equals(State.goingToPlating)) {
			g.setColor(Color.WHITE);
    		g.fillOval(position.mX + size / 4, position.mY + size / 4, plateSize, plateSize);
		}
		// Draw string if going to cooking or plating area
		if (state.equals(State.goingToPlating) || state.equals(State.goingToCooking)) {
    		g.setColor(Color.BLACK);
    		g.drawString(food, (int) (position.mX + size / 2), position.mY + size);
		}
    }

    public boolean isPresent() {
        return true;
    }

	public void DoGoToHomePosition() {
    	state = State.idle; // Prevents updatePosition() from calling animationFinished.release()
        destination.mX = position.mX;
        destination.mY = position.mY;
    }
    
    public void DoGoToFridge() {
    	state = State.goingToFridge;
    	destination.mX = fridgePos.mX + fridgeDim.width / 2;
    	destination.mY = fridgePos.mY + fridgeDim.height;
    }
    
    public void DoGoToCooking() {
    	state = State.goingToCooking;
    	destination.mX = cookingPos.mX - size;
    	destination.mY = cookingPos.mX + (cookingPos.mY - size) / 2;
    }
    
    public void DoGoToPlating() {
    	state = State.goingToPlating;
    	destination.mX = platingPos.mX + platingPos.mY;
    	destination.mY = platingPos.mY + plateSize - size;
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
