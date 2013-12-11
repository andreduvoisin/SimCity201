package restaurant.restaurant_duvoisin.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import restaurant.restaurant_duvoisin.AndreRestaurant;
import restaurant.restaurant_duvoisin.interfaces.Waiter;
import restaurant.restaurant_duvoisin.roles.AndreSharedWaiterRole;
import restaurant.restaurant_duvoisin.roles.AndreWaiterRole;
import base.Gui;

public class WaiterGui implements Gui {

    private Waiter agent = null;
    static final int WAITERSIZE = 20;	// Size of each side of host (square).
    static final int STARTPOS = -20;
    private int currentTable = 0;
    
    private boolean onFire = false;
	private BufferedImage fireImage;
    
    private int xPos = STARTPOS, yPos = STARTPOS;//default waiter position
    private int xDestination = STARTPOS, yDestination = STARTPOS;//default start position
    
    static final int COOK_X = 75;
    static final int COOK_Y = 390;
    static final int COOK_INCREMENT = 20;
    static final int CASHIER_X = 550;
    static final int CASHIER_Y = 175;
    static final int COOK_DELIVER_X = 250;
    static final int COOK_DELIVER_Y = 390;
    
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
	
	static final int STAND_X = 410 - WAITERSIZE;
	static final int STAND_Y = 401 - WAITERSIZE;
    
    private String currentOrder;
    int foodPosition;

    public WaiterGui(AndreSharedWaiterRole role) {
        this.agent = role;
        
        currentOrder = "";
        
        for(int i = 0; i < AndreRestaurant.idleHere.length; i++)
			if(AndreRestaurant.idleHere[i] == false) {
				IDLE_Y += IDLE_INCREMENT * i;
				AndreRestaurant.idleHere[i] = true;
				break;
			}
        
        fireImage = null;
    	try {
    		java.net.URL imageURL = this.getClass().getClassLoader().getResource("city/gui/images/fire.png");
    		fireImage = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		System.out.println(e.getMessage());
    	}
    }
    
    public WaiterGui(AndreWaiterRole role) {
        this.agent = role;
        
        currentOrder = "";
        
        for(int i = 0; i < AndreRestaurant.idleHere.length; i++)
			if(AndreRestaurant.idleHere[i] == false) {
				IDLE_Y += IDLE_INCREMENT * i;
				AndreRestaurant.idleHere[i] = true;
				break;
			}
        
        fireImage = null;
    	try {
    		java.net.URL imageURL = this.getClass().getClassLoader().getResource("city/gui/images/fire.png");
    		fireImage = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		System.out.println(e.getMessage());
    	}
    }

    public void updatePosition() {
    	if(!onFire){
        if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;

        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == AndreRestaurant.tgui.getTableX(currentTable) + WAITERSIZE) & (yDestination == AndreRestaurant.tgui.getTableY(currentTable) - WAITERSIZE)) {
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
    }

    public void draw(Graphics2D g) {
    	if(onFire)
			g.drawImage(fireImage, xPos, yPos, null);
		else{
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, WAITERSIZE, WAITERSIZE);
        g.setColor(Color.BLACK);
		g.drawString(currentOrder, xPos + TEXT_OFFSET_X, yPos + TEXT_OFFSET_Y);}
    }

    public boolean isPresent() {
        return true;
    }

    public void DoGoToTable(int table) {
    	currentTable = table - 1;
        xDestination = AndreRestaurant.tgui.getTableX(currentTable) + WAITERSIZE;
        yDestination = AndreRestaurant.tgui.getTableY(currentTable) - WAITERSIZE;
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
    
    public void enableButton() { }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    
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
		onFire = state;
	}
}
