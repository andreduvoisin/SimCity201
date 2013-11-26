package restaurant.restaurant_jerryweb.gui;


import restaurant.restaurant_jerryweb.CustomerRole;
import restaurant.restaurant_jerryweb.HostRole;

import java.awt.*;

public class HostGui implements Gui {

    private HostRole agent = null;
    private boolean AtOrigin = true;
    
    
    private int xPos = -20, yPos = -20;//default waiter position
    private int xDestination = -20, yDestination = -20;//default start position
 
    
    public static final int xTable1 = 200;
    public static final int yTable1 = 250;
	
	public static final int  xTable2 = 275;
	public static final int  yTable2 = 175;

	public static final int  xTable3 = 350;
	public static final int  yTable3 = 100;
	
	static final int hostWidth = 20;
	static final int hostHeight = 20;
	static final int xIndex = 20;
	static final int yIndex = 20;
	
    public HostGui(HostRole agent) {
        this.agent = agent;
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

        if ((xPos == xDestination && yPos == yDestination
        		& (xDestination == xTable1 + xIndex) & (yDestination == yTable1 - yIndex)) || (xPos == xDestination && yPos == yDestination
                & (xDestination == xTable2 + xIndex) & (yDestination == yTable2 - yIndex)) || (xPos == xDestination && yPos == yDestination
                		& (xDestination == xTable3 + xIndex) & (yDestination == yTable3 - yIndex))) {
        	
           //agent.msgAtTable();
        }
          
        if(xPos == -20  && yPos == -20 && AtOrigin == false){ 
        	//agent.msgAtTable();
        	AtOrigin = true;
        }
       
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, hostWidth, hostHeight);
    }

    public boolean isPresent() {
        return true;
    }

    public void DoBringToTable(CustomerRole customer, int tableNumber) {
    	AtOrigin = false;
    	if(tableNumber == 1 ){
        	xDestination = xTable1 + 20;
        	yDestination = yTable1 - 20;
    	}
    	
    	if(tableNumber == 2 ){
    		xDestination = xTable2 + 20;
    		yDestination = yTable2 - 20;
    	}
    	
    	if(tableNumber == 3 ){
    		xDestination = xTable3 + 20;
    		yDestination = yTable3 - 20;
    	}
                
    }

    public void DoLeaveCustomer() {
        xDestination = -20;
        yDestination = -20;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
