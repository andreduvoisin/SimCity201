package restaurant.restaurant_duvoisin.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import restaurant.restaurant_duvoisin.roles.AndreCookRole;
import base.Gui;

public class CookGui implements Gui {
    private AndreCookRole agent = null;
    static final int COOKSIZE = 20;	// Size of each side of host (square).
    static final int STARTPOS_X = 250;
    static final int STARTPOS_Y = 428;
    
    static final int IDLE_X = 250;
    static final int IDLE_Y = 428;
    
    private int xPos = STARTPOS_X, yPos = STARTPOS_Y;//default waiter position
    private int xDestination = STARTPOS_X, yDestination = STARTPOS_Y;//default start position
    
    static final int FRIDGE_X = 430;
    static final int FRIDGE_Y = 428;
    
    static final int GRILL_X = 75;
    static final int GRILL_Y = 431;
    static final int PLATE_X = 75;
    static final int PLATE_Y = 425;
    static final int INCREMENT = 20;
    
    static final int STAND_X = 410 - COOKSIZE;
	static final int STAND_Y = 425;
    
    int currentPosition;
    String currentOrder;
    
    static final int TEXT_OFFSET_X = 1;
    static final int TEXT_OFFSET_Y = 15;
    
    Map<Integer, String> grilledItems = new HashMap<Integer, String>();
    Map<Integer, String> platedItems = new HashMap<Integer, String>();

    public CookGui(AndreCookRole agent) {
        this.agent = agent;
        currentOrder = "";
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
        
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == FRIDGE_X) & (yDestination == FRIDGE_Y)) {
        	agent.msgAtFridge();
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == GRILL_X + (INCREMENT * currentPosition)) & (yDestination == GRILL_Y)) {
        	agent.msgAtGrill();
        	if(!currentOrder.equals(""))
        		grilledItems.put(currentPosition, currentOrder);
        	else
        		grilledItems.remove(currentPosition);
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == PLATE_X + (INCREMENT * currentPosition)) & (yDestination == PLATE_Y)) {
        	agent.msgAtPlating();
        	if(!currentOrder.equals(""))
        		platedItems.put(currentPosition, currentOrder);
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == STAND_X) & (yDestination == STAND_Y)) {
        	agent.msgAtStand();
        }
    }

    public void draw(Graphics2D g) {
    	Integer keys[];
    	String values[];
        g.setColor(Color.RED);
        g.fillRect(xPos, yPos, COOKSIZE, COOKSIZE);
        g.setColor(Color.BLACK);
        g.drawString(currentOrder, xPos + TEXT_OFFSET_X, yPos + TEXT_OFFSET_Y);
        keys = grilledItems.keySet().toArray(new Integer[0]);
        values = grilledItems.values().toArray(new String[0]);
        for(int i = 0; i < grilledItems.size(); i++)
        	g.drawString(values[i], GRILL_X + (INCREMENT * keys[i]), GRILL_Y + 32);
    	keys = platedItems.keySet().toArray(new Integer[0]);
        values = platedItems.values().toArray(new String[0]);
        for(int i = 0; i < platedItems.size(); i++)
        	g.drawString(values[i], PLATE_X + (INCREMENT * keys[i]), PLATE_Y - 2);
    }

    public boolean isPresent() {
        return true;
    }

    public void DoGoWait() {
        xDestination = IDLE_X;
        yDestination = IDLE_Y;
    }
    
    public void DoGoToFridge() {
    	xDestination = FRIDGE_X;
    	yDestination = FRIDGE_Y;
    }
    
    public void DoCooking(int position) {
    	currentPosition = position;
    	xDestination = GRILL_X + (INCREMENT * position);
    	yDestination = GRILL_Y;
    }
    
    public void DoPlating(int position) {
    	currentPosition = position;
    	xDestination = PLATE_X + (INCREMENT * position);
    	yDestination = PLATE_Y;
    }
    
    public void DoGoToRevolvingStand() {
    	xDestination = STAND_X;
    	yDestination = STAND_Y;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    
    public void removePlatedItem(int position) { platedItems.remove(position); }
    
    public void setCurrentOrder(String co) {
    	switch(co) {
			case "steak":
				currentOrder = "ST";
				break;
			case "chicken":
				currentOrder = "CH";
				break;
			case "salad":
				currentOrder = "SA";
				break;
			case "pizza":
				currentOrder = "PI";
				break;
			default:
				currentOrder = "";
				break;
    	}
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
