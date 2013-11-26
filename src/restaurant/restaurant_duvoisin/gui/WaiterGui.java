package restaurant.restaurant_duvoisin.gui;

import restaurant.restaurant_duvoisin.interfaces.Waiter;
import java.awt.*;

public class WaiterGui implements Gui {

    private Waiter agent = null;
    static final int WAITERSIZE = 20;	// Size of each side of host (square).
    static final int STARTPOS = -20;
    private int currentTable = 0;
    
    AndreRestaurantGui gui;
    
    private int xPos = STARTPOS, yPos = STARTPOS;//default waiter position
    private int xDestination = STARTPOS, yDestination = STARTPOS;//default start position
    
    static final int COOK_X = 50;
    static final int COOK_Y = 290;
    static final int COOK_INCREMENT = 20;
    static final int CASHIER_X = 470;
    static final int CASHIER_Y = 185;
    static final int COOK_DELIVER_X = 225;
    static final int COOK_DELIVER_Y = 290;
    //static final int WAIT_X = 215;
    //static final int WAIT_Y = 100;
    static final int BREAK_X = 420;
    static final int BREAK_Y = 10;
    static final int TEXT_OFFSET_X = 1;
    static final int TEXT_OFFSET_Y = 15;
    static final int IDLE_X = 15;
    int IDLE_Y = 57;
    static final int IDLE_INCREMENT = 25;
    
    private int CUSTOMER_WAIT_X = 15;
	static final int CUSTOMER_WAIT_Y = 15;
	static final int CUSTOMER_WAIT_INCREMENT = 25;
	int CUSTOMER_WAIT_POSITION;
	
	static final int STAND_X = 385 - WAITERSIZE;
	static final int STAND_Y = 305 - WAITERSIZE;
    
    TableGui myTables;
    
    private String currentOrder;
    int foodPosition;

    public WaiterGui(Waiter agent, AndreRestaurantGui gui) {
        this.agent = agent;
        this.gui = gui;
        currentOrder = "";
        
        for(int i = 0; i < gui.idleHere.length; i++)
			if(gui.idleHere[i] == false) {
				IDLE_Y += IDLE_INCREMENT * i;
				gui.idleHere[i] = true;
				break;
			}
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
        		& (xDestination == myTables.getTableX(currentTable) + WAITERSIZE) & (yDestination == myTables.getTableY(currentTable) - WAITERSIZE)) {
        	agent.msgAtTable();
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == CUSTOMER_WAIT_X + 20 + (CUSTOMER_WAIT_POSITION * CUSTOMER_WAIT_INCREMENT)) & (yDestination == CUSTOMER_WAIT_Y + 20)) {
        	agent.msgAtCustomer();
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == COOK_X + (COOK_INCREMENT * foodPosition)) & (yDestination == COOK_Y)) {
        	agent.msgAtCook();
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == COOK_DELIVER_X) & (yDestination == COOK_DELIVER_Y)) {
        	agent.msgAtCook();
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == STAND_X) & (yDestination == STAND_Y)) {
        	agent.msgAtCook();
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == CASHIER_X) & (yDestination == CASHIER_Y)) {
        	agent.msgAtCashier();
        }
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, WAITERSIZE, WAITERSIZE);
        g.setColor(Color.BLACK);
		g.drawString(currentOrder, xPos + TEXT_OFFSET_X, yPos + TEXT_OFFSET_Y);
    }

    public boolean isPresent() {
        return true;
    }

    public void DoGoToTable(int table) {
    	currentTable = table - 1;
        xDestination = myTables.getTableX(currentTable) + WAITERSIZE;
        yDestination = myTables.getTableY(currentTable) - WAITERSIZE;
    }

    public void DoGoWait() {
        xDestination = IDLE_X;
        yDestination = IDLE_Y;
    }
    
    public void DoGoToCook(int position) {
    	foodPosition = position;
    	xDestination = COOK_X + (COOK_INCREMENT * foodPosition);
    	yDestination = COOK_Y;
    }
    
    public void DoGiveOrderToCook() {
    	xDestination = COOK_DELIVER_X;
    	yDestination = COOK_DELIVER_Y;
    }
    
    public void DoGoToCashier() {
    	xDestination = CASHIER_X;
    	yDestination = CASHIER_Y;
    }
    
    public void DoGoToCustomer(int waitingPosition) {
    	CUSTOMER_WAIT_POSITION = waitingPosition;
    	xDestination = CUSTOMER_WAIT_X + 20 + (CUSTOMER_WAIT_POSITION * CUSTOMER_WAIT_INCREMENT);
    	yDestination = CUSTOMER_WAIT_Y + 20;
    }
    
    public void DoGoOnBreak() {
    	xDestination = BREAK_X;
    	yDestination = BREAK_Y;
    }
    
    public void DoGoOffBreak() {
    	xDestination = IDLE_X;
    	yDestination = IDLE_Y;
    	enableButton();
    }
    
    public void DoGoToRevolvingStand() {
    	xDestination = STAND_X;
    	yDestination = STAND_Y;
    }
    
    public void enableButton() { gui.setWaiterEnabled(agent); }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    
    public void setTables(TableGui tg) { myTables = tg; }
    
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
}
