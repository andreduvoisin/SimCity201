package restaurant.restaurant_jerryweb.gui;


import restaurant.restaurant_jerryweb.HostRole;
import restaurant.restaurant_jerryweb.CustomerRole;
import restaurant.restaurant_jerryweb.WaiterRole;
import restaurant.restaurant_jerryweb.interfaces.Customer;
import restaurant.restaurant_jerryweb.interfaces.Waiter;

import java.awt.*;

public class WaiterGui implements Gui {

    private Waiter agent = null;
    private HostRole host = null;
    private boolean AtOrigin = true;
    private boolean AtCustomerQue = false;
    RestaurantGui gui;
    
   
 
    private int xPos = -20, yPos = -20;//default waiter position
    private int xDestination = 120, yDestination = 35;//default start position
    public static final int xTable1 = 200;
    public static final int yTable1 = 250;
	
	public static final int  xTable2 = 310;
	public static final int  yTable2 = 185;

	public static final int  xTable3 = 370;
	public static final int  yTable3 = 100;
	
	public static final int  xCookLocation = 225;
	public static final int  yCookLocation = 50;
	
	static final int cashierXpos = 50;
	static final int cashierYpos = 200;
	
	public static final int xServeDestination1 = xTable1 + 22;
	public static final int yServeDestination1 = yTable1 + 22;
	
	public static final int xServeDestination2 = xTable2 + 22;
	public static final int yServeDestination2 = yTable2 + 22;
	
	public static final int xServeDestination3 = xTable3 + 22;
	public static final int yServeDestination3 = yTable3 + 22;
	
	public int previousX = 0;
	public int previousY = 0;
	int idleSpotX = 0;
	
	static final int waiterWidth = 20;
	static final int waiterHeight = 20;
	static final int xIndex = 20;
	static final int yIndex = 20;
	
	private boolean currentlyAtTable = false;
	
	//These are the strings variables that will be draw onto the animation panel. They're values can be changed by the corresponding agent
	//actions 
	public String table1String = "";
	public String table2String = "";
	public String table3String = "";
	public String waiterString = "";
	public String customerString1 = "";
	public String customerString2 = "";
	public String customerString3 = "";
	public String cookingFood1 = "";
	public String cookingFood2 = "";
	public String cookingFood3 = "";
	
	public String plate1 = "";
	public String plate2 = "";
	public String plate3 = "";
	
    public WaiterGui(Waiter w, RestaurantGui gui, HostRole h) {
       agent = w;
       host = h;
        this.gui = gui;
        idleSpotX = 25*host.Waiters.size();
    }

    public void updatePosition() {
    	 previousX = xPos;
    	 previousY = yPos;
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
        	if( xPos != previousX || yPos !=  previousY ){
        		agent.msgAtTable();
        	}
        }
        
        if(xPos == xCookLocation && yPos == yCookLocation +20 ){ 
        	agent.msgAtCook();
        	
        }
        
        if(xPos == cashierXpos +20 && yPos == cashierYpos ){ 
        	agent.msgAtCashier();
        	
        }
          
        if(xPos == 120  && yPos == 35 && AtOrigin == false){ 
        	//agent.msgAtOrigin();
        	agent.msgAtTable();
        	//agent.msgAtCook();
        	AtOrigin = true;
        }
        
        if(xPos == 40  && yPos == 140 && AtCustomerQue == false){
        	
        	AtCustomerQue = true;
        	//agent.msgAtOrigin();
        	agent.msgAtTable();
        	//agent.msgAtCook();

        }
        
              
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(xPos, yPos, waiterWidth, waiterHeight);

        g.setColor(Color.BLACK);
        g.drawString(waiterString,xPos+20, yPos);
       
        
        g.drawString(table1String, xTable1 +15, yTable1 +35);
        g.drawString(customerString1,  xTable1 +5,  yTable1 -5);
        g.drawString(table2String, xTable2 +15, yTable2 +35);
        g.drawString(customerString2,  xTable2 +5,  yTable2 -5);
        g.drawString(table3String, xTable3 +15, yTable3 +35);
        g.drawString(customerString3,  xTable3 +5,  yTable3 -5);
        
        g.setColor(Color.WHITE);
        g.drawString(plate1, xCookLocation, yCookLocation  +20);
        g.drawString(plate2, xCookLocation + 40 , yCookLocation + 20);
        g.drawString(plate3, xCookLocation + 80 , yCookLocation  +20); 
        
        g.drawString(cookingFood1, xCookLocation, yCookLocation  - 30);
        g.drawString(cookingFood2, xCookLocation + 40 , yCookLocation - 30);
        g.drawString(cookingFood3, xCookLocation + 80 , yCookLocation  - 30); 
    }

    public boolean isPresent() {
        return true;
    }
    
    public boolean getIsAtOrigin(){
    	return AtOrigin;
    }
    
    public boolean getIsAtCustomerQue(){
    	return AtCustomerQue;
    }


    public void DoBringToTable(Customer customer, int tableNumber) {
    	AtOrigin = false;
    	AtCustomerQue = false;
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
    
    public void DoGoToTable(Customer c, int tableNumber){
    	AtOrigin = false;
    	AtCustomerQue = false;
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
    
    public void DoGoToCook(){
    	AtOrigin = false;
    	AtCustomerQue = false;
    	xDestination = xCookLocation;
		yDestination = yCookLocation + 20;
    }
    
    public void DoGoToCashier(){
    	AtOrigin = false;
    	AtCustomerQue = false;
    	xDestination = cashierXpos + 20;
		yDestination = cashierYpos;
    }
    
    public void DoServeCustomer(int tableNumber){
    	AtCustomerQue = false;
    	AtOrigin = false;
    	if(tableNumber == 1 ){
        	xDestination = xTable1 + 22;
        	yDestination = yTable1 - 22;
    	}
    	
    	if(tableNumber == 2 ){
    		xDestination = xTable2 + 22;
    		yDestination = yTable2 - 22;
    	}
    	
    	if(tableNumber == 3 ){
    		xDestination = xTable3 + 22;
    		yDestination = yTable3 - 22;
    	}
    }
    
    public void GoToIdleSpot() {
    	
		xDestination = 145 - idleSpotX;
		        yDestination = 35;
    }
    
    public void DoLeaveCustomer() {
        xDestination = 40;
        yDestination = 140;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
