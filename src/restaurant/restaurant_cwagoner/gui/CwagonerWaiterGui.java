package restaurant.restaurant_cwagoner.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import restaurant.restaurant_cwagoner.CwagonerRestaurant;
import restaurant.restaurant_cwagoner.interfaces.CwagonerWaiter;
import base.Gui;
import base.Location;
import base.interfaces.Role;

public class CwagonerWaiterGui extends CwagonerBaseGui implements Gui {

	private static int waiterNum = 0;
    
    private enum State { idle, gettingCustomer, movingToTable, movingToCook,
    						movingToCashier, onBreak }
    private State state;
    private String food = "";

    // GUI is a square. While not busy, wait in home position
    private int size = 20, plateSize = 20;
    private Location homePos = new Location(100 + waiterNum * (size + 10), 2 * size),
    				cookPos = new Location(180, 350),
    				cashierPos = new Location(-size, 100),
    				position,
    				destination;
    
    BufferedImage waiterImg;

    public CwagonerWaiterGui(CwagonerWaiter w) {
    	super((Role)w);
    	state = State.idle;
        waiterNum++;

        CwagonerRestaurant.addGui(this);

        position = new Location(homePos.mX, homePos.mY);
        destination = new Location(homePos.mX, homePos.mY);

        try {
			java.net.URL waiterURL = this.getClass().getClassLoader().getResource("restaurant/restaurant_cwagoner/gui/img/waiter.png");
			waiterImg = ImageIO.read(waiterURL);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public void updatePosition() {
        if (position.mX < destination.mX)		position.mX++;
        else if (position.mX > destination.mX)	position.mX--;

        if (position.mY < destination.mY)		position.mY++;
        else if (position.mY > destination.mY)	position.mY--;

        if (position.mX == destination.mX && position.mY == destination.mY) {
			if (! state.equals(State.idle)) {
        		state = State.idle;
        		((CwagonerWaiter)role).msgAnimationFinished();
			}
        }
    }

    public void draw(Graphics2D g) {
		g.drawImage(waiterImg, position.mX, position.mY, null);
		
		if (! food.equals("")) {	// Waiter is carrying food
			g.setColor(Color.WHITE);
    		g.fillOval(position.mX + size / 4, position.mY + size / 4, plateSize, plateSize);
    		g.setColor(Color.BLACK);
    		g.drawString(food, (int) (position.mX + size / 2), position.mY + size);
		}
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
    	Location tableLoc = CwagonerAnimationPanel.getTableLocation(tableNum);
        destination.mX = tableLoc.mX + size;
        destination.mY = tableLoc.mY - size;
    }

    public void DoGoToHomePosition() {
    	state = State.idle; // Prevents updatePosition() from calling animationFinished.release()
        destination.setTo(homePos);
    }

    public void DoGoToCook() {
    	state = State.movingToCook;
    	destination.setTo(cookPos);
    }
    
    public void DoDeliverFood(int tableNum) {
    	state = State.movingToTable;
    	DoGoToTable(tableNum);
    }
    
    public void DoGoToCashier() {
    	state = State.movingToCashier;
    	destination.setTo(cashierPos);
    }
    
    // Adds food to waiter's GUI (to drop it off at customer)
    public void DoDrawFood(String f) {
    	food = f;
    }
    
    // Removes food from waiter's GUI (dropped off at customer)
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
}
