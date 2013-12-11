package restaurant.restaurant_cwagoner.gui;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import restaurant.restaurant_cwagoner.CwagonerRestaurant;
import restaurant.restaurant_cwagoner.roles.CwagonerCookRole;
import base.Gui;
import base.Location;

public class CwagonerCookGui extends CwagonerBaseGui implements Gui {
    
    private enum State { idle, goingToFridge, goingToCooking, goingToPlating }
    private State state;
    BufferedImage cookImg;
    private String food = "";

    // GUI is a square. While not busy, wait in home position
    private int size = 20, plateSize = 20;
    private Location position = new Location(0, 0),
    				destination = new Location(0, 0),
    				homePos = new Location(200, 350);
    private Dimension fridgeDim = new Dimension(100, 50);

    public CwagonerCookGui(CwagonerCookRole c) {
    	super(c);
    	state = State.idle;
        position.setTo(homePos);
        destination.setTo(homePos);

        CwagonerRestaurant.addGui(this);

        java.net.URL cookURL = this.getClass().getClassLoader().getResource("restaurant/restaurant_cwagoner/gui/img/cook.png");
		try { cookImg = ImageIO.read(cookURL); } catch (Exception e) {}
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
        		((CwagonerCookRole)role).msgAnimationFinished();
			}
        }
    }

    public void draw(Graphics2D g) {

    	// Cook himself
    	g.drawImage(cookImg, position.mX, position.mY, null);


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

	public void DoGoToHomePosition() {
    	state = State.idle; // Prevents updatePosition() from calling animationFinished.release()
        destination.setTo(homePos);
    }
    
    public void DoGoToFridge() {
    	state = State.goingToFridge;
    	destination.mX = CwagonerAnimationPanel.fridgePos.mX + fridgeDim.width / 2;
    	destination.mY = CwagonerAnimationPanel.fridgePos.mY + fridgeDim.height;
    }
    
    public void DoGoToCooking() {
    	state = State.goingToCooking;
    	destination.mX = CwagonerAnimationPanel.cookingPos.mX - size;
    	destination.mY = CwagonerAnimationPanel.cookingPos.mX + (CwagonerAnimationPanel.cookingPos.mY - size) / 2;
    }
    
    public void DoGoToPlating() {
    	state = State.goingToPlating;
    	destination.mX = CwagonerAnimationPanel.platingPos.mX +CwagonerAnimationPanel. platingPos.mY;
    	destination.mY = CwagonerAnimationPanel.platingPos.mY + plateSize - size;
    }
    
    
    // Adds food to GUI
    public void DoDrawFood(String f) {
    	food = f;
    }
    
    // Removes food from GUI
    public void DoClearFood() {
    	food = "";
    }

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setPresent(boolean state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFired(boolean state) {
		// TODO Auto-generated method stub
		
	}
}
